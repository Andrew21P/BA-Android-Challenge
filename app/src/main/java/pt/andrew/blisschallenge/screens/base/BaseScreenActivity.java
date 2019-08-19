package pt.andrew.blisschallenge.screens.base;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import pt.andrew.blisschallenge.R;
import pt.andrew.blisschallenge.application.BlissChallengeApplication;
import pt.andrew.blisschallenge.dialog.ShareScreenDialog;
import pt.andrew.blisschallenge.screens.fragments.DetailScreenFragment;
import pt.andrew.blisschallenge.screens.fragments.QuestionScreenFragment;

/**
 * Created by andrew.fernandes on 18/08/2019
 */

public class BaseScreenActivity extends AppCompatActivity {

    public static String QUESTION_LIST_FILTER_QUERY = "question_filter";
    public static String QUESTION_ID_QUERY = "question_id";

    @BindView(R.id.baseScreenActivityFrameContainer)
    FrameLayout _baseFragmentContainer;
    @BindView(R.id.baseNetworkErrorScreen)
    View _networkErrorScreen;

    private ShareScreenDialog _shareDialog;

    @Override
    protected void onNewIntent(Intent intent) {
        Uri data = intent.getData();

        if (data != null) {
            String filter = data.getQueryParameter(QUESTION_LIST_FILTER_QUERY);
            String questionId = data.getQueryParameter(QUESTION_ID_QUERY);
            if (filter != null) {
                QuestionScreenFragment questionScreenFragment = QuestionScreenFragment.newInstance(filter);
                getSupportFragmentManager().beginTransaction().replace(R.id.baseScreenActivityFrameContainer, questionScreenFragment).addToBackStack(null).commit();
            } else if (questionId != null) {
                DetailScreenFragment detailScreenFragment = DetailScreenFragment.newInstance(Integer.valueOf(questionId), true);
                getSupportFragmentManager().beginTransaction().replace(R.id.baseScreenActivityFrameContainer, detailScreenFragment).addToBackStack(null).commit();
            } else {
                startFromSplash();
            }
        } else {
            startFromSplash();
        }

        BlissChallengeApplication.getInstance().setCurrentActivity(BaseScreenActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_screen);
        ButterKnife.bind(this);
        onNewIntent(getIntent());
    }

    public void startFromSplash() {
        getSupportFragmentManager().beginTransaction().add(R.id.baseScreenActivityFrameContainer, new QuestionScreenFragment()).commit();
    }

    public void showNetworkError() {
        if (_shareDialog != null) {
            _shareDialog.dismissDialog();
        }

        _networkErrorScreen.setVisibility(View.VISIBLE);
        _networkErrorScreen.bringToFront();
    }

    public void hideNetworkError() {
        _networkErrorScreen.setVisibility(View.GONE);
    }

    public void setShareDialog(ShareScreenDialog shareDialog) {
        this._shareDialog = shareDialog;
    }
}
