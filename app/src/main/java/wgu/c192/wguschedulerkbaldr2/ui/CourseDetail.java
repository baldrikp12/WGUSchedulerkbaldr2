package wgu.c192.wguschedulerkbaldr2.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import wgu.c192.wguschedulerkbaldr2.R;
import wgu.c192.wguschedulerkbaldr2.database.Repository;
import wgu.c192.wguschedulerkbaldr2.entities.Assessment;
import wgu.c192.wguschedulerkbaldr2.entities.Course;
import wgu.c192.wguschedulerkbaldr2.entities.Term;
import wgu.c192.wguschedulerkbaldr2.util.ReminderManager;
@SuppressWarnings("unchecked")
public class CourseDetail extends AppCompatActivity {
    
    public static final String MODE_KEY = "mode";
    public static final int MODE_VIEW = 0;
    public static final int MODE_ADD = 1;
    public static final int MODE_EDIT = 2;
    private static final String[] courseStatusArray = {"COURSE STATUS", "in progress", "completed", "dropped", "plan to take"};
    
    private Course selectedCourse = null;
    private TextInputLayout course_text_input_layout;
    private EditText courseTitleEditText;
    private Button addEditCourseButton;
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
    private int previousTermSpinnerSelection = -1;
    private int previousStatusSpinnerSelection = -1;
    int mode;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        
        repository = new Repository(getApplication());
        
        initializeUIElements();
        
        int courseId = getIntent().getIntExtra("COURSE_ID", -1);
        mode = getIntent().getIntExtra(MODE_KEY, MODE_VIEW);
        
        if (courseId != -1) {
            selectedCourse = repository.getAssociatedCourse(courseId);
        }
        
        if (mode == MODE_VIEW && selectedCourse != null) {
            setViewMode(selectedCourse);
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
        addEditCourseButton = findViewById(R.id.addEditBtn);
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
        mentorName.setEnabled(false);
        mentorNumber = findViewById(R.id.mentorPhoneField);
        mentorNumber.setEnabled(false);
        mentorEmail = findViewById(R.id.mentorEMailField);
        mentorEmail.setEnabled(false);
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
    
    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        userIsInteracting = true;
    }
    
