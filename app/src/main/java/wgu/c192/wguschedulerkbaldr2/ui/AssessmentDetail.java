package wgu.c192.wguschedulerkbaldr2.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Switch;
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
import wgu.c192.wguschedulerkbaldr2.util.ReminderManager;

public class AssessmentDetail extends AppCompatActivity {
    public static final String MODE_KEY = "mode";
    private static final int MODE_VIEW = 0;
    private static final int MODE_ADD = 1;
    private static final int MODE_EDIT = 2;
    
    private Assessment selectedAssessment = null;
    private TextInputLayout assessment_text_input_layout;
    private EditText assessmentTitleEditText;
    private Spinner courseSpinner;
    private Switch assessmentType;
    private Button addEditAssessmentButton;
    private Button cancelAssessmentButton;
    private TextView startDate;
    private TextView endDate;
    private ImageButton startDateAlert;
    private ImageButton endDateAlert;
    private DatePickerDialog startDatePicker;
    private DatePickerDialog endDatePicker;
    private int previousCourseSpinnerSelection = -1;
    private boolean userIsInteracting = false;
    private Repository repository = new Repository(getApplication());
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail);
        
        int assessmentId = getIntent().getIntExtra("ASSESSMENT_ID", -1);
        int mode = getIntent().getIntExtra(MODE_KEY, MODE_VIEW);
        
        initializeUIElements();
        
        initializeAssessmentData(assessmentId, mode);
        buildActionBar();
        initDatePickers();
    }
    
    private void initializeUIElements() {
        this.assessment_text_input_layout = findViewById(R.id.assessment_text_input_layout);
        this.assessmentTitleEditText = findViewById(R.id.assessmentTitleTextview);
        this.courseSpinner = findViewById(R.id.courseSpinnner);
        this.assessmentType = findViewById(R.id.typeSwitch);
        this.startDate = findViewById(R.id.assessmentStartDateLabel);
        this.endDate = findViewById(R.id.assessmentEndDateLabel);
        this.startDateAlert = findViewById(R.id.startDateAlert);
        this.endDateAlert = findViewById(R.id.endDateAlert);
        this.addEditAssessmentButton = findViewById(R.id.addEditBtn);
        this.cancelAssessmentButton = findViewById(R.id.cancelBtn);
        
        List<Course> courseList = repository.getAllCourses();
        Course defaultCourse = new Course();
        defaultCourse.setCourseTitle("Select Course");
        courseList.add(0, defaultCourse);
        ArrayList<Course> courseArrayList = new ArrayList<>(courseList);
        ArrayAdapter<Course> termAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courseArrayList);
        termAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(termAdapter);
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
                PopupMenu popup = new PopupMenu(AssessmentDetail.this, v);
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
                            
                            deleteAssessment(selectedAssessment);
                            Intent intent = new Intent(AssessmentDetail.this, AssessmentList.class);
                            startActivity(intent);
                            showToast("Assessment has been deleted");
                            
                        } else if (isAddMode()) { // Cancel
                            onBackPressed();
                        } else { // Edit
                            getIntent().putExtra(MODE_KEY, MODE_EDIT);
                            setEditMode(selectedAssessment);
                            return true;
                        }
                        return false;
                    }
                });
                
                popup.show();
            }
        });
        if (isAddMode()) {
            titleView.setText("New Assessment");
        } else {
            // Set the title dynamically
            titleView.setText(selectedAssessment.getAssessmentTitle());
        }
    }
    
    private void deleteAssessment(Assessment assessment) {
        repository.delete(assessment);
    }
    
    private void initializeAssessmentData(int assessmentId, int mode) {
        Repository repository = new Repository(getApplication());
        
        if (assessmentId != -1) {
            selectedAssessment = repository.getAssociatedAssessment(assessmentId);
        }
        
        if (mode == MODE_VIEW && selectedAssessment != null) {
            setViewMode(selectedAssessment);
        } else if (mode == MODE_ADD) {
            setAddMode();
        } else {
            setEditMode(selectedAssessment);
        }
    }
    
    private void setViewMode(Assessment assessment) {
        assessmentTitleEditText.setText(assessment.getAssessmentTitle());
        buildAlertActions();
        
        // Find the index of the selected term in the term list
        int selectedCourseIndex = findCourseIndex(assessment.getCourseID_f());
        if (selectedCourseIndex != -1) {
            courseSpinner.setSelection(selectedCourseIndex);
            previousCourseSpinnerSelection = courseSpinner.getSelectedItemPosition();
        }
        assessmentType.setChecked(selectedAssessment.getAssessmentType());
        startDate.setText(assessment.getStartDate());
        endDate.setText(assessment.getEndDate());
        
        setCourseSpinnerListener();
        setUIReadOnly();
        
        setBellIcons();
    }
    
    private void setAddMode() {
        setUIEditable();
        addEditAssessmentButton.setText("Add");
    }
    
    private void setEditMode(Assessment assessment) {
        
        assessmentTitleEditText.setText(assessment.getAssessmentTitle());
        
        int selectedTermIndex = findCourseIndex(assessment.getCourseID_f());
        if (selectedTermIndex != -1) {
            courseSpinner.setSelection(selectedTermIndex);
        }
        assessmentType.setChecked(selectedAssessment.getAssessmentType());
        startDate.setText(assessment.getStartDate());
        endDate.setText(assessment.getEndDate());
        addEditAssessmentButton.setText("Update");
        setUIEditable();
        
    }
    
    private void setUIReadOnly() {
        assessment_text_input_layout.setVisibility(View.GONE);
        startDate.setEnabled(false);
        endDate.setEnabled(false);
        addEditAssessmentButton.setVisibility(View.GONE);
        cancelAssessmentButton.setVisibility(View.GONE);
    }
    
    private void setUIEditable() {
        assessment_text_input_layout.setVisibility(View.VISIBLE);
        startDate.setEnabled(true);
        endDate.setEnabled(true);
        addEditAssessmentButton.setVisibility(View.VISIBLE);
        buildCancelButton();
    }
    

    private void setBellIcons() {
        setBellIcon(startDateAlert, "Start");
        setBellIcon(endDateAlert, "End");
    }
    
    private void setBellIcon(ImageButton button, String startOrEnd) {
        String alarmKeyEnabled = "Alarm_assessment_" + selectedAssessment.getAssessmentID() + "_" + startOrEnd + "_date";
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
        
        String alarmKeyDate = "Alarm_assessment_" + selectedAssessment.getAssessmentID() + "_" + startOrEnd + "_date";
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
    
    private void initDatePickers() {
        startDatePicker = createDatePickerDialog(startDate);
        endDatePicker = createDatePickerDialog(endDate);
    }
    
    private DatePickerDialog createDatePickerDialog(final TextView dateLabel) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(selectedYear, selectedMonth, selectedDay);
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
            String formattedDate = sdf.format(selectedDate.getTime());
            dateLabel.setText(formattedDate);
        }, year, month, day);
        
        return datePickerDialog;
    }
    
    public void addOrEditAssessment(View view) {
        if (!assessmentTitleEditText.getText().toString().trim().isEmpty()) {
            String newTitle = assessmentTitleEditText.getText().toString().trim();
            String newStartDate = startDate.getText().toString().isEmpty() ? "" : startDate.getText().toString().trim();
            String newEndDate = endDate.getText().toString().isEmpty() ? "" : endDate.getText().toString().trim();
            boolean type = assessmentType.isChecked();
            int courseID = courseSpinner.getSelectedItem() != null ? ((Course) courseSpinner.getSelectedItem()).getCourseID() : 0;
            
            
            Repository repository = new Repository(getApplication());
            
            if (isAddMode()) {
                Assessment assessment = new Assessment(newTitle, newStartDate, newEndDate, type, courseID);
                
                repository.insert(assessment);
            }
            
            if (isEditMode()) {
                selectedAssessment.setAssessmentTitle(newTitle);
                selectedAssessment.setStartDate(newStartDate);
                selectedAssessment.setEndDate(newEndDate);
                selectedAssessment.setAssessmentType(type);
                
                repository.update(selectedAssessment);
            }
            
            navigateToAssessmentsList();
        } else {
            showToast("Title cannot be blank");
        }
    }
    
    private void navigateToAssessmentsList() {
        Intent viewIntent = new Intent(AssessmentDetail.this, AssessmentList.class);
        viewIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(viewIntent);
        finish();
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
    
    private boolean isAddMode() {
        int mode = getIntent().getIntExtra(MODE_KEY, MODE_VIEW);
        return mode == MODE_ADD;
    }
    
    private void buildCancelButton() {
        cancelAssessmentButton.setVisibility(View.VISIBLE);
        cancelAssessmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditMode()) {
                    setViewMode(selectedAssessment);
                } else if (isAddMode()) {
                    onBackPressed();
                }
            }
        });
        
    }
    
    private int findCourseIndex(int courseID) {
        ArrayAdapter<Course> courseAdapter = (ArrayAdapter<Course>) courseSpinner.getAdapter();
        for (int i = 0; i < courseAdapter.getCount(); i++) {
            if (courseAdapter.getItem(i).getCourseID() == courseID) {
                return i;
            }
        }
        return -1; // Term not found
    }
    
    private void setCourseSpinnerListener() {
        courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                
                if (userIsInteracting) {
                    new AlertDialog.Builder(AssessmentDetail.this).setTitle("Save Changes").setMessage("Do you want to save the changes?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Save the selected term
                            Course selectedCourse = (Course) courseSpinner.getSelectedItem();
                            selectedAssessment.setCourseID_f(selectedCourse.getCourseID());
                            repository.update(selectedAssessment);
                            
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            courseSpinner.setSelection(previousCourseSpinnerSelection);
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
    
    private void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }
    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        userIsInteracting = true;
    }
}