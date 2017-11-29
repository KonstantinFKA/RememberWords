package com.fka.rememberwords.data.realm;

import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by 074FrantsuzovKA on 21.11.2017.
 */

public class DictionaryRealm extends RealmObject{

    public static final String KEY_DICTIONARY = "id";

    @PrimaryKey
    private int id;
    private String dictionaryTitle;
    private RealmList<WordRealm> words;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDictionaryTitle() {
        return dictionaryTitle;
    }

    public void setDictionaryTitle(String dictionaryTitle) {
        this.dictionaryTitle = dictionaryTitle;
    }

    public RealmList<WordRealm> getWords() {
        return words;
    }

    public void setWords(RealmList<WordRealm> words) {
        this.words = words;
    }
}
