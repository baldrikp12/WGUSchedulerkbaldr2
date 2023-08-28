package wgu.c192.wguschedulerkbaldr2.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;

import wgu.c192.wguschedulerkbaldr2.R;
import wgu.c192.wguschedulerkbaldr2.database.Repository;
import wgu.c192.wguschedulerkbaldr2.entities.Term;

public class TermDetail extends AppCompatActivity {

    public static final String MODE_KEY = "mode";
    public static final int MODE_VIEW = 0;
    public static final int MODE_ADD = 1;

    private EditText termTitleEditText;
    private Button addTermButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);

        int termId = getIntent().getIntExtra("TERM_ID", -1);
        if (termId != -1) {
            // Use the term ID to query the database and retrieve the corresponding term's data
            Repository repository = new Repository(getApplication());
            Term selectedTerm = repository.getAssociatedTerm(termId);

            termTitleEditText = findViewById(R.id.termTitleTextview);
            addTermButton = findViewById(R.id.addBtn);

            int mode = getIntent().getIntExtra(MODE_KEY, MODE_VIEW);

            if (mode == MODE_VIEW && selectedTerm != null) {
                termTitleEditText.setText(selectedTerm.getTermTitle());
                // Initialize your fragment or other UI for viewing mode if needed
            } else if (mode == MODE_ADD) {
                setViewToEdit();
            }
        }
    }

    private void setViewToEdit() {
        termTitleEditText.setEnabled(true); // Enable EditText for editing
        addTermButton.setVisibility(View.VISIBLE);

        addTermButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = termTitleEditText.getText().toString();

                // Create new term
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, 2023);
                calendar.set(Calendar.MONTH, Calendar.AUGUST);
                calendar.set(Calendar.DAY_OF_MONTH, 6);
                Date startDate = calendar.getTime();

                calendar.set(Calendar.YEAR, 2023);
                calendar.set(Calendar.MONTH, Calendar.AUGUST);
                calendar.set(Calendar.DAY_OF_MONTH, 6);
                Date endDate = calendar.getTime();

                Term term = new Term(title, startDate, endDate);
                Repository repository = new Repository(getApplication());
                repository.insert(term);

                termTitleEditText.setEnabled(false); // Disable EditText
                addTermButton.setVisibility(View.INVISIBLE);
            }
        });
    }
}
