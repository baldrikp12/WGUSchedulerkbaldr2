package wgu.c192.wguschedulerkbaldr2.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import wgu.c192.wguschedulerkbaldr2.R;
import wgu.c192.wguschedulerkbaldr2.entities.Assessment;

public class AssessmentDetail extends AppCompatActivity {
    
    private static final int MODE_VIEW = 0;
    private static final int MODE_ADD = 1;
    private static final int MODE_EDIT = 2;
    
    private int mode;
    private Assessment selectedAssessment;
    
    private TextInputLayout assessmentTextLayout;
    private EditText assessmentTitleEditText;
    private TextView assessmentstartDate;
    private TextView assessmentEndDate;
    private DatePickerDialog dueDatePicker;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail);
        
        // Initialize UI elements
        assessmentTextLayout = findViewById(R.id.assessment_text_input_layout);
        assessmentTitleEditText = findViewById(R.id.assessmentTitleTextview);
        assessmentstartDate = findViewById(R.id.assessmentStartDateLabel);
        assessmentEndDate = findViewById(R.id.assessmentEndDateLabel);
        
    }


    
    private void enableEditFields() {
        assessmentTitleEditText.setEnabled(true);
        assessmentstartDate.setEnabled(true);
        assessmentEndDate.setEnabled(true);
    }
    
    private void disableEditFields() {
        assessmentTitleEditText.setEnabled(false);
        assessmentstartDate.setEnabled(false);
        assessmentEndDate.setEnabled(false);
    }
    

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    
    


}
