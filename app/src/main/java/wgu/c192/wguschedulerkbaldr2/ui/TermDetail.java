package wgu.c192.wguschedulerkbaldr2.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import wgu.c192.wguschedulerkbaldr2.R;
import wgu.c192.wguschedulerkbaldr2.database.Repository;
import wgu.c192.wguschedulerkbaldr2.entities.Term;

public class TermDetail extends AppCompatActivity {

    public static final String MODE_KEY = "mode";
    public static final int MODE_VIEW = 0;
    public static final int MODE_ADD = 1;

    private TextInputLayout term_text_input_layout;
    private EditText termTitleEditText;
    private Button addTermButton;
    private Button cancelTermButton;
    private TextView startDate;
    private TextView endDate;

    private DatePickerDialog startDatePicker;
    private DatePickerDialog endDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);

        int termId = getIntent().getIntExtra("TERM_ID", -1);
        int mode = getIntent().getIntExtra(MODE_KEY, MODE_VIEW);

        term_text_input_layout = findViewById(R.id.term_text_input_layout);
        termTitleEditText = findViewById(R.id.termTitleTextview);
        addTermButton = findViewById(R.id.addBtn);
        cancelTermButton = findViewById(R.id.cancelBtn);
        startDate = findViewById(R.id.termStartDateLabel);
        endDate = findViewById(R.id.termEndDateLabel);

        // Initialize selectedTerm as null
        Term selectedTerm = null;

        if (termId != -1) {
            // Retrieve the term data if the termId is valid
            Repository repository = new Repository(getApplication());
            selectedTerm = repository.getAssociatedTerm(termId);
        }

        // Set the appropriate mode
        if (mode == MODE_VIEW && selectedTerm != null) {
            setViewMode(selectedTerm);
        } else if (mode == MODE_ADD) {
            setAddMode();
        }

        // Initialize DatePickerDialogs
        initDatePickers();
    }

    private void setViewMode(Term term) {
        termTitleEditText.setText(term.getTermTitle());
        startDate.setText(term.getStartDate().toString());
        endDate.setText(term.getEndDate().toString());

        // Find the CourseListFragment and pass termID as an argument
        FragmentManager fragmentManager = getSupportFragmentManager();
        CourseListFragment fragment = (CourseListFragment) fragmentManager.findFragmentById(R.id.termfragmentcontainter);
        Bundle args = new Bundle();
        args.putInt("termID", term.getTermID());
        fragment.setArguments(args);

        termTitleEditText.setFocusable(false);
        //startDate.setFocusable(false);
        //endDate.setFocusable(false);
        addTermButton.setVisibility(View.INVISIBLE);
        cancelTermButton.setVisibility(View.INVISIBLE);
    }

    private void setAddMode() {
        termTitleEditText.setFocusable(true);
        startDate.setFocusable(true);
        endDate.setFocusable(true);
        addTermButton.setVisibility(View.VISIBLE);
        cancelTermButton.setVisibility(View.VISIBLE);
    }

    // Initialize DatePickerDialogs
    private void initDatePickers() {
        // Initialize the start date picker dialog
        startDatePicker = createDatePickerDialog(startDate);

        // Initialize the end date picker dialog
        endDatePicker = createDatePickerDialog(endDate);
    }

    // Create a DatePickerDialog and set its OnDateSetListener
    private DatePickerDialog createDatePickerDialog(final TextView dateLabel) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                // Set the selected date to the corresponding label's text in month-day-year format
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(selectedYear, selectedMonth, selectedDay);
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
                String formattedDate = sdf.format(selectedDate.getTime());
                dateLabel.setText(formattedDate);
            }
        }, year, month, day);

        return datePickerDialog;
    }

    public void addTerm(View view) {
        EditText titleText = findViewById(R.id.termTitleTextview);
        String title = titleText.getText().toString().trim();
        if (!title.isEmpty()) {
            Calendar calendar = Calendar.getInstance();
            Date dateS = calendar.getTime();

            calendar.set(Calendar.YEAR, 2023);
            calendar.set(Calendar.MONTH, Calendar.AUGUST);
            calendar.set(Calendar.DAY_OF_MONTH, 6);
            Date dateE = calendar.getTime();

            Term term = new Term(title, startDate.getText().toString(), endDate.getText().toString());
            Repository repository = new Repository(getApplication());
            repository.insert(term);

            // After adding the term, switch to viewing mode by starting a new instance of the activity
            Intent viewIntent = new Intent(TermDetail.this, TermsList.class);
            viewIntent.putExtra(MODE_KEY, MODE_VIEW);
            viewIntent.putExtra("TERM_ID", term.getTermID());

            viewIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(viewIntent);

            // Finish the current activity (optional, depending on your workflow)
            finish();
        } else {
            // Display a message to the user indicating that the title is required.
            Toast toast = Toast.makeText(this, "Title cannot be blank", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0); // Set gravity to the top
            toast.show();
        }
    }

    public void onStartDateLabelClick(View view) {
        if (isEditMode()) {
            // Show the start date picker dialog
            startDatePicker.show();
        }
    }

    public void onEndDateLabelClick(View view) {
        if (isEditMode()) {
            // Show the end date picker dialog
            endDatePicker.show();
        }
    }

    private boolean isEditMode() {
        // Check if the activity is in edit or add mode, return true if so
        int mode = getIntent().getIntExtra(MODE_KEY, MODE_VIEW);
        return mode == MODE_ADD;
    }
}
