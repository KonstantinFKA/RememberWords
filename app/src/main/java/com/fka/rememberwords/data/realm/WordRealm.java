package com.fka.rememberwords.data.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by 074FrantsuzovKA on 21.11.2017.
 */

public class WordRealm extends RealmObject {

    public static final String KEY_WORD = "wordId";
    public static final String KEY_IS_CHECKED = "isRemember";

    @PrimaryKey
    private int wordId;
    @Required
    private String wordTitle;
    private String translation;
    private boolean isLearn;
    private boolean isRemember;

    public int getWordId() {
        return wordId;
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


    public boolean isLearn() {
        return isLearn;
    }

    public void setLearn(boolean learn) {
        isLearn = learn;
    }

    public boolean isRemember() {
        return isRemember;
    }

    public void setRemember(boolean remember) {
        isRemember = remember;
    }
}
