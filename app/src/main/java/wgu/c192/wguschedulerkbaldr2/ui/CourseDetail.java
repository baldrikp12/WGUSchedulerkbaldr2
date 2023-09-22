package wgu.c192.wguschedulerkbaldr2.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import wgu.c192.wguschedulerkbaldr2.R;
import wgu.c192.wguschedulerkbaldr2.database.Repository;
import wgu.c192.wguschedulerkbaldr2.entities.Course;
import wgu.c192.wguschedulerkbaldr2.entities.Term;
import wgu.c192.wguschedulerkbaldr2.util.ReminderManager;

public class CourseDetail extends AppCompatActivity {

    public static final String MODE_KEY = "mode";
    public static final int MODE_VIEW = 0;
    public static final int MODE_ADD = 1;
    public static final int MODE_EDIT = 2;
    private static final String[] courseStatusArray = {"COURSE STATUS", "in progress", "completed", "dropped", "plan to take"};

    private Course selectedCourse = null;
    private TextInputLayout course_text_input_layout;
    private EditText courseTitleEditText;
    private Button addCourseButton;
    private Button cancelCourseButton;
    private TextView startDate;
    private TextView endDate;
    private DatePickerDialog startDatePicker;
    private DatePickerDialog endDatePicker;
    private ImageButton startDateAlert;
    private ImageButton endDateAlert;
    private EditText notesField;
    private ImageButton saveNotesButton;
    private EditText mentorName;
    private EditText mentorNumber;
    private EditText mentorEmail;

    private Repository repository;
    private Spinner termSpinner;
    private Spinner statusSpinner;
    private boolean userIsInteracting = false;
    private boolean isFirstTermSpinnerInteraction = true;
    private boolean isFirstStatusSpinnerInteraction = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        repository = new Repository(getApplication());

        initializeUIElements();

        int courseId = getIntent().getIntExtra("COURSE_ID", -1);
        int mode = getIntent().getIntExtra(MODE_KEY, MODE_VIEW);

        if (courseId != -1) {
            selectedCourse = repository.getAssociatedCourse(courseId);
        }

