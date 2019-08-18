package pt.andrew.blisschallenge.screens.base;

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

    @BindView(R.id.baseScreenActivityFrameContainer)
    FrameLayout _baseFragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_screen);
        startFromSplash();
    }

    private void startFromSplash() {
        getSupportFragmentManager().beginTransaction().add(R.id.baseScreenActivityFrameContainer, new QuestionScreenFragment()).commit();
    }
}
