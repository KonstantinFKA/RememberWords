package com.fka.rememberwords.data.realm;

import android.widget.CheckBox;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by 074FrantsuzovKA on 21.11.2017.
 */

public class WordRealm extends RealmObject {

    public static final String KEY_WORD = "wordId";

    @PrimaryKey
    private int wordId;
    @Required
    private String wordTitle;
    private String translation;
    private boolean isChecked;

    public int getWordId() {
        return wordId;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }

    public String getWordTitle() {
        return wordTitle;
    }

    public void setWordTitle(String wordTitle) {
        this.wordTitle = wordTitle;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }


    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
