package pt.andrew.blisschallenge.adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import pt.andrew.blisschallenge.R;
import pt.andrew.blisschallenge.helpers.DateHelper;
import pt.andrew.blisschallenge.model.Question;
import pt.andrew.blisschallenge.screens.fragments.DetailScreenFragment;

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

        private final View questionView;
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
    public void onBindViewHolder(final QuestionViewHolder holder, int position) {
        final Question currentQuestion = _questionList.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //On row click open detail screen fragment
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                DetailScreenFragment detailScreenFragment = DetailScreenFragment.newInstance(currentQuestion.getQuestionId());
                activity.getSupportFragmentManager().beginTransaction().add(R.id.baseScreenActivityFrameContainer, detailScreenFragment).addToBackStack(null).commit();
            }
        });

        holder.questionTitle.setText(currentQuestion.getQuestionTitle());
        holder.questionData.setText(DateHelper.formatDate(currentQuestion.getQuestionPublishDate()));

        //Load image thumbnail with Glade
        Glide.with(_context).load(currentQuestion.getQuestionThumbnailUrl())
                .centerCrop().placeholder(R.mipmap.ic_launcher)
                .into(holder.questionThumbnail);
    }

}

