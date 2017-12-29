package com.fka.rememberwords.data.realm;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;


public class WordRealm extends RealmObject {

    public static final String KEY_WORD = "wordId";
    public static final String KEY_TITLE = "wordTitle";
    public static final String KEY_IS_REMEMBER = "isRemember";
    public static final String KEY_IS_LEARN = "isLearn";
    public static final String KEY_DATE_REPEAT = "dateRepeat";
    public static final String KEY_REP_1 = "rep1";
    public static final String KEY_REP_2 = "rep2";

    @PrimaryKey
    private int wordId;
    @Required@Index
    private String wordTitle;   //слово
    private String translation; //перевод
    private boolean isLearn;    //на изучении
    private boolean isRemember; //на запоминании

    private boolean rep1;       //
    private boolean rep2;       //варианты повторения

    private Date dateRepeat;    //дата повторения слова
    private int countRepeat;    //количество поворений




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

    public boolean isRep1() {
        return rep1;
    }

    public void setRep1(boolean rep1) {
        this.rep1 = rep1;
    }

    public boolean isRep2() {
        return rep2;
    }

    public void setRep2(boolean rep2) {
        this.rep2 = rep2;
    }

    public Date getDateRepeat() {
        return dateRepeat;
    }

    public void setDateRepeat(Date dateRepeat) {
        this.dateRepeat = dateRepeat;
    }

    public int getCountRepeat() {
        return countRepeat;
    }

    public void setCountRepeat(int countRepeat) {
        this.countRepeat = countRepeat;
    }
}
