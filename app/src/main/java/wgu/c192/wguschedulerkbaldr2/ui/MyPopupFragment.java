package wgu.c192.wguschedulerkbaldr2.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import wgu.c192.wguschedulerkbaldr2.R;

public class MyPopupFragment extends DialogFragment {
    
    private int courseID;
    public MyPopupFragment(int courseID) {
        this.courseID = courseID;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_assessment_list, container, false);
    
        // Create a bundle with arguments
        Bundle args = new Bundle();
        args.putInt("courseID", courseID);
    
        AssessmentListFragment fragmentList = (AssessmentListFragment) getChildFragmentManager().findFragmentById(R.id.assessmentfragmentcontainter);
        if (fragmentList != null) {
            fragmentList.setArguments(args);
        }
        view.findViewById(R.id.backFAB3).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.floatingActionButton3).setVisibility(View.INVISIBLE);
        return view;
    }
    
}
