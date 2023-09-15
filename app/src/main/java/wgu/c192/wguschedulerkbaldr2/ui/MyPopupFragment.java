package wgu.c192.wguschedulerkbaldr2.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import wgu.c192.wguschedulerkbaldr2.R;

public class MyPopupFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_assessment_list, container, false);
        return view;
    }

}
