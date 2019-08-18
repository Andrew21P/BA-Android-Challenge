package pt.andrew.blisschallenge.screens.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pt.andrew.blisschallenge.R;
import pt.andrew.blisschallenge.dialog.ShareScreenDialog;
import pt.andrew.blisschallenge.helpers.ContentUrlHelper;
import pt.andrew.blisschallenge.helpers.DateHelper;
import pt.andrew.blisschallenge.helpers.ValidationsHelper;
import pt.andrew.blisschallenge.model.Choice;
import pt.andrew.blisschallenge.model.Question;
import pt.andrew.blisschallenge.model.ServiceStatus;
import pt.andrew.blisschallenge.network.RetrofitInstance;
import pt.andrew.blisschallenge.network.entities.ServiceData;
import pt.andrew.blisschallenge.screens.base.BaseScreenActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by andrew.fernandes on 18/08/2019
 */

public class DetailScreenFragment extends Fragment {

    private static String QUESTION_ID_ARGUMENT = "QUESTION_ID_ARGUMENT";
    private static String FROM_URI_ARGUMENT = "FROM_URI_ARGUMENT";
    private static int CHOICES_QTY_DEFAULT = 4;

    @BindView(R.id.detailScreenTitle)
    TextView _title;
    @BindView(R.id.detailScreenImage)
    ImageView _image;
    @BindView(R.id.detailScreenDate)
    TextView _date;
    @BindView(R.id.detailScreenOption1)
    CheckBox _option1;
    @BindView(R.id.detailScreenOption2)
    CheckBox _option2;
    @BindView(R.id.detailScreenOption3)
    CheckBox _option3;
    @BindView(R.id.detailScreenOption4)
    CheckBox _option4;
    @BindView(R.id.detailScreenBackButton)
    View _backButton;
    @BindView(R.id.detailScreenLoaderContainer)
    View _loader;
    @BindView(R.id.detailScreensShareFloatingButton)
    View _shareButton;
    @BindView(R.id.detailScreenVoteButton)
    TextView _voteButton;
    @BindView(R.id.detailScreenChoicesText)
    TextView _subTitle;
    @BindView(R.id.detailScreenOption1Result)
    TextView _option1Result;
    @BindView(R.id.detailScreenOption2Result)
    TextView _option2Result;
    @BindView(R.id.detailScreenOption3Result)
    TextView _option3Result;
    @BindView(R.id.detailScreenOption4Result)
    TextView _option4Result;
    @BindView(R.id.detailScreenEmptyState)
    View _emptyState;
    @BindView(R.id.detailScreenTryAgainButton)
    View _tryAgainButton;

    private Question _question;
    private int _questionId;
    private ServiceData _serviceData;
    private ShareScreenDialog _shareDialog;
    private boolean _fromURI = false;

    private SparseArray<CheckBox> _optionMap = new SparseArray<>();

    public DetailScreenFragment() {
        // Required empty public constructor
    }

    public static DetailScreenFragment newInstance(int questionId) {
        DetailScreenFragment detailScreenFragment = new DetailScreenFragment();
        Bundle args = new Bundle();
        args.putInt(QUESTION_ID_ARGUMENT, questionId);
        detailScreenFragment.setArguments(args);

        return detailScreenFragment;
    }

