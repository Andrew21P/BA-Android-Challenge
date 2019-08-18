package pt.andrew.blisschallenge.screens.base;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import butterknife.BindView;
import pt.andrew.blisschallenge.R;
import pt.andrew.blisschallenge.screens.fragments.QuestionScreenFragment;

/**
 * Created by andrew.fernandes on 18/08/2019
 */

public class BaseScreenActivity extends AppCompatActivity {

    public static String QUESTION_LIST_FILTER_QUERY = "question_filter";

    @BindView(R.id.baseScreenActivityFrameContainer)
    FrameLayout _baseFragmentContainer;

    @Override
    protected void onNewIntent(Intent intent) {
        Uri data = intent.getData();

        if (data != null) {
            String filter = data.getQueryParameter(QUESTION_LIST_FILTER_QUERY);
            if (filter != null) {
                QuestionScreenFragment questionScreenFragment = QuestionScreenFragment.newInstance(filter);
                getSupportFragmentManager().beginTransaction().replace(R.id.baseScreenActivityFrameContainer, questionScreenFragment).addToBackStack(null).commit();
            }
        } else {
            startFromSplash();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_screen);
        onNewIntent(getIntent());
    }

    private void startFromSplash() {
        getSupportFragmentManager().beginTransaction().add(R.id.baseScreenActivityFrameContainer, new QuestionScreenFragment()).commit();
    }
}
