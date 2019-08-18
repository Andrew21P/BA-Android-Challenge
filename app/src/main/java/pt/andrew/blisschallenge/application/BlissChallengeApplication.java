package pt.andrew.blisschallenge.application;

import android.app.Application;

/**
 * Created by andrew.fernandes on 16/08/2019
 */
public final class BlissChallengeApplication extends Application  {

    private static BlissChallengeApplication instance = new BlissChallengeApplication();

    public static BlissChallengeApplication getInstance() {
        return instance;
    }
}
