package pt.andrew.blisschallenge.network.entities;

import java.util.List;

import pt.andrew.blisschallenge.model.Question;
import pt.andrew.blisschallenge.model.ServiceStatus;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by andrew.fernandes on 16/08/2019
 */
public interface ServiceData {

    String QUESTION_LIMIT = "10";

    @GET("/health")
    Call<ServiceStatus> getServiceStatus();

    @GET("/questions?limit=" + QUESTION_LIMIT)
    Call<List<Question>> getQuestions(@Query("offset") int offset, @Query("filter") String filter);

    @POST("/share")
    Call<ServiceStatus> getShareResponse(@Query("destination_email") String email, @Query("content_url") String contentUrl);

    @GET("/questions/{questionId}")
    Call<Question> getQuestion(@Path("questionId") int questionId);

    @PUT("/questions/{questionId}")
    Call<Question> updateQuestion(@Path("questionId") int questionId, @Body Question questionUpdated);
}
