package wgu.c192.wguschedulerkbaldr2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import wgu.c192.wguschedulerkbaldr2.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //test//

/*      Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2023);
        calendar.set(Calendar.MONTH, Calendar.AUGUST);  // Note: Months are 0-based in Java.
        calendar.set(Calendar.DAY_OF_MONTH, 6);
        Date dateS = calendar.getTime();

        calendar.set(Calendar.YEAR, 2023);
        calendar.set(Calendar.MONTH, Calendar.AUGUST);  // Note: Months are 0-based in Java.
        calendar.set(Calendar.DAY_OF_MONTH, 6);
        Date dateE = calendar.getTime();

        Term term = new Term("summer", dateS, dateE);
        Course course = new Course("Science");

        Assessment assessment = new Assessment("HW");

        Repository repository = new Repository(getApplication());
        repository.insert(term);
        repository.insert(course);
        repository.insert(assessment);*/

        //test//
        Button termsButton = findViewById(R.id.termsButton);
        Button courseButton = findViewById(R.id.courseButton);
        Button assessmentsButton = findViewById(R.id.assessmentsButton);

        termsButton.setOnClickListener(this);
        courseButton.setOnClickListener(this);
        assessmentsButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;

        if (view.getId() == R.id.termsButton) {
            intent = new Intent(MainActivity.this, TermsList.class);
        } else if (view.getId() == R.id.courseButton) {
            intent = new Intent(MainActivity.this, CourseList.class);
        } else if (view.getId() == R.id.assessmentsButton) {
            intent = new Intent(MainActivity.this, AssessmentList.class);
        }

        if (intent != null) {
            startActivity(intent);
        }
    }
}