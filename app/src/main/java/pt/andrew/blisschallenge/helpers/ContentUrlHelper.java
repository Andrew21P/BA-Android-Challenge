package pt.andrew.blisschallenge.helpers;

/**
 * Created by andrew.fernandes on 18/08/2019
 */
public class ContentUrlHelper {

    private static String questionUrl = "blissrecruitment://questions?";

    public static String getQuestionsContentUrl(String limit, String offset, String filter) {
        return questionUrl + "limit=" + limit + "&offset=" + offset + "&filter=" + filter;
    }
}
