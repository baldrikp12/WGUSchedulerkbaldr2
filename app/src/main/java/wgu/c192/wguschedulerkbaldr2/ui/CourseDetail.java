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
    private Button show_popup_button;
    private EditText mentorName;
    private EditText mentorNumber;
    private EditText mentorEmail;
    private final Repository repository = new Repository(getApplication());
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
        
        int courseId = getIntent().getIntExtra("COURSE_ID", -1);
        mode = getIntent().getIntExtra(MODE_KEY, MODE_VIEW);
        
        initializeUIElements();
        
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
 
    private void initializeUIElements() {
        this.course_text_input_layout = findViewById(R.id.course_text_input_layout);
        this.courseTitleEditText = findViewById(R.id.courseTitleTextview);
        this.course_text_input_layout = findViewById(R.id.course_text_input_layout);
        this.addEditCourseButton = findViewById(R.id.addEditBtn);
        this.cancelCourseButton = findViewById(R.id.cancelBtn);
        this.startDate = findViewById(R.id.courseStartDateLabel);
        this.endDate = findViewById(R.id.courseEndDateLabel);
        this.startDateAlert = findViewById(R.id.startDateAlert);
        this.endDateAlert = findViewById(R.id.endDateAlert);
        this.notesField = findViewById(R.id.notesField);
        this.saveNotesButton = findViewById(R.id.saveNotesButton);
        this.show_popup_button = findViewById(R.id.assessmentPopupButton);
        this.termSpinner = findViewById(R.id.term_spinner);
        this.statusSpinner = findViewById(R.id.status_spinner);
        this.mentorName = findViewById(R.id.mentorNameField);
        this.mentorNumber = findViewById(R.id.mentorPhoneField);
        this.mentorEmail = findViewById(R.id.mentorEMailField);
     
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
    
    private void setViewMode(Course course) {
        buildActionBar();
        buildAlertActions();
     
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
        mentorName.setEnabled(false);
        mentorNumber.setEnabled(false);
        mentorEmail.setEnabled(false);
        startDate.setEnabled(false);
        endDate.setEnabled(false);
        
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
    
    private void setEditMode(Course course) {
        buildActionBar();
        courseTitleEditText.setText(course.getCourseTitle());
        startDate.setText(course.getCourseStart());
        endDate.setText(course.getCourseEnd());
        
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
        saveNotesButton.setOnClickListener(null);
        
        ImageButton shareNotesButton = findViewById(R.id.shareNotesButton);
        shareNotesButton.setVisibility(View.INVISIBLE);
    }
    
    private void setAddMode() {
        buildActionBar();
        course_text_input_layout.setVisibility(View.VISIBLE);
        courseTitleEditText.setEnabled(true);
        startDate.setEnabled(true);
        endDate.setEnabled(true);
        mentorName.setEnabled(true);
        mentorNumber.setEnabled(true);
        mentorEmail.setEnabled(true);
        addEditCourseButton.setText("Add");
        addEditCourseButton.setVisibility(View.VISIBLE);
        buildCancelButton();
        
        
        saveNotesButton.setOnClickListener(null);
        
        ImageButton shareNotesButton = findViewById(R.id.shareNotesButton);
        shareNotesButton.setVisibility(View.INVISIBLE);
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
        
        startDateAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDateAlertClick(startDate, "Start", startDateAlert);
            }
        });
        
        
        endDateAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDateAlertClick(endDate, "End", endDateAlert);
            }
        });
    }
    
    private void handleDateAlertClick(TextView dateLabel, String startOrEnd, ImageButton button) {
        
        String alarmKeyDate = "Alarm_course_" + selectedCourse.getCourseID() + "_" + startOrEnd + "_date";

        boolean isAlarmOn = ReminderManager.isReminderSet(this, alarmKeyDate);
        
        if (isAlarmOn) {
          
            
            ReminderManager.cancelReminder(this, alarmKeyDate);
            button.setImageResource(R.drawable.iconmonstr_bell_12);
        } else {
        
            String dateStr = dateLabel.getText().toString();
            ReminderManager.setReminder(this, alarmKeyDate, dateStr);
            button.setImageResource(R.drawable.iconmonstr_bell_8);
        }
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
            String newStartDate = startDate.getText().toString().isEmpty() ? "" : startDate.getText().toString().trim();
            String newEndDate = endDate.getText().toString().isEmpty() ? "" : endDate.getText().toString().trim();
            int termID = termSpinner.getSelectedItem() != null ? ((Term) termSpinner.getSelectedItem()).getTermID() : 0;
            String mentName = mentorName.getText().toString().isEmpty() ? "" : mentorName.getText().toString().trim();
            String mentNumber = mentorNumber.getText().toString().isEmpty() ? "" : mentorNumber.getText().toString().trim();
            String mentEmail = mentorEmail.getText().toString().isEmpty() ? "" : mentorEmail.getText().toString().trim();
            
            String status = statusSpinner.getSelectedItem().toString();
            saveStatus(status);
            
            if (isAddMode()) {
                Course course = new Course(newTitle, newStartDate, newEndDate, mentName, mentNumber, mentEmail, termID);
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
            
            navigateToCourseList();
        } else {
            showToast("Title cannot be blank");
        }
    }
    
    private void navigateToCourseList() {
        Intent viewIntent = new Intent(CourseDetail.this, CourseList.class);
        viewIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(viewIntent);
        finish();
    }
    
    private void showToast(String theMessage) {
        Toast toast = Toast.makeText(this, theMessage, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }
    
    private void loadNotes() {
        SharedPreferences sharedPreferences = getSharedPreferences("NOTES", MODE_PRIVATE);
        String savedNotes = sharedPreferences.getString(String.valueOf(selectedCourse.getCourseID()), "");
        
        notesField.setText(savedNotes);
    }
    
    private String loadStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences("COURSE_PREFS", MODE_PRIVATE);
        return sharedPreferences.getString("status", "COURSE STATUS");
    }
    
    private void saveNotes() {
        String notes = notesField.getText().toString();
        
        SharedPreferences sharedPreferences = getSharedPreferences("NOTES", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        
        editor.putString(String.valueOf(selectedCourse.getCourseID()), notes);
        editor.apply();
        
        showToast("Notes saved");
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
        return -1;
    }
    
    private int findStatusIndex(String status) {
        for (int i = 0; i < courseStatusArray.length; i++) {
            if (courseStatusArray[i].equals(status)) {
                return i;
            }
        }
        return -1;
    }
    
    
    private boolean isAddMode() {
        int mode = getIntent().getIntExtra(MODE_KEY, MODE_VIEW);
        
        return mode == MODE_ADD;
    }
    
    private void buildActionBar() {
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
                            if (courseHasAssessments(selectedCourse)) {
                                showAlertForExistingAssessments();
                            } else {
                                deleteCourse(selectedCourse);
                                Intent intent = new Intent(CourseDetail.this, CourseList.class);
                                startActivity(intent);
                                showToast("Course has been deleted.");
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
            titleView.setText(selectedCourse.getCourseTitle());
        }
    }
    
    private boolean courseHasAssessments(Course course) {
        List<Assessment> assessments = repository.getAssessmentsByCourseID(course.getCourseID());
        return !assessments.isEmpty();
    }
 
    private void showAlertForExistingAssessments() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This course has existing assessments. Please remove assessments first.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                   
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }
    
    private void deleteCourse(Course course) {
        repository.delete(course);
    }
    
    private void shareNotes() {
        String notes = notesField.getText().toString();
        
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, notes);
        
        startActivity(Intent.createChooser(shareIntent, "Share Notes"));
    }
    
    public void OnShowPopupButtonClick(View view) {
        if (!isAddMode() && !isEditMode()) {
            MyPopupFragment popupFragment = new MyPopupFragment(selectedCourse.getCourseID());
            popupFragment.show(getSupportFragmentManager(), "MyPopupFragment");
        }
    }
    
}

