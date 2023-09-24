package wgu.c192.wguschedulerkbaldr2.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import wgu.c192.wguschedulerkbaldr2.R;
import wgu.c192.wguschedulerkbaldr2.database.Repository;
import wgu.c192.wguschedulerkbaldr2.entities.Course;
import wgu.c192.wguschedulerkbaldr2.entities.Term;
import wgu.c192.wguschedulerkbaldr2.util.ReminderManager;

public class TermDetail extends AppCompatActivity {
    
    public static final String MODE_KEY = "mode";
    public static final int MODE_VIEW = 0;
    public static final int MODE_ADD = 1;
    public static final int MODE_EDIT = 2;
    
    private Term selectedTerm = null;
    private TextInputLayout term_text_input_layout;
    private EditText termTitleEditText;
    private Button addEditTermButton;
    private Button cancelTermButton;
    private TextView startDate;
    private TextView endDate;
    private ImageButton startDateAlert;
    private ImageButton endDateAlert;
    private DatePickerDialog startDatePicker;
    private DatePickerDialog endDatePicker;
    private Repository repository = new Repository(getApplication());
    int mode;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);
        
        int termId = getIntent().getIntExtra("TERM_ID", -1);
        int mode = getIntent().getIntExtra(MODE_KEY, MODE_VIEW);
        
        initializeUIElements();
        
        initializeTermData(termId, mode);
        buildActionBar();
        initDatePickers();
    }
    
    private void initializeUIElements() {
        term_text_input_layout = findViewById(R.id.term_text_input_layout);
        termTitleEditText = findViewById(R.id.termTitleTextview);
        
        startDate = findViewById(R.id.termStartDateLabel);
        endDate = findViewById(R.id.termEndDateLabel);
        startDateAlert = findViewById(R.id.startDateAlert);
        endDateAlert = findViewById(R.id.endDateAlert);
        addEditTermButton = findViewById(R.id.addEditBtn);
        cancelTermButton = findViewById(R.id.cancelBtn);
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
                PopupMenu popup = new PopupMenu(TermDetail.this, v);
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
                            // Check if the term has associated courses
                            if (termHasCourses(selectedTerm)) {
                                // Alert the user about existing courses
                                showAlertForExistingCourses();
                            } else {
                                deleteTerm(selectedTerm);
                                Intent intent = new Intent(TermDetail.this, TermsList.class);
                                startActivity(intent);
                                showToast("Term has been deleted");
                            }
                        } else if (isAddMode()) { // Cancel
                            onBackPressed();
                        } else { // Edit
                            getIntent().putExtra(MODE_KEY, MODE_EDIT);
                            setEditMode(selectedTerm);
                            return true;
                        }
                        return false;
                    }
                });
                
                popup.show();
            }
        });
        if (isAddMode()) {
            titleView.setText("New Term");
        } else {
            // Set the title dynamically
            titleView.setText(selectedTerm.getTermTitle());
        }
    }
    
    private void showAlertForExistingCourses() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This Term has existing courses. Please remove courses first.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User acknowledged the alert, simply close the dialog
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }
    
    private boolean termHasCourses(Term selectedTerm) {
        List<Course> courses = repository.getCoursesByTermID(selectedTerm.getTermID());
        return !courses.isEmpty();
    }
    
    private void deleteTerm(Term term) {
        repository.delete(term);
    }
    
    private void initializeTermData(int termId, int mode) {
        Repository repository = new Repository(getApplication());
        
        if (termId != -1) {
            selectedTerm = repository.getAssociatedTerm(termId);
        }
        
        if (mode == MODE_VIEW && selectedTerm != null) {
            setViewMode(selectedTerm);
        } else if (mode == MODE_ADD) {
            setAddMode();
        } else {
            setEditMode(selectedTerm);
        }
    }
    
    private void setViewMode(Term term) {
        termTitleEditText.setText(term.getTermTitle());
        buildAlertActions();
        startDate.setText(term.getStartDate());
        endDate.setText(term.getEndDate());
        
        setFragmentArguments(term.getTermID());
        
        setUIReadOnly();
        setBellIcons();
    }
    
    private void setAddMode() {
        setUIEditable();
    }
    
    private void setEditMode(Term term) {
        
        termTitleEditText.setText(term.getTermTitle());
        
        startDate.setText(term.getStartDate());
        endDate.setText(term.getEndDate());
        
        setFragmentArguments(term.getTermID());
        
        setUIEditable();
        
    }
    
    private void setUIReadOnly() {
        term_text_input_layout.setVisibility(View.GONE);
        startDate.setEnabled(false);
        endDate.setEnabled(false);
        addEditTermButton.setVisibility(View.GONE);
        cancelTermButton.setVisibility(View.GONE);
    }
    
    private void setUIEditable() {
        term_text_input_layout.setVisibility(View.VISIBLE);
        startDate.setEnabled(true);
        endDate.setEnabled(true);
        addEditTermButton.setVisibility(View.VISIBLE);
        cancelTermButton.setVisibility(View.VISIBLE);
        buildCancelButton();
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
        String alarmKeyEnabled = "Alarm_term_" + selectedTerm.getTermID() + "_" + startOrEnd + "_date";
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
        
        String alarmKeyDate = "Alarm_term_" + selectedTerm.getTermID() + "_" + startOrEnd + "_date";
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
    
    public void addOrEditTerm(View view) {
        if (!termTitleEditText.getText().toString().trim().isEmpty()) {
            String newTitle = termTitleEditText.getText().toString().trim();
            String newStartDate = startDate.getText().toString();
            String newEndDate = endDate.getText().toString();
            
            Repository repository = new Repository(getApplication());
            
            if (isAddMode()) {
                Term term = new Term(newTitle, newStartDate, newEndDate);
                repository.insert(term);
            }
            
            if (isEditMode()) {
                selectedTerm.setTermTitle(newTitle);
                selectedTerm.setStartDate(newStartDate);
                selectedTerm.setEndDate(newEndDate);
                repository.update(selectedTerm);
            }
            
            navigateToTermsList();
        } else {
            showToast("Title cannot be blank");
        }
    }
    
    private void navigateToTermsList() {
        Intent viewIntent = new Intent(TermDetail.this, TermsList.class);
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
    
    private void setFragmentArguments(int termID) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        CourseListFragment fragment = (CourseListFragment) fragmentManager.findFragmentById(R.id.termfragmentcontainter);
        Bundle args = new Bundle();
        args.putInt("termID", termID);
        fragment.setArguments(args);
    }
    
    private void buildCancelButton() {
        cancelTermButton.setVisibility(View.VISIBLE);
        cancelTermButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditMode()) {
                    mode = MODE_VIEW;
                    setViewMode(selectedTerm);
                } else if (isAddMode()) {
                    onBackPressed();
                }
            }
        });
        
    }
    
    private void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        recreateCourseListFragment();
    }
    
    private void recreateCourseListFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        CourseListFragment fragment = (CourseListFragment) fragmentManager.findFragmentById(R.id.termfragmentcontainter);
        
        int currentTermID = fragment.getArguments().getInt("termID");
        
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(fragment);
        transaction.commit();
        
        CourseListFragment newFragment = new CourseListFragment();
        Bundle args = new Bundle();
        args.putInt("termID", currentTermID);
        newFragment.setArguments(args);
        
        transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.termfragmentcontainter, newFragment);
        transaction.commit();
    }
}