    private void setTermSpinnerListener() {
        termSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                
                if (userIsInteracting) {
                    new AlertDialog.Builder(CourseDetail.this).setTitle("Save Changes").setMessage("Do you want to save the changes?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Save the selected term
                            Term selectedTerm = (Term) termSpinner.getSelectedItem();
                            selectedCourse.setTermID_F(selectedTerm.getTermID());
                            repository.update(selectedCourse);
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            termSpinner.setSelection(previousTermSpinnerSelection);
                            userIsInteracting = false;
                        }
                    }).show();
                }
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
                if (userIsInteracting) {
                    
                    new AlertDialog.Builder(CourseDetail.this).setTitle("Save Changes").setMessage("Do you want to save the changes?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Save the selected status
                            String status = statusSpinner.getSelectedItem().toString();
                            saveStatus(status);
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            statusSpinner.setSelection(previousStatusSpinnerSelection);
                            userIsInteracting = false;
                        }
                    }).show();
                    
                }
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
        buildActionBar();
        buildAlertActions();
        // Find the index of the selected term in the term list
        int selectedTermIndex = findTermIndex(course.getTermID_F());
        if (selectedTermIndex != -1) {
            termSpinner.setSelection(selectedTermIndex);
            previousTermSpinnerSelection = termSpinner.getSelectedItemPosition();
        }
        
        String status = loadStatus();
        int statusIndex = findStatusIndex(status);
        if (statusIndex != -1) {
            statusSpinner.setSelection(statusIndex);
            previousStatusSpinnerSelection = statusSpinner.getSelectedItemPosition();
        }
        
        course_text_input_layout.setVisibility(View.GONE);
        courseTitleEditText.setText(course.getCourseTitle());
        startDate.setText(course.getCourseStart());
        endDate.setText(course.getCourseEnd());
        mentorName.setText(selectedCourse.getMentName());
        mentorNumber.setText(selectedCourse.getMentNumber());
        mentorEmail.setText(selectedCourse.getMentEmail());
        
        startDate.setEnabled(false); // Disable editability
        endDate.setEnabled(false); // Disable editability
        
        addEditCourseButton.setVisibility(View.GONE);
        cancelCourseButton.setVisibility(View.GONE);
        
        
        saveNotesButton.setOnClickListener(v -> saveNotes());
        loadNotes();
        
        ImageButton shareNotesButton = findViewById(R.id.shareNotesButton);
        shareNotesButton.setOnClickListener(v -> shareNotes());
        shareNotesButton.setVisibility(View.VISIBLE);
        setTermSpinnerListener();
        setStatusSpinnerListener();
        setBellIcons();
    }
    
    
    private void setAddMode() {
        buildActionBar();
        course_text_input_layout.setVisibility(View.VISIBLE);
        courseTitleEditText.setEnabled(true); // Enable editability
        startDate.setEnabled(true); // Enable editability
        endDate.setEnabled(true); // Enable editability
        mentorName.setEnabled(true);
        mentorNumber.setEnabled(true);
        mentorEmail.setEnabled(true);
        addEditCourseButton.setText("Add");
        addEditCourseButton.setVisibility(View.VISIBLE);
        buildCancelButton();
        
        
        saveNotesButton.setOnClickListener(null); // Remove click listener for saveNotesButton
        
        // Hide shareNotesButton in add mode
        ImageButton shareNotesButton = findViewById(R.id.shareNotesButton);
        shareNotesButton.setVisibility(View.INVISIBLE);
    }
    
    private void buildCancelButton() {
        cancelCourseButton.setVisibility(View.VISIBLE);
        cancelCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditMode()) {
                    mode = MODE_VIEW;
                    setViewMode(selectedCourse);
                } else if (isAddMode()) {
                    onBackPressed();
                }
            }
        });
        
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
            String newStartDate = startDate.getText().toString().isEmpty() ? null : startDate.getText().toString().trim();
            String newEndDate = endDate.getText().toString().isEmpty() ? null : endDate.getText().toString().trim();
            int termID = termSpinner.getSelectedItem() != null ? ((Term) termSpinner.getSelectedItem()).getTermID() : 0;
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++   " + termID);
            String mentName = mentorName.getText().toString().isEmpty() ? null : mentorName.getText().toString().trim();
            String mentNumber = mentorNumber.getText().toString().isEmpty() ? null : mentorNumber.getText().toString().trim();
            String mentEmail = mentorEmail.getText().toString().isEmpty() ? null : mentorEmail.getText().toString().trim();
            
            String status = statusSpinner.getSelectedItem().toString();
            saveStatus(status);
            
            if (isAddMode()) {
                Course course = new Course(newTitle, newStartDate, newEndDate, mentName, mentNumber, mentEmail);
                repository.insert(course);
            }
            if (isEditMode()) {
                selectedCourse.setCourseTitle(newTitle);
                selectedCourse.setCourseStart(newStartDate);
                selectedCourse.setCourseEnd(newEndDate);
                selectedCourse.setMentName(mentName);
                selectedCourse.setMentNumber(mentNumber);
                selectedCourse.setMentEmail(mentEmail);
                
                repository.update(selectedCourse);
            }
            
            Intent viewIntent = new Intent(CourseDetail.this, CourseList.class);
            viewIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(viewIntent);
            
            
            finish();
        } else {
            toastAlert("Title cannot be blank");
        }
    }
    
    private void toastAlert(String theMessage) {
        Toast toast = Toast.makeText(this, theMessage, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
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
        toastAlert("Notes saved");
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
        System.out.println("                        seteditmode 1 " + isEditMode());
        buildActionBar();
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
        course_text_input_layout.setVisibility(View.VISIBLE);
        courseTitleEditText.setEnabled(true);
        startDate.setEnabled(true);
        endDate.setEnabled(true);
        mentorName.setEnabled(true);
        mentorNumber.setEnabled(true);
        mentorEmail.setEnabled(true);
        addEditCourseButton.setText("Update");
        addEditCourseButton.setVisibility(View.VISIBLE);
        buildCancelButton();
        System.out.println("                        seteditmode 2 " + isEditMode());
        saveNotesButton.setOnClickListener(null); // Remove click listener for saveNotesButton
        
        // Hide shareNotesButton in edit  mode
        ImageButton shareNotesButton = findViewById(R.id.shareNotesButton);
        shareNotesButton.setVisibility(View.INVISIBLE);
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
                PopupMenu popup = new PopupMenu(CourseDetail.this, v);
                popup.getMenuInflater().inflate(R.menu.menu_main, popup.getMenu());
                MenuItem editMenuItem = popup.getMenu().findItem(R.id.action_item);
                if (isEditMode()) {
                    editMenuItem.setTitle("Delete");
                } else if (isAddMode()) {
                    editMenuItem.setTitle("Cancel");
                } else {
                    editMenuItem.setTitle("Edit");
                }
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        
                        if (isEditMode()) { // delete
                            // Check if the course has associated assessments
                            if (courseHasAssessments(selectedCourse)) {
                                // Alert the user about existing assessments
                                showAlertForExistingAssessments();
                            } else {
                                deleteCourse(selectedCourse);
                                Intent intent = new Intent(CourseDetail.this, CourseList.class);
                                startActivity(intent);
                            }
                        } else if (isAddMode()) { // Cancel
                            onBackPressed();
                        } else { // Edit
                            getIntent().putExtra(MODE_KEY, MODE_EDIT);
                            setEditMode(selectedCourse);
                            return true;
                        }
                        return false;
                    }
                });
                
                popup.show();
            }
        });
        if (isAddMode()) {
            titleView.setText("New Course");
        } else {
            // Set the title dynamically
            titleView.setText(selectedCourse.getCourseTitle());
        }
    }
    
    // Check if the course has associated assessments (pseudocode)
    private boolean courseHasAssessments(Course course) {
        List<Assessment> assessments = repository.getAssessmentsByCourseID(course.getCourseID());
        return !assessments.isEmpty();
    }
    
    // Show an alert for existing assessments
    private void showAlertForExistingAssessments() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This course has existing assessments. Delete anyway?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User confirmed to delete the course with assessments
                        deleteCourse(selectedCourse);
                        toastAlert("Notes Saved");
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User canceled the deletion
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }
    
    // Delete the course (pseudocode)
    private void deleteCourse(Course course) {
        repository.delete(course);
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

