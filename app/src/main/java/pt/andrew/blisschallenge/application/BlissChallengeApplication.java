package pt.andrew.blisschallenge.application;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import pt.andrew.blisschallenge.broadcasts.NetworkBroadcast;
import pt.andrew.blisschallenge.screens.base.BaseScreenActivity;

/**
 * Created by andrew.fernandes on 16/08/2019
 */
public final class BlissChallengeApplication extends Application {

    private BaseScreenActivity _currentActivity;

    private static BlissChallengeApplication instance = new BlissChallengeApplication();

    public static BlissChallengeApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerReceiver(new NetworkBroadcast(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public BaseScreenActivity getCurrentActivity() {
        return _currentActivity;
    }

    public void setCurrentActivity(BaseScreenActivity currentActivity) {
        this._currentActivity = currentActivity;
    }
}
