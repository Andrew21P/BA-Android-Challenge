package pt.andrew.blisschallenge.helpers;

/**
 * Created by andrew.fernandes on 18/08/2019
 */
public class ContentUrlHelper {

    /**
     * Helper for build the custom URL Content parameter in share option
     */

    private static String questionListUrl = "blissrecruitment://questions?";
    private static String questionUrl = "question_id=";

    public static String getQuestionListContentUrl(String limit, String offset, String filter) {
        return questionListUrl + "limit=" + limit + "&offset=" + offset + "&filter=" + filter;
    }

    public static String getQuestionContentUrl(int questionId) {
        return questionListUrl + questionUrl + questionId;
    }
}
