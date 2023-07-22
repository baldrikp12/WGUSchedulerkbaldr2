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
import wgu.c192.wguschedulerkbaldr2.entities.Course;

public class CourseListFragment extends Fragment {

    private Repository repository;
    private RecyclerView recyclerView;
    private CourseAdapter courseAdapter;

    public CourseListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_course_list, container, false);

        recyclerView = view.findViewById(R.id.courserecyclerview);
        courseAdapter = new CourseAdapter(getContext());
        recyclerView.setAdapter(courseAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        repository = new Repository(getActivity().getApplication());
        List<Course> allCourses = repository.getAllCourses(); // Ensure you have a method to get all courses in your Repository

        courseAdapter.setCourses(allCourses); // Ensure you have a setCourses method in your CourseAdapter


        return view;
    }
}