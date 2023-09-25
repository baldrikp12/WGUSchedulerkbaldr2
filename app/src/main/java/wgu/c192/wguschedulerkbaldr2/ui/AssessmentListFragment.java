package wgu.c192.wguschedulerkbaldr2.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import wgu.c192.wguschedulerkbaldr2.R;
import wgu.c192.wguschedulerkbaldr2.database.Repository;
import wgu.c192.wguschedulerkbaldr2.entities.Assessment;

public class AssessmentListFragment extends Fragment {
    
    private Repository repository;
    private RecyclerView recyclerView;
    private AssessmentAdapter assessmentAdapter;
    private List<Assessment> allAssessments = new ArrayList<>();
    
    public AssessmentListFragment() {

    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assessment_list, container, false);
        
        recyclerView = view.findViewById(R.id.assessmentrecyclerview);
        assessmentAdapter = new AssessmentAdapter(getContext());
        recyclerView.setAdapter(assessmentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        repository = new Repository(getActivity().getApplication());
        Bundle arguments = this.getArguments();
        
        int courseID = 0;
        
        if (arguments != null) {
            courseID = arguments.getInt("courseID");
        }
        
        if (courseID == 0) {
            allAssessments.addAll(repository.getAllAssessments());
        } else {
            allAssessments.addAll(repository.getAssessmentsByCourseID(courseID));
        }
        
        assessmentAdapter.setAssessments(allAssessments);
        assessmentAdapter.notifyDataSetChanged();
        
        return view;
    }
}