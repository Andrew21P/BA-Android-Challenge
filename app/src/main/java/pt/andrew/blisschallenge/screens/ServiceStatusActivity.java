package pt.andrew.blisschallenge.screens;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @BindView(R.id.test_textview)
    TextView _testTextView;

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

                Toast.makeText(ServiceStatusActivity.this, "Service is OK!", Toast.LENGTH_SHORT).show();

                if (ValidationHelpers.isServiceHealthOk(response.body())) {
                    _testTextView.setText("It's OK!");
                } else {
                    _testTextView.setText("Service Down!");
                }


            }

            @Override
            public void onFailure(Call<ServiceStatus> call, Throwable t) {
                Toast.makeText(ServiceStatusActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                _testTextView.setText("Service Down!");
            }
        });


    }
}
