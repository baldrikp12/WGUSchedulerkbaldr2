package wgu.c192.wguschedulerkbaldr2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import wgu.c192.wguschedulerkbaldr2.R;

public class CourseList extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
    
        TextView titleView = findViewById(R.id.plain_actionbar_title);
        titleView.setText("Your Courses");
        
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.coursefragmentcontainter, new CourseListFragment())
                    .commit();
        }
        
        FloatingActionButton addCourseBtn = findViewById(R.id.floatingActionButton2);
        
        addCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseList.this, CourseDetail.class);
                intent.putExtra(CourseDetail.MODE_KEY, CourseDetail.MODE_ADD);
                startActivity(intent);
            }
        });
        
        FloatingActionButton backFAB = findViewById(R.id.backFAB2);
        backFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

}