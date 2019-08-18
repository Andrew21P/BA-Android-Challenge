package pt.andrew.blisschallenge.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by andrew.fernandes on 16/08/2019
 */
public class RetrofitInstance {

    /**
     * Base URL for all service requests
     */
    private static final String BLISS_BASE_URL = "https://private-anon-2942175374-blissrecruitmentapi.apiary-mock.com";

    private static Retrofit _retrofitService;

    public static Retrofit getRetrofitServiceInstance() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        return _retrofitService != null ? _retrofitService : new retrofit2.Retrofit.Builder()
                .baseUrl(BLISS_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
