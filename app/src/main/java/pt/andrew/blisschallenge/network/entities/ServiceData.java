package pt.andrew.blisschallenge.network.entities;

import java.util.List;

import pt.andrew.blisschallenge.model.Question;
import pt.andrew.blisschallenge.model.ServiceStatus;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by andrew.fernandes on 16/08/2019
 */
public interface ServiceData {

    @GET("/health")
    Call<ServiceStatus> getServiceStatus();

    @GET("/questions?limit=&offset=&filter=")
    Call<List<Question>> getQuestions();
}
