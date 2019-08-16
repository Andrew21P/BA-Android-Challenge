package pt.andrew.blisschallenge.network.entities;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by andrew.fernandes on 16/08/2019
 */
public interface serviceStatusEntity {

    @GET("/health")
    String getServiceStatus;
}
