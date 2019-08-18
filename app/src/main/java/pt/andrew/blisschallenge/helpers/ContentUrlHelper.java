package pt.andrew.blisschallenge.helpers;

/**
 * Created by andrew.fernandes on 18/08/2019
 */
public class ContentUrlHelper {

    private static String questionListUrl = "blissrecruitment://questions?";
    private static String questionUrl = "question_id=";

    public static String getQuestionListContentUrl(String limit, String offset, String filter) {
        return questionListUrl + "limit=" + limit + "&offset=" + offset + "&filter=" + filter;
    }

    public static String getQuestionContentUrl(int questionId) {
        return questionListUrl + questionUrl + questionId;
    }
}
