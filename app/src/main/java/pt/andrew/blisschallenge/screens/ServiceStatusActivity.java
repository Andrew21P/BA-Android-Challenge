package pt.andrew.blisschallenge.screens;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pt.andrew.blisschallenge.R;
import pt.andrew.blisschallenge.helpers.ValidationsHelper;
import pt.andrew.blisschallenge.model.ServiceStatus;
import pt.andrew.blisschallenge.network.RetrofitInstance;
import pt.andrew.blisschallenge.network.entities.ServiceData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceStatusActivity extends AppCompatActivity {

    private static final int SPLASHSCREEN_DURATION = 1150;

    @BindView(R.id.serviceHealthMainContainer)
    View _mainContainer;
    @BindView(R.id.serviceHealthLoaderContainer)
    View _loader;
    @BindView(R.id.serviceHealthDescription)
    TextView _description;
    @BindView(R.id.serviceHealthStatusIcon)
    ImageView _statusIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_health);
        ButterKnife.bind(this);

        ServiceData serviceData = RetrofitInstance.getRetrofitServiceInstance().create(ServiceData.class);
        Call<ServiceStatus> statusCall = serviceData.getServiceStatus();
        statusCall.enqueue(new Callback<ServiceStatus>() {
            @Override
            public void onResponse(Call<ServiceStatus> call, Response<ServiceStatus> response) {

                _loader.setVisibility(View.GONE);
                _mainContainer.setVisibility(View.VISIBLE);

                if (ValidationsHelper.isServiceHealthOk(response.body())) {
                    setServiceOkLayout();
                } else {
                    setServiceDownLayout();
                }
            }

            @Override
            public void onFailure(Call<ServiceStatus> call, Throwable t) {
                _loader.setVisibility(View.GONE);
                _mainContainer.setVisibility(View.VISIBLE);
                setServiceDownLayout();
            }
        });
    }

    private void setServiceDownLayout() {
        _description.setText(getString(R.string.service_status, getString(R.string.service_down)));
        _statusIcon.setImageResource(R.drawable.fail_icon);
    }

    private void setServiceOkLayout() {
        _description.setText(getString(R.string.service_status, getString(R.string.service_ok)));
        _statusIcon.setImageResource(R.drawable.check_icon);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent questionsScreenActivity = new Intent(ServiceStatusActivity.this, QuestionsScreenActivity.class);
                finish();
                startActivity(questionsScreenActivity);
            }
        }, SPLASHSCREEN_DURATION);
    }
}
