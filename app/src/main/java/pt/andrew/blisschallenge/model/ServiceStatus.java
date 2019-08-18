package pt.andrew.blisschallenge.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by andrew.fernandes on 16/08/2019
 */
public class ServiceStatus {

    @SerializedName("status")
    private String _status;

    public ServiceStatus(String status) {
        this._status = status;
    }

    public String getStatus() {
        return _status;
    }
}
