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

    /**
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return new CourseViewHolder
     */
    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View courseItemView = mInflater.inflate(R.layout.course_list_item, parent, false);

        return new CourseViewHolder((courseItemView));
    }


    /**
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        if (mCourses != null) {
            System.out.println("not null");
            Course currentCourse = mCourses.get(position);
            String name = currentCourse.getCourseTitle();
            holder.courseNameView.setText(name);
        } else {
            holder.courseNameView.setText("No Terms");
        }
    }

    /**
     * @return mCourses.size()
     */
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

        private CourseViewHolder(View itemView) {
            super(itemView);
            courseNameView = itemView.findViewById(R.id.termNameLabel);
            itemView.setOnClickListener(new View.OnClickListener() {
                /**
                 * @param v The view that was clicked.
                 */
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
