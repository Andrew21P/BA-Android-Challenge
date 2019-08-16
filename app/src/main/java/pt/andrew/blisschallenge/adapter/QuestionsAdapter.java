package pt.andrew.blisschallenge.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

import pt.andrew.blisschallenge.R;
import pt.andrew.blisschallenge.model.Question;

/**
 * Created by andrew.fernandes on 16/08/2019
 */
public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.QuestionViewHolder> {

    private Context _context;
    private List<Question> _questionList;

    public QuestionsAdapter(Context context, List<Question> questionList) {
        this._context = context;
        this._questionList = questionList;
    }

    class QuestionViewHolder extends RecyclerView.ViewHolder {

        public final View questionView;

        TextView txtTitle;
        private ImageView coverImage;

        QuestionViewHolder(View itemView) {
            super(itemView);
            questionView = itemView;

            txtTitle = questionView.findViewById(R.id.title);
            coverImage = questionView.findViewById(R.id.coverImage);
        }
    }

    @Override
    public int getItemCount() {
        return _questionList.size();
    }

    @Override
    public QuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_row, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QuestionViewHolder holder, int position) {
        holder.txtTitle.setText(_questionList.get(position).getTitle());

        Picasso.Builder builder = new Picasso.Builder(_context);
        builder.downloader(new OkHttp3Downloader(_context));
        builder.build().load(_questionList.get(position).getThumbnailUrl())
                .placeholder((R.drawable.ic_launcher_background))
                .error(R.drawable.ic_launcher_background)
                .into(holder.coverImage);
    }

}

