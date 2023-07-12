package wgu.c192.wguschedulerkbaldr2.ui;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import wgu.c192.wguschedulerkbaldr2.entities.Term;
import wgu.c192.wguschedulerkbaldr2.R;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermViewHolder> {

    private List<Term> mTerms;

    /**
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return
     */
    @NonNull
    @Override
    public TermAdapter.TermViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    /**
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull TermAdapter.TermViewHolder holder, int position) {

    }

    /**
     * @return
     */
    @Override
    public int getItemCount() {
        return mTerms.size();
    }

    public void setTerms(List<Term> terms) {
        mTerms = terms;
        notifyDataSetChanged();
    }

    class TermViewHolder extends RecyclerView.ViewHolder {
        private final TextView termNameView;

        private TermViewHolder(View itemView) {
            super(itemView);
            termNameView = itemView.findViewById(R.id.termNameLabel);
            itemView.setOnClickListener(new View.OnClickListener() {
                /**
                 * @param v The view that was clicked.
                 */
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final Term currentTerm = mTerms.get(position);


                }
            });
        }
    }

}
