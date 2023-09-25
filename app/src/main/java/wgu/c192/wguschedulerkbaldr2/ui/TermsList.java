package wgu.c192.wguschedulerkbaldr2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import wgu.c192.wguschedulerkbaldr2.R;
import wgu.c192.wguschedulerkbaldr2.database.Repository;
import wgu.c192.wguschedulerkbaldr2.entities.Term;

public class TermsList extends AppCompatActivity {
    
    private Repository repository;
    private TermAdapter termAdapter;
    private RecyclerView recyclerView;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_list);
        
        TextView titleView = findViewById(R.id.plain_actionbar_title);
        titleView.setText("Your Terms");
        
        recyclerView = findViewById(R.id.termrecyclerview);
        termAdapter = new TermAdapter(this);
        recyclerView.setAdapter(termAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        repository = new Repository(getApplication());
        List<Term> allTerms = repository.getAllTerms();
        termAdapter.setTerms(allTerms);
        
        FloatingActionButton addTermBtn = findViewById(R.id.floatingActionButton);
        addTermBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
          
                Intent intent = new Intent(TermsList.this, TermDetail.class);
                intent.putExtra(TermDetail.MODE_KEY, TermDetail.MODE_ADD);
                intent.putExtra("TERM_ID", -1);
                startActivity(intent);
            }
        });
        
        FloatingActionButton backFAB = findViewById(R.id.backFAB);
        backFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        List<Term> allTerms = repository.getAllTerms();
        termAdapter.setTerms(allTerms);
    }
}