    public static DetailScreenFragment newInstance(int _questionId, boolean fromUri) {
        DetailScreenFragment detailScreenFragment = newInstance(_questionId);
        Bundle args = detailScreenFragment.getArguments();
        if (args != null) {
            args.putBoolean(FROM_URI_ARGUMENT, fromUri);
        }
        detailScreenFragment.setArguments(args);
        return detailScreenFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_screen, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        _loader.bringToFront();

        if (getArguments() != null) {
            if (getArguments().getBoolean(FROM_URI_ARGUMENT)) {
                _fromURI = true;
            }
        }

        _backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_fromURI) {
                    BaseScreenActivity activity = (BaseScreenActivity) getActivity();
                    activity.onBackPressed();
                    activity.startFromSplash();
                } else {
                    getActivity().onBackPressed();
                }
            }
        });

        _voteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateQuestion();
            }
        });

        _shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _shareDialog = new ShareScreenDialog();
                _shareDialog.initDialog(getContext());
                BaseScreenActivity activity = (BaseScreenActivity) getActivity();
                if (activity != null) {
                    activity.setShareDialog(_shareDialog);
                }
                _shareDialog.showDialog(getString(R.string.share_question));
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

        _optionMap.put(1, _option1);
        _optionMap.put(2, _option2);
        _optionMap.put(3, _option3);
        _optionMap.put(4, _option4);

        if (getArguments() != null) {
            _questionId = getArguments().getInt(QUESTION_ID_ARGUMENT);
        }

        _serviceData = RetrofitInstance.getRetrofitServiceInstance().create(ServiceData.class);
        callService();
    }

    private void callService() {
        Call<Question> call = _serviceData.getQuestion(_questionId);

        call.enqueue(new Callback<Question>() {
            @Override
            public void onResponse(Call<Question> call, Response<Question> response) {
                if (response.body() != null) {
                    _question = response.body();
                    setLayout();
                } else {
                    setEmptyState();
                }
            }

            @Override
            public void onFailure(Call<Question> call, Throwable t) {
                setEmptyState();
            }
        });
    }

    private void setEmptyState() {
        _emptyState.setVisibility(View.VISIBLE);
        _emptyState.bringToFront();

        _tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _emptyState.setVisibility(View.GONE);
                callService();
            }
        });
    }

    private void setLayout() {
        Glide.with(getContext()).load(_question.getQuestionImageUrl()).centerInside().into(_image);

        _title.setText(_question.getQuestionTitle());
        _date.setText(DateHelper.formatDate(_question.getQuestionPublishDate(), getContext()));

        List<Choice> choiceList = _question.getQuestionChoiceList();

        if (choiceList != null && choiceList.size() == CHOICES_QTY_DEFAULT) {
            _option1.setText(choiceList.get(0).getChoiceName());
            _option2.setText(choiceList.get(1).getChoiceName());
            _option3.setText(choiceList.get(2).getChoiceName());
            _option4.setText(choiceList.get(3).getChoiceName());

            setOptionListener();
        }

        _loader.setVisibility(View.GONE);
        _shareButton.setVisibility(View.VISIBLE);
    }

    private void setOptionListener() {
        for (int i = 0; i < _optionMap.size(); i++) {
            int key = _optionMap.keyAt(i);
            final CheckBox option = _optionMap.get(key);

            option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectOption(option);
                }
            });
        }
    }

    private void selectOption(CheckBox selectedOption) {
        _voteButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        _voteButton.setClickable(true);

        for (int i = 0; i < _optionMap.size(); i++) {
            int key = _optionMap.keyAt(i);
            CheckBox option = _optionMap.get(key);

            if (option != selectedOption) {
                option.setChecked(false);
            } else {
                option.setChecked(true);
            }
        }
    }

    private void updateQuestion() {
        _shareButton.setVisibility(View.GONE);
        _loader.setVisibility(View.VISIBLE);
        _loader.bringToFront();

        for (int i = 0; i < _optionMap.size(); i++) {
            int key = _optionMap.keyAt(i);
            CheckBox option = _optionMap.get(key);

            if (option.isChecked()) {
                String selectedOption = option.getText().toString();

                for (Choice currentChoice : _question.getQuestionChoiceList()) {
                    if (selectedOption.equals(currentChoice.getChoiceName())) {
                        currentChoice.sumChoiceVote();
                        break;
                    }
                }

                Call<Question> updateCall = _serviceData.updateQuestion(_questionId, _question);

                updateCall.enqueue(new Callback<Question>() {
                    @Override
                    public void onResponse(Call<Question> call, Response<Question> response) {
                        if (response.body() != null) {
                            setResultsLayout(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<Question> call, Throwable t) {
                        Log.d("ok", "ok");
                    }
                });
            }
        }
    }

    private void setResultsLayout(Question updatedQuestion) {
        List<Choice> choiceList = updatedQuestion.getQuestionChoiceList();

        _shareButton.setVisibility(View.VISIBLE);
        _subTitle.setText(getString(R.string.results));
        _voteButton.setVisibility(View.GONE);

        if (choiceList.size() == CHOICES_QTY_DEFAULT) {
            prepareOptionsResult(_option1, _option1Result, choiceList.get(0).getChoiceVotes());
            prepareOptionsResult(_option2, _option2Result, choiceList.get(1).getChoiceVotes());
            prepareOptionsResult(_option3, _option3Result, choiceList.get(2).getChoiceVotes());
            prepareOptionsResult(_option4, _option4Result, choiceList.get(3).getChoiceVotes());
        }

        _loader.setVisibility(View.GONE);
    }

    private void prepareOptionsResult(CheckBox option, TextView optionResult, int votes) {
        option.setClickable(false);
        optionResult.setText(getString(R.string.votes, votes));
        optionResult.setVisibility(View.VISIBLE);
    }

    private void shareQuestionList(String email) {
        _shareDialog.showLoader();

        Call<ServiceStatus> _shareCall = _serviceData.getShareResponse(email,
                ContentUrlHelper.getQuestionContentUrl(_questionId));

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
