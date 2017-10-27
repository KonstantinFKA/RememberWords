package com.fka.rememberwords.objects;

import java.util.UUID;

/**
 * Created by 074FrantsuzovKA on 03.10.2017.
 */

public class Word {

    private UUID dictionaryId;
    private String word;
    private String translation;
    private boolean isChecked;

    public Word(UUID dictionaryId, String title, String translation) {
        this.dictionaryId = dictionaryId;
        this.word = title;
        this.translation = translation;
        this.isChecked = false;
    }

    public UUID getId() {
        return dictionaryId;
    }

    public void setId(UUID id) {
        this.dictionaryId = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
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