        if (mode == MODE_VIEW && selectedCourse != null) {
            buildActionBar();
            buildAlertActions();
            setViewMode(selectedCourse);
            setBellIcons();

        } else if (mode == MODE_ADD) {
            setAddMode();
        } else {
            setEditMode(selectedCourse);

        }
        initDatePickers();

    }

    /**
     * Initialize UI elements and set click listeners.
     */
    private void initializeUIElements() {
        course_text_input_layout = findViewById(R.id.course_text_input_layout);
        courseTitleEditText = findViewById(R.id.courseTitleTextview);
        course_text_input_layout = findViewById(R.id.course_text_input_layout);
        addCourseButton = findViewById(R.id.addBtn);
        cancelCourseButton = findViewById(R.id.cancelBtn);
        startDate = findViewById(R.id.courseStartDateLabel);
        endDate = findViewById(R.id.courseEndDateLabel);
        startDateAlert = findViewById(R.id.startDateAlert);
        endDateAlert = findViewById(R.id.endDateAlert);
        notesField = findViewById(R.id.notesField);
        saveNotesButton = findViewById(R.id.saveNotesButton);
        termSpinner = findViewById(R.id.term_spinner);
        statusSpinner = findViewById(R.id.status_spinner);
        mentorName = findViewById(R.id.mentorNameField);
        mentorNumber = findViewById(R.id.mentorPhoneField);
        mentorEmail = findViewById(R.id.mentorEMailField);
        // Initialize the Spinner
        List<Term> termList = repository.getAllTerms();
        Term defaultTerm = new Term();
        defaultTerm.setTermTitle("SELECT A TERM");
        termList.add(0, defaultTerm);
        ArrayList<Term> termArrayList = new ArrayList<>(termList);
        ArrayAdapter<Term> termAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, termArrayList);
        termAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        termSpinner.setAdapter(termAdapter);

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courseStatusArray);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusAdapter);



    }

    private void setTermSpinnerListener() {
        termSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                    new AlertDialog.Builder(CourseDetail.this)
                            .setTitle("Save Changes")
                            .setMessage("Do you want to save the changes?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Save the selected term
                                    Term selectedTerm = (Term) termSpinner.getSelectedItem();
                                    selectedCourse.setTermID_F(selectedTerm.getTermID());
                                    repository.update(selectedCourse);
                                    userIsInteracting = true;
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setStatusSpinnerListener() {

        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    new AlertDialog.Builder(CourseDetail.this)
                            .setTitle("Save Changes")
                            .setMessage("Do you want to save the changes?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Save the selected status
                                    String status = statusSpinner.getSelectedItem().toString();
                                    saveStatus(status);
                                    userIsInteracting = true;
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    /**
     * Check and set the bell icons based on the alarms' status.
     */
    private void setBellIcons() {
        setBellIcon(startDateAlert, "Start");
        setBellIcon(endDateAlert, "End");
    }

    /**
     * Set the bell icon based on the alarm's status.
     */
    private void setBellIcon(ImageButton button, String startOrEnd) {
        String alarmKeyEnabled = "Alarm_course_" + selectedCourse.getCourseID() + "_" + startOrEnd + "_date";
        boolean isAlarmOn = ReminderManager.isReminderSet(this, alarmKeyEnabled);

        if (isAlarmOn) {
            button.setImageResource(R.drawable.iconmonstr_bell_8);
        } else {
            button.setImageResource(R.drawable.iconmonstr_bell_12);
        }
    }


    private void buildAlertActions() {
        // Set the click listener for the start date bell icon
        startDateAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDateAlertClick(startDate, "Start", startDateAlert);
            }
        });

        // Set the click listener for the end date bell icon
        endDateAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDateAlertClick(endDate, "End", endDateAlert);
            }
        });
    }

    private void handleDateAlertClick(TextView dateLabel, String startOrEnd, ImageButton button) {

        String alarmKeyDate = "Alarm_course_" + selectedCourse.getCourseID() + "_" + startOrEnd + "_date";
        // Check if the alarm is already set for this date
        boolean isAlarmOn = ReminderManager.isReminderSet(this, alarmKeyDate);

        if (isAlarmOn) {
            // Cancel the alarm
            ReminderManager.cancelReminder(this, alarmKeyDate);
            button.setImageResource(R.drawable.iconmonstr_bell_12);
        } else {
            // Set the alarm
            String dateStr = dateLabel.getText().toString();
            ReminderManager.setReminder(this, alarmKeyDate, dateStr);
            button.setImageResource(R.drawable.iconmonstr_bell_8);
        }
    }


    private void setViewMode(Course course) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(course.getCourseTitle());
        }
        course_text_input_layout.setVisibility(View.GONE);
        courseTitleEditText.setText(course.getCourseTitle());
        startDate.setText(course.getCourseStart());
        endDate.setText(course.getCourseEnd());

        // Find the index of the selected term in the term list
        int selectedTermIndex = findTermIndex(course.getTermID_F());
        if (selectedTermIndex != -1) {
            termSpinner.setSelection(selectedTermIndex);
        }

        String status = loadStatus();
        int statusIndex = findStatusIndex(status);
        if (statusIndex != -1) {
            statusSpinner.setSelection(statusIndex);
        }


        startDate.setEnabled(false); // Disable editability
        endDate.setEnabled(false); // Disable editability

        addCourseButton.setVisibility(View.GONE);
        cancelCourseButton.setVisibility(View.GONE);

        saveNotesButton.setOnClickListener(v -> saveNotes());
        loadNotes();

        ImageButton shareNotesButton = findViewById(R.id.shareNotesButton);
        shareNotesButton.setOnClickListener(v -> shareNotes());
        setTermSpinnerListener();
        setStatusSpinnerListener();
    }


    private void setAddMode() {
        course_text_input_layout.setVisibility(View.VISIBLE);
        courseTitleEditText.setEnabled(true); // Enable editability
        startDate.setEnabled(true); // Enable editability
        endDate.setEnabled(true); // Enable editability

        addCourseButton.setVisibility(View.VISIBLE);
        cancelCourseButton.setVisibility(View.VISIBLE);

        saveNotesButton.setOnClickListener(null); // Remove click listener for saveNotesButton

        // Hide shareNotesButton in add mode
        ImageButton shareNotesButton = findViewById(R.id.shareNotesButton);
        shareNotesButton.setVisibility(View.INVISIBLE);
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
            Integer termID = termSpinner.getSelectedItem() != null ? ((Term) termSpinner.getSelectedItem()).getTermID() : null;
            String mentName = mentorName.getText().toString();
            String mentNumber = mentorNumber.getText().toString();
            String mentEmail = mentorEmail.getText().toString();
            String status = statusSpinner.getSelectedItem().toString();
            saveStatus(status);
            if (isAddMode()) {
                Course course = new Course(newTitle, newStartDate, newEndDate, termID, mentName, mentNumber, mentEmail);
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

    // Method to load saved notes from SharedPreferences
    private void loadNotes() {
        SharedPreferences sharedPreferences = getSharedPreferences("NOTES", MODE_PRIVATE);
        String savedNotes = sharedPreferences.getString(String.valueOf(selectedCourse.getCourseID()), "");

        // Display the saved notes in the EditText
        notesField.setText(savedNotes);
    }

    private String loadStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences("COURSE_PREFS", MODE_PRIVATE);
        return sharedPreferences.getString("status", "COURSE STATUS"); // Default value is "COURSE STATUS"
    }

    // Method to save notes to SharedPreferences
    private void saveNotes() {
        String notes = notesField.getText().toString();

        SharedPreferences sharedPreferences = getSharedPreferences("NOTES", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Save the notes with the course ID as the key
        editor.putString(String.valueOf(selectedCourse.getCourseID()), notes);
        editor.apply();

        // Provide feedback to the user
        Toast.makeText(this, "Notes saved", Toast.LENGTH_SHORT).show();
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

        // Find the index of the selected term in the term list
        int selectedTermIndex = findTermIndex(course.getTermID_f());
        if (selectedTermIndex != -1) {
            termSpinner.setSelection(selectedTermIndex);
        }

        String status = loadStatus();
        int statusIndex = findStatusIndex(status);
        if (statusIndex != -1) {
            statusSpinner.setSelection(statusIndex);
        }
        setAddMode();
    }

    private void saveStatus(String status) {
        SharedPreferences sharedPreferences = getSharedPreferences("COURSE_PREFS", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("status", status);
        editor.apply();
    }

    private int findTermIndex(int termID) {
        ArrayAdapter<Term> termAdapter = (ArrayAdapter<Term>) termSpinner.getAdapter();
        for (int i = 0; i < termAdapter.getCount(); i++) {
            if (termAdapter.getItem(i).getTermID() == termID) {
                return i;
            }
        }
        return -1; // Term not found
    }

    private int findStatusIndex(String status) {
        for (int i = 0; i < courseStatusArray.length; i++) {
            if (courseStatusArray[i].equals(status)) {
                return i;
            }
        }
        return -1; // Status not found
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

    /**
     * Build and customize the ActionBar.
     */
    private void buildActionBar() {
        // Customize the elements of the ActionBar
        ImageView backButton = findViewById(R.id.back_button);
        TextView titleView = findViewById(R.id.actionbar_title);
        ImageView menuIcon = findViewById(R.id.menu_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle menu icon click (e.g., show a popup menu with options)
            }
        });

        // Set the title dynamically
        titleView.setText("Your Title");
    }

    private void shareNotes() {
        String notes = notesField.getText().toString();

        // Create an Intent to send text
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, notes);

        // Start an activity to show a list of available sharing apps
        startActivity(Intent.createChooser(shareIntent, "Share Notes"));
    }

}

