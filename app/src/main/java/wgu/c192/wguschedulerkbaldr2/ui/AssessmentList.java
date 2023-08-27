package wgu.c192.wguschedulerkbaldr2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

        FloatingActionButton addAssessmentBtn = findViewById(R.id.floatingActionButton3);

        addAssessmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AssessmentList.this, AssessmentDetail.class);
                intent.putExtra("MODE_KEY", 1); // or MODE_ADD
                startActivity(intent);
            }
        });

        FloatingActionButton backFAB = findViewById(R.id.backFAB3);
        backFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

}