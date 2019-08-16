package pt.andrew.blisschallenge.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by andrew.fernandes on 16/08/2019
 */
public class Question {

    @SerializedName("id")
    private int _questionId;

    @SerializedName("question")
    private String _questionTitle;

    @SerializedName("image_url")
    private String _questionImageUrl;

    @SerializedName("thumb_url")
    private String _questionThumbnailUrl;

    @SerializedName("published_at")
    private String _questionPublishDate;

    @SerializedName("choices")
    private List<Choice> _questionChoiceList;

    public Question(int questionId, String questionTitle, String questionImageUrl,
                    String questionThumbnailUrl, String questionPublishDate, List<Choice> questionChoiceList) {
        this._questionId = questionId;
        this._questionTitle = questionTitle;
        this._questionImageUrl = questionImageUrl;
        this._questionThumbnailUrl = questionThumbnailUrl;
        this._questionPublishDate = questionPublishDate;
        this._questionChoiceList = questionChoiceList;
    }
}
