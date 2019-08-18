package pt.andrew.blisschallenge.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import pt.andrew.blisschallenge.application.BlissChallengeApplication;
import pt.andrew.blisschallenge.screens.base.BaseScreenActivity;
import pt.andrew.blisschallenge.services.NetworkService;

/**
 * Created by andrew.fernandes on 18/08/2019
 */

public class NetworkBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {

        BaseScreenActivity currentActivity = BlissChallengeApplication.getInstance().getCurrentActivity();

        if (!checkInternet(context) && currentActivity != null) {
            currentActivity.showNetworkError();
        } else if (checkInternet(context) && currentActivity != null) {
            currentActivity.hideNetworkError();
        }
    }

    boolean checkInternet(Context context) {
        NetworkService serviceManager = new NetworkService(context);
        if (serviceManager.isNetworkAvailable()) {
            return true;
        } else {
            return false;
        }
    }

}
