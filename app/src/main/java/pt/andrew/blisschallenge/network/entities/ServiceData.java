package pt.andrew.blisschallenge.network.entities;

import pt.andrew.blisschallenge.model.ServiceStatus;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by andrew.fernandes on 16/08/2019
 */
public interface ServiceData {

    @GET("/health")
    Call<ServiceStatus> getServiceStatus();
}
