package wgu.c192.wguschedulerkbaldr2.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import wgu.c192.wguschedulerkbaldr2.R;

public class TermDetail extends AppCompatActivity {

    public static final String MODE_KEY = "mode";
    public static final int MODE_VIEW = 0;
    public static final int MODE_ADD = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);

        //checks if we'll be viewing or adding
        int mode = getIntent().getIntExtra(MODE_KEY, MODE_VIEW);
        if (mode == MODE_VIEW) {
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.termfragmentcontainter, new CourseListFragment())
                        .commit();
            }
        } else if (mode == MODE_ADD) {
            setIntent(null);
        }


    }
}