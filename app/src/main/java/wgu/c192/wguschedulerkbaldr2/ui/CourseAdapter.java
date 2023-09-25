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
import wgu.c192.wguschedulerkbaldr2.entities.Course;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private final Context context;
    private final LayoutInflater mInflater;
    private List<Course> mCourses;
    
    public CourseAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View courseItemView = mInflater.inflate(R.layout.course_list_item, parent, false);
        
        return new CourseViewHolder((courseItemView));
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        if (mCourses != null) {
            Course currentTerm = mCourses.get(position);
            String name = currentTerm.getCourseTitle();
            String start = currentTerm.getCourseStart();
            String end = currentTerm.getCourseEnd();
            holder.courseNameView.setText(name);
            holder.startDateView.setText(start);
            holder.endDateView.setText(end);
        
        } else {
            holder.courseNameView.setText("N/A");
            holder.startDateView.setText("N/A");
            holder.endDateView.setText("N/A");
        }
    }
 
    @Override
    public int getItemCount() {
        return mCourses.size();
    }
    
    public void setCourses(List<Course> courses) {
        mCourses = courses;
        notifyDataSetChanged();
    }
    
    class CourseViewHolder extends RecyclerView.ViewHolder {
        private final TextView courseNameView;
        private final TextView startDateView;
        private final TextView endDateView;
        
        private CourseViewHolder(View itemView) {
            super(itemView);
            courseNameView = itemView.findViewById(R.id.courseNameLabel);
            startDateView = itemView.findViewById(R.id.cStartDateLabel);
            endDateView = itemView.findViewById(R.id.cEndDateLabel);
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Course currentCourse = mCourses.get(position);
                        
                        Intent intent = new Intent(context, CourseDetail.class);
                        intent.putExtra(TermDetail.MODE_KEY, TermDetail.MODE_VIEW);
                        intent.putExtra("COURSE_ID", currentCourse.getCourseID());
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}