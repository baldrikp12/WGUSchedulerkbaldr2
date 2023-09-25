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
import wgu.c192.wguschedulerkbaldr2.entities.Term;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermViewHolder> {
    private final Context context;
    private final LayoutInflater mInflater;
    private List<Term> mTerms;

    public TermAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;

    }

    @NonNull
    @Override
    public TermAdapter.TermViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View termItemView = mInflater.inflate(R.layout.term_list_item, parent, false);

        return new TermViewHolder((termItemView));
    }

    @Override
    public void onBindViewHolder(@NonNull TermAdapter.TermViewHolder holder, int position) {
        if (mTerms != null) {
            Term currentTerm = mTerms.get(position);
            String name = currentTerm.getTermTitle();
            String start = currentTerm.getStartDate();
            String end = currentTerm.getEndDate();
            holder.termNameView.setText(name);
            holder.startDateView.setText(start);
            holder.endDateView.setText(end);

        } else {
            holder.termNameView.setText("N/A");
            holder.startDateView.setText("N/A");
            holder.endDateView.setText("N/A");
        }
    }
    
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
        private final TextView startDateView;
        private final TextView endDateView;

        private TermViewHolder(View itemView) {
            super(itemView);
            termNameView = itemView.findViewById(R.id.termNameLabel);
            startDateView = itemView.findViewById(R.id.startDateLabel);
            endDateView = itemView.findViewById(R.id.endDateLabel);
            itemView.setOnClickListener(new View.OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Term currentTerm = mTerms.get(position);
                        
                        Intent intent = new Intent(context, TermDetail.class);
                        intent.putExtra(TermDetail.MODE_KEY, TermDetail.MODE_VIEW);
                        intent.putExtra("TERM_ID", currentTerm.getTermID());
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}
