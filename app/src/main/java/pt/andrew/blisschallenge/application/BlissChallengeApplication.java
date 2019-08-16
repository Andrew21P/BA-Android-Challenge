package pt.andrew.blisschallenge.application;

import android.app.Application;

/**
 * Created by andrew.fernandes on 16/08/2019
 */
public class BlissChallengeApplication extends Application  {
    private static final BlissChallengeApplication ourInstance = new BlissChallengeApplication();

    public static BlissChallengeApplication getInstance() {
        return ourInstance;
    }

    private BlissChallengeApplication() {
    }
}
