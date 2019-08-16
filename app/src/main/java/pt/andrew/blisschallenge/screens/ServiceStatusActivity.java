package pt.andrew.blisschallenge.screens;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import pt.andrew.blisschallenge.R;
import pt.andrew.blisschallenge.helpers.ValidationHelpers;
import pt.andrew.blisschallenge.model.ServiceStatus;
import pt.andrew.blisschallenge.network.RetrofitInstance;
import pt.andrew.blisschallenge.network.entities.ServiceData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceStatusActivity extends AppCompatActivity {

    @BindView(R.id.serviceHealthLoaderContainer)
    View _loader;
    @BindView(R.id.serviceHealthTitle)
    TextView _title;

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

                Toast.makeText(ServiceStatusActivity.this, "Service is OK!", Toast.LENGTH_SHORT).show();

                if (ValidationHelpers.isServiceHealthOk(response.body())) {
                    _title.setText("It's OK!");
                } else {
                    _title.setText("Service Down!");
                }
            }

            @Override
            public void onFailure(Call<ServiceStatus> call, Throwable t) {
                Toast.makeText(ServiceStatusActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                _title.setText("Service Down!");
            }
        });


    }
}
