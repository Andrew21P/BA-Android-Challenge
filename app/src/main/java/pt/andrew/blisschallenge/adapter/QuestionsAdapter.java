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
import pt.andrew.blisschallenge.helpers.DateHelper;
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

        private TextView questionTitle;
        private TextView questionData;
        private ImageView questionThumbnail;

        QuestionViewHolder(View itemView) {
            super(itemView);
            questionView = itemView;

            questionTitle = questionView.findViewById(R.id.questionRowTitle);
            questionData = questionView.findViewById(R.id.questionRowData);
            questionThumbnail = questionView.findViewById(R.id.questionRowThumbnail);
        }
    }

    @Override
    public int getItemCount() {
        return _questionList.size();
    }

    @Override
    public QuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.question_row, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QuestionViewHolder holder, int position) {
        Question currentQuestion = _questionList.get(position);

        holder.questionTitle.setText(currentQuestion.getQuestionTitle());
        holder.questionData.setText(DateHelper.formatDate(currentQuestion.getQuestionPublishDate(), _context));

        Picasso.Builder builder = new Picasso.Builder(_context);
        builder.downloader(new OkHttp3Downloader(_context));
        builder.build().load(currentQuestion.getQuestionThumbnailUrl())
                .placeholder((R.mipmap.ic_launcher))
                .error(R.mipmap.ic_launcher)
                .into(holder.questionThumbnail);
    }

}

