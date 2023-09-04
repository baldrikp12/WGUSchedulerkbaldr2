package wgu.c192.wguschedulerkbaldr2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

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
    private Button cancelTermButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);

        int termId = getIntent().getIntExtra("TERM_ID", -1);
        int mode = getIntent().getIntExtra(MODE_KEY, MODE_VIEW);

        termTitleEditText = findViewById(R.id.termTitleTextview);
        addTermButton = findViewById(R.id.addBtn);
        cancelTermButton = findViewById(R.id.cancelBtn);

        // Assuming selectedTerm is initially null
        Term selectedTerm = null;

        if (termId != -1) {
            // Use the term ID to query the database and retrieve the corresponding term's data
            Repository repository = new Repository(getApplication());
            selectedTerm = repository.getAssociatedTerm(termId);
        }

        // Call the appropriate method based on the mode
        if (mode == MODE_VIEW && selectedTerm != null) {
            setViewMode(selectedTerm);
        } else if (mode == MODE_ADD) {
            setAddMode();
        }

    }

    private void setViewMode(Term term) {
        termTitleEditText.setText(term.getTermTitle());
        termTitleEditText.setEnabled(false);

        FragmentManager fragmentManager = getSupportFragmentManager();
        CourseListFragment fragment = (CourseListFragment) fragmentManager.findFragmentById(R.id.termfragmentcontainter);
        Bundle args = new Bundle();
        args.putInt("termID", term.getTermID());
        System.out.println("+++++++++++++++++++++++++++++++++++++termDetail"+args.get("termID"));
        fragment.setArguments(args);

        addTermButton.setVisibility(View.INVISIBLE);
        cancelTermButton.setVisibility(View.INVISIBLE);
    }

    private void setAddMode() {
        termTitleEditText.setEnabled(true); // Enable EditText for editing
        addTermButton.setVisibility(View.VISIBLE);
        cancelTermButton.setVisibility(View.VISIBLE);

    }

    public void addTerm(View view) {
        EditText titleText = findViewById(R.id.termTitleTextview);
        String title = titleText.getText().toString().trim();
        if (!title.isEmpty()) {
            Calendar calendar = Calendar.getInstance();
            Date dateS = calendar.getTime();

            calendar.set(Calendar.YEAR, 2023);
            calendar.set(Calendar.MONTH, Calendar.AUGUST);
            calendar.set(Calendar.DAY_OF_MONTH, 6);
            Date dateE = calendar.getTime();

            Term term = new Term(title, dateS, dateE);
            Repository repository = new Repository(getApplication());
            repository.insert(term);

            // After adding the term, switch to viewing mode by starting a new instance of the activity
            Intent viewIntent = new Intent(TermDetail.this, TermsList.class);
            viewIntent.putExtra(MODE_KEY, MODE_VIEW);
            viewIntent.putExtra("TERM_ID", term.getTermID());

            viewIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(viewIntent);

            // Finish the current activity (optional, depending on your workflow)
            finish();
        } else {
            // Display a message to the user indicating that the title is required.
            // You can use a Toast or another UI element for this purpose.
        }
    }
}