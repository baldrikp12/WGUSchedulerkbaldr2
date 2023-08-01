package wgu.c192.wguschedulerkbaldr2.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import wgu.c192.wguschedulerkbaldr2.R;
import wgu.c192.wguschedulerkbaldr2.database.Repository;
import wgu.c192.wguschedulerkbaldr2.entities.Assessment;

public class AssessmentListFragment extends Fragment {

    private Repository repository;
    private RecyclerView recyclerView;
    private AssessmentAdapter assessmentAdapter;

    public AssessmentListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assessment_list, container, false);

        recyclerView = view.findViewById(R.id.assessmentrecyclerview);
        assessmentAdapter = new AssessmentAdapter(getContext());
        recyclerView.setAdapter(assessmentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        repository = new Repository(getActivity().getApplication());
        List<Assessment> allAssessments = repository.getAllAssessments(); // Ensure you have a method to get all assessments in your Repository

        assessmentAdapter.setAssessments(allAssessments); // Ensure you have a setAssessments method in your AssessmentAdapter


        return view;
    }
}