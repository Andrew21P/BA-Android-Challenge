package pt.andrew.blisschallenge.helpers;

import pt.andrew.blisschallenge.model.ServiceStatus;

/**
 * Created by andrew.fernandes on 16/08/2019
 */
public class ValidationHelpers {

    //Service Response when health is OK
    private final static String STATUS_OK = "OK";

    public static boolean isServiceHealthOk(ServiceStatus response) {
        return response != null && response.getStatus() != null && response.getStatus().equals(STATUS_OK);
    }
}
