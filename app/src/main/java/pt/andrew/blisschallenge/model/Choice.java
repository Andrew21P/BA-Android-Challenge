package pt.andrew.blisschallenge.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by andrew.fernandes on 16/08/2019
 */
public class Choice {

    @SerializedName("choice")
    private String _choiceName;

    @SerializedName("votes")
    private int _choiceVotes;

    public Choice(String choiceName, int choiceVotes) {
        this._choiceName = choiceName;
        this._choiceVotes = choiceVotes;
    }

    public String getChoiceName() {
        return _choiceName;
    }

    public int getChoiceVotes() {
        return _choiceVotes;
    }
}
