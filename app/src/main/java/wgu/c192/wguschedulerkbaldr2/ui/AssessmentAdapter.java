package wgu.c192.wguschedulerkbaldr2.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import wgu.c192.wguschedulerkbaldr2.R;
import wgu.c192.wguschedulerkbaldr2.entities.Assessment;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.CourseViewHolder> {
    private final Context context;
    private final LayoutInflater mInflater;
    private List<Assessment> mAssessments;
    
    public AssessmentAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        
    }
    

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View termItemView = mInflater.inflate(R.layout.assessment_list_item, parent, false);
        
        return new CourseViewHolder((termItemView));
    }
    
    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        if (mAssessments != null) {
            Assessment currentAssessment = mAssessments.get(position);
            String name = currentAssessment.getAssessmentTitle();
            String start = currentAssessment.getStartDate();
            String end = currentAssessment.getEndDate();
            holder.assessmentNameView.setText(name);
            holder.startDateView.setText(start);
            holder.endDateView.setText(end);
        
        } else {
            holder.assessmentNameView.setText("N/A");
            holder.startDateView.setText("N/A");
            holder.endDateView.setText("N/A");
        }
    }
    
    @Override
    public int getItemCount() {
        return mAssessments.size();
    }
    
    public void setAssessments(List<Assessment> assessment) {
        mAssessments = assessment;
        notifyDataSetChanged();
    }
    
    class CourseViewHolder extends RecyclerView.ViewHolder {
        private final TextView assessmentNameView;
        private final TextView startDateView;
        private final TextView endDateView;
    
        private CourseViewHolder(View itemView) {
            super(itemView);
            assessmentNameView = itemView.findViewById(R.id.assessmentNameLabel);
            startDateView = itemView.findViewById(R.id.aStartDateLabel);
            endDateView = itemView.findViewById(R.id.aEndDateLabel);
            itemView.setOnClickListener(new View.OnClickListener() {
              
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Assessment currentAssessment = mAssessments.get(position);
                        
                        Intent intent = new Intent(context, AssessmentDetail.class);
                        intent.putExtra(TermDetail.MODE_KEY, TermDetail.MODE_VIEW);
                        intent.putExtra("ASSESSMENT_ID", currentAssessment.getAssessmentID());
                        context.startActivity(intent);
                    }
                    
                }
            });
        }
    }
}