package wgu.c192.wguschedulerkbaldr2.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import wgu.c192.wguschedulerkbaldr2.R;

public class AssessmentList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_assessment_list);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.assessmentfragmentcontainter, new AssessmentListFragment())
                    .commit();
        }
    }

}