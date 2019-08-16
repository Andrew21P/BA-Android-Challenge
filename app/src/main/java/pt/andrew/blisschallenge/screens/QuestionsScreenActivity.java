package pt.andrew.blisschallenge.screens;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pt.andrew.blisschallenge.R;
import pt.andrew.blisschallenge.adapter.QuestionsAdapter;
import pt.andrew.blisschallenge.model.Question;
import pt.andrew.blisschallenge.network.RetrofitInstance;
import pt.andrew.blisschallenge.network.entities.ServiceData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionsScreenActivity extends AppCompatActivity {

    @BindView(R.id.questionsScreenRecyclerView)
    RecyclerView _questionsRecyclerView;
    @BindView(R.id.questionsScreenLoaderContainer)
    View _loader;
    @BindView(R.id.questionsScreenEmptyState)
    View _emptyState;
    @BindView(R.id.questionsScreenTryAgainButton)
    View _tryAgainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_screen);
        ButterKnife.bind(this);

        ServiceData serviceData = RetrofitInstance.getRetrofitServiceInstance().create(ServiceData.class);
        Call<List<Question>> call = serviceData.getQuestions();
        callService(call);
    }

    private void callService(Call<List<Question>> call) {
        call.enqueue(new Callback<List<Question>>() {s
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                generateDataList(response.body());
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                showEmptyState(call);
            }
        });
    }

    private void generateDataList(List<Question> questionList) {
        _loader.setVisibility(View.GONE);
        QuestionsAdapter questionsAdapter = new QuestionsAdapter(this, questionList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(QuestionsScreenActivity.this);
        _questionsRecyclerView.setLayoutManager(layoutManager);
        _questionsRecyclerView.setAdapter(questionsAdapter);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.question_recycler_view_sepator));
        _questionsRecyclerView.addItemDecoration(itemDecorator);
    }

    private void showEmptyState(final Call<List<Question>> call) {
        _loader.setVisibility(View.GONE);
        _emptyState.setVisibility(View.VISIBLE);
        _tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _emptyState.setVisibility(View.GONE);
                _loader.setVisibility(View.VISIBLE);
                callService(call.clone());
            }
        });
    }
}
