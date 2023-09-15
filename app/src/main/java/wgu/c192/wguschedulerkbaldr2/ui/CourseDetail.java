package wgu.c192.wguschedulerkbaldr2.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import wgu.c192.wguschedulerkbaldr2.R;
import wgu.c192.wguschedulerkbaldr2.database.Repository;
import wgu.c192.wguschedulerkbaldr2.entities.Course;

public class CourseDetail extends AppCompatActivity {

    public static final String MODE_KEY = "mode";
    public static final int MODE_VIEW = 0;
    public static final int MODE_ADD = 1;
    public static final int MODE_EDIT = 2;

    private Course selectedCourse = null;
    private TextInputLayout course_text_input_layout;
    private EditText courseTitleEditText;
    private Button addCourseButton;
    private Button cancelCourseButton;
    private TextView startDate;
    private TextView endDate;
    private DatePickerDialog startDatePicker;
    private DatePickerDialog endDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        int courseId = getIntent().getIntExtra("COURSE_ID", -1);
        int mode = getIntent().getIntExtra(MODE_KEY, MODE_VIEW);

        course_text_input_layout = findViewById(R.id.course_text_input_layout);
        courseTitleEditText = findViewById(R.id.courseTitleTextview);
        addCourseButton = findViewById(R.id.addBtn);
        cancelCourseButton = findViewById(R.id.cancelBtn);
        startDate = findViewById(R.id.courseStartDateLabel);
        endDate = findViewById(R.id.courseEndDateLabel);

        if (courseId != -1) {
            Repository repository = new Repository(getApplication());
            selectedCourse = repository.getAssociatedCourse(courseId);
        }

        if (mode == MODE_VIEW && selectedCourse != null) {
            buildActionBar();
            setViewMode(selectedCourse);
        } else if (mode == MODE_ADD) {
            setAddMode();
        } else {
            setEditMode(selectedCourse);
        }

        initDatePickers();
    }

    private void setViewMode(Course course) {
        ActionBar actionBar = getSupportActionBar(); // Assuming you are using AppCompatActivity

        if (actionBar != null) {
            actionBar.setTitle(course.getCourseTitle()); // Set the new title here
        }

        courseTitleEditText.setText(course.getCourseTitle());
        startDate.setText(course.getCourseStart());
        endDate.setText(course.getCourseEnd());
        // Find the CourseListFragment and pass termID as an argument
        FragmentManager fragmentManager = getSupportFragmentManager();
        AssessmentListFragment fragment = (AssessmentListFragment) fragmentManager.findFragmentById(R.id.coursefragmentcontainter);
        Bundle args = new Bundle();
        args.putInt("courseID", course.getCourseID());
        fragment.setArguments(args);
        courseTitleEditText.setFocusable(false);
        addCourseButton.setVisibility(View.INVISIBLE);
        cancelCourseButton.setVisibility(View.INVISIBLE);
        Button showPopupButton = findViewById(R.id.show_popup_button);
        showPopupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an instance of your popup fragment
                MyPopupFragment popupFragment = new MyPopupFragment();

                // Show the fragment as a dialog
                popupFragment.show(getSupportFragmentManager(), "MyPopupFragment");
            }
        });
    }

    private void setAddMode() {
        courseTitleEditText.setFocusable(true);
        startDate.setFocusable(true);
        endDate.setFocusable(true);

        addCourseButton.setVisibility(View.VISIBLE);
        cancelCourseButton.setVisibility(View.VISIBLE);
    }

    private void initDatePickers() {
        startDatePicker = createDatePickerDialog(startDate);
        endDatePicker = createDatePickerDialog(endDate);
    }

    private DatePickerDialog createDatePickerDialog(final TextView dateLabel) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(selectedYear, selectedMonth, selectedDay);
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
                String formattedDate = sdf.format(selectedDate.getTime());
                dateLabel.setText(formattedDate);
            }
        }, year, month, day);

        return datePickerDialog;
    }

    public void addOrEditCourse(View view) {
        if (!courseTitleEditText.getText().toString().trim().isEmpty()) {

            String newTitle = courseTitleEditText.getText().toString().trim();
            String newStartDate = startDate.getText().toString();
            String newEndDate = endDate.getText().toString();


            Repository repository = new Repository(getApplication());

            if (isAddMode()) {
                Course course = new Course(newTitle, newStartDate, newEndDate);
                repository.insert(course);
            }
            if (isEditMode()) {
                selectedCourse.setCourseTitle(newTitle);
                selectedCourse.setCourseStart(newStartDate);
                selectedCourse.setCourseEnd(newEndDate);
                repository.update(selectedCourse);
            }

            Intent viewIntent = new Intent(CourseDetail.this, CourseList.class);
            viewIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(viewIntent);

            finish();
        } else {
            Toast toast = Toast.makeText(this, "Title cannot be blank", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
        }
    }

    public void onStartDateLabelClick(View view) {
        if (isEditMode() || isAddMode()) {
            startDatePicker.show();
        }
    }

    public void onEndDateLabelClick(View view) {
        if (isEditMode() || isAddMode()) {
            endDatePicker.show();
        }
    }

    private boolean isEditMode() {
        int mode = getIntent().getIntExtra(MODE_KEY, MODE_VIEW);
        return mode == MODE_EDIT;
    }

    private void setEditMode(Course course) {
        courseTitleEditText.setText(course.getCourseTitle());
        startDate.setText(course.getCourseStart());
        endDate.setText(course.getCourseEnd());

        FragmentManager fragmentManager = getSupportFragmentManager();
        CourseListFragment fragment = (CourseListFragment) fragmentManager.findFragmentById(R.id.coursefragmentcontainter);
        Bundle args = new Bundle();
        args.putInt("courseID", course.getCourseID());
        fragment.setArguments(args);

        setAddMode();
    }

    private boolean isAddMode() {
        int mode = getIntent().getIntExtra(MODE_KEY, MODE_VIEW);
        return mode == MODE_ADD;
    }

    public void cancel(View view) {
        // After adding the term, switch to viewing mode by starting a new instance of the activity
        Intent viewIntent = new Intent(CourseDetail.this, CourseList.class);
        viewIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(viewIntent);
    }

    private void buildActionBar() {


        // Customize the elements of the ActionBar
        ImageView backButton = findViewById(R.id.back_button);
        TextView titleView = findViewById(R.id.actionbar_title);
        ImageView menuIcon = findViewById(R.id.menu_icon);

        // Set click listeners for backButton and menuIcon here to handle actions
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle menu icon click
                // You can show a popup menu with "Edit" and "Delete" options here
            }
        });

        // Set the title dynamically
        titleView.setText("Your Title");
    }
}

