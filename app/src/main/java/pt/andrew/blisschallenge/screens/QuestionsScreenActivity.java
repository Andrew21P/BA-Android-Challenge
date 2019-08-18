package pt.andrew.blisschallenge.screens;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

    private static final int ITEM_VIEW_CACHE_SIZE = 20;

    @BindView(R.id.questionsScreenRecyclerView)
    RecyclerView _questionsRecyclerView;
    @BindView(R.id.questionsScreenLoaderContainer)
    View _loader;
    @BindView(R.id.questionsScreenEmptyState)
    View _emptyState;
    @BindView(R.id.questionsScreenTryAgainButton)
    View _tryAgainButton;
    @BindView(R.id.questionsScreenSearchView)
    SearchView _searchView;
    @BindView(R.id.questionsScreensShareButton)
    View _shareButton;
    @BindView(R.id.questionsScreenSearchViewHorizontalSeparator)
    View _searchContainerSeparator;

    private ServiceData _serviceData;
    private QuestionsAdapter _questionsAdapter;
    private Call<List<Question>> _call;
    private int _offset = 0;
    private String _filter = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_screen);
        ButterKnife.bind(this);

        _questionsRecyclerView.setItemViewCacheSize(ITEM_VIEW_CACHE_SIZE);
        _questionsRecyclerView.setDrawingCacheEnabled(true);
        _questionsRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        ImageView closeButton = this._searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _filter = "";
                _searchView.setQuery(_filter, false);
                _searchView.clearFocus();
                search();
            }
        });

        _searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                search();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        _loader.bringToFront();

        ImageView searchIcon = _searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        ImageView searchCloseIcon = _searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        searchIcon.setColorFilter(getColor(R.color.colorPrimary));
        searchCloseIcon.setColorFilter(getColor(R.color.pureWhite));
        TextView _searchText = _searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        Typeface customFont = ResourcesCompat.getFont(this, R.font.raleway);
        _searchText.setTypeface(customFont);

        _serviceData = RetrofitInstance.getRetrofitServiceInstance().create(ServiceData.class);
        callService();
    }

    private void callService() {
        _call = _serviceData.getQuestions(_offset, _filter);

        _call.enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                generateDataList(response.body());
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                showEmptyState();
            }
        });
    }

    private void generateDataList(List<Question> questionList) {
        _loader.setVisibility(View.GONE);

        if (_questionsAdapter == null) {
            _questionsAdapter = new QuestionsAdapter(this, questionList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(QuestionsScreenActivity.this);
            _questionsRecyclerView.setLayoutManager(layoutManager);
            _questionsRecyclerView.setAdapter(_questionsAdapter);
            DividerItemDecoration itemDecorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
            itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.question_recycler_view_sepator));
            _questionsRecyclerView.addItemDecoration(itemDecorator);
        } else {
            _questionsAdapter.notifyDataSetChanged();
        }
    }

    private void showEmptyState() {
        _loader.setVisibility(View.GONE);
        _emptyState.setVisibility(View.VISIBLE);
        _tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _emptyState.setVisibility(View.GONE);
                _loader.setVisibility(View.VISIBLE);
                _loader.bringToFront();
                callService();
            }
        });
    }

    private void search() {
        _searchView.bringToFront();
        _shareButton.bringToFront();
        _searchContainerSeparator.bringToFront();
        _loader.setVisibility(View.VISIBLE);
        _filter = _searchView.getQuery().toString();
        callService();
    }
}
