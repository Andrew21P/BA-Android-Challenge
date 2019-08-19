package pt.andrew.blisschallenge.screens.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pt.andrew.blisschallenge.R;
import pt.andrew.blisschallenge.views.EndlessRecyclerViewScrollListener;
import pt.andrew.blisschallenge.adapter.QuestionsAdapter;
import pt.andrew.blisschallenge.dialog.ShareScreenDialog;
import pt.andrew.blisschallenge.helpers.ContentUrlHelper;
import pt.andrew.blisschallenge.helpers.ValidationsHelper;
import pt.andrew.blisschallenge.model.Question;
import pt.andrew.blisschallenge.model.ServiceStatus;
import pt.andrew.blisschallenge.network.RetrofitInstance;
import pt.andrew.blisschallenge.interfaces.ServiceData;
import pt.andrew.blisschallenge.screens.base.BaseScreenActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by andrew.fernandes on 18/08/2019
 */

public class QuestionScreenFragment extends Fragment {

    private static final String QUESTION_SCREEN_FILTER_ARG = "QUESTION_SCREEN_FILTER_ARG";

    private static final int ITEM_VIEW_CACHE_SIZE = 20;
    private static final int QUESTION_LIMIT = 10;

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
    @BindView(R.id.questionsScreensShareFloatingButton)
    View _shareButton;
    @BindView(R.id.questionsScreenSearchViewHorizontalSeparator)
    View _searchContainerSeparator;

    private ServiceData _serviceData;
    private QuestionsAdapter _questionsAdapter;
    private List<Question> _questionList;
    private ShareScreenDialog _shareDialog;
    private int _offset = 0;
    private String _filter = "";
    private boolean _loadMore = false;

    public QuestionScreenFragment() {
        // Required empty public constructor
    }

    public static QuestionScreenFragment newInstance(String filter) {
        QuestionScreenFragment questionScreenFragment = new QuestionScreenFragment();
        Bundle args = new Bundle();
        args.putString(QUESTION_SCREEN_FILTER_ARG, filter);
        questionScreenFragment.setArguments(args);
        return questionScreenFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_screen, container, false);
        ButterKnife.bind(this, view);

        //Back pressed Logic for fragment
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        getActivity().finish();
                        return true;
                    }
                }
                return true;
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments() != null) {
            if (getArguments().get(QUESTION_SCREEN_FILTER_ARG) != null) {
                _filter = (String) getArguments().get(QUESTION_SCREEN_FILTER_ARG);

                if (_filter != null && !_filter.equals("")) {
                    _searchView.setQuery(_filter, false);
                    _searchView.clearFocus();
                } else if (_filter.equals("")){
                    _searchView.onActionViewExpanded();
                }
            }
        }

        _questionList = new ArrayList<>();
        _questionsRecyclerView.setItemViewCacheSize(ITEM_VIEW_CACHE_SIZE);
        _questionsRecyclerView.setDrawingCacheEnabled(true);
        _questionsRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);

        ImageView closeButton = this._searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _offset = 0;
                _filter = "";
                _questionList.clear();
                _questionsAdapter.notifyDataSetChanged();
                _searchView.setQuery(_filter, false);
                _searchView.clearFocus();
                search();
            }
        });

        _searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                _offset = 0;
                _questionList.clear();
                _questionsAdapter.notifyDataSetChanged();
                search();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        _shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _searchView.clearFocus();
                _shareDialog = new ShareScreenDialog();
                _shareDialog.initDialog(getContext());
                BaseScreenActivity activity = (BaseScreenActivity) getActivity();
                if (activity != null) {
                    activity.setShareDialog(_shareDialog);
                }
                _shareDialog.showDialog(getString(R.string.share_results));
                _shareDialog.setPositiveButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                        if (inputMethodManager != null) {
                            inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                        }
                        if (ValidationsHelper.isValidEmail(_shareDialog.getEmail())) {
                            shareQuestionList(_shareDialog.getEmail());
                        }
                    }
                });
            }
        });

        _loader.bringToFront();

        ImageView searchIcon = _searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        ImageView searchCloseIcon = _searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        searchIcon.setColorFilter(getContext().getColor(R.color.colorPrimary));
        searchCloseIcon.setColorFilter(getContext().getColor(R.color.colorDemiWhite));
        TextView _searchText = _searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        Typeface customFont = ResourcesCompat.getFont(getContext(), R.font.raleway);
        _searchText.setTypeface(customFont);
        _searchText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_body_size));

        _serviceData = RetrofitInstance.getRetrofitServiceInstance().create(ServiceData.class);
        callService();
    }

    private void callService() {
        Call<List<Question>> _call = _serviceData.getQuestions(_offset, _filter);

        _call.enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                _searchView.setVisibility(View.VISIBLE);
                _searchContainerSeparator.setVisibility(View.VISIBLE);
                _shareButton.setVisibility(View.VISIBLE);
                generateDataList(response.body());
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                showEmptyState();
            }
        });
    }

    private void generateDataList(List<Question> questionList) {
        _questionList.addAll(questionList);
        _shareButton.setVisibility(View.VISIBLE);
        _loader.setVisibility(View.GONE);

        _loadMore = questionList.size() == QUESTION_LIMIT;

        if (_questionsAdapter == null) {
            _questionsAdapter = new QuestionsAdapter(getContext(), _questionList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            _questionsRecyclerView.setLayoutManager(layoutManager);
            _questionsRecyclerView.setAdapter(_questionsAdapter);
            DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
            itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.question_recycler_view_sepator));
            _questionsRecyclerView.addItemDecoration(itemDecorator);
            EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
                @Override
                public void onLoadMore() {
                    if (_loadMore) {
                        _offset = _offset + QUESTION_LIMIT;
                        callService();
                    }

                }
            };
            _questionsRecyclerView.addOnScrollListener(scrollListener);
        } else {
            _questionsAdapter.notifyDataSetChanged();
        }
    }

    private void showEmptyState() {
        _loader.setVisibility(View.GONE);
        _shareButton.setVisibility(View.GONE);
        _searchView.setVisibility(View.GONE);
        _searchContainerSeparator.setVisibility(View.GONE);
        _emptyState.setVisibility(View.VISIBLE);
        _tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _emptyState.setVisibility(View.GONE);
                _shareButton.setVisibility(View.GONE);
                _loader.setVisibility(View.VISIBLE);
                _loader.bringToFront();
                callService();
            }
        });
    }

    private void search() {
        _searchView.bringToFront();
        _searchContainerSeparator.bringToFront();
        _shareButton.setVisibility(View.GONE);
        _loader.setVisibility(View.VISIBLE);
        _filter = _searchView.getQuery().toString();
        callService();
    }

    private void shareQuestionList(String email) {
        _shareDialog.showLoader();

        Call<ServiceStatus> _shareCall = _serviceData.getShareResponse(email,
                ContentUrlHelper.getQuestionListContentUrl(ServiceData.QUESTION_LIMIT,
                        String.valueOf(_offset), _filter));

        _shareCall.enqueue(new Callback<ServiceStatus>() {
            @Override
            public void onResponse(Call<ServiceStatus> call, Response<ServiceStatus> response) {
                if (ValidationsHelper.isServiceHealthOk(response.body())) {
                    _shareDialog.showResume(getString(R.string.share_positive_resume));
                } else {
                    _shareDialog.showResume(getString(R.string.share_negative_resume));
                }
            }

            @Override
            public void onFailure(Call<ServiceStatus> call, Throwable t) {
                _shareDialog.showResume(getString(R.string.share_negative_resume));
            }
        });
    }
}
