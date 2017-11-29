package com.fka.rememberwords.data.realm;

import android.widget.CheckBox;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.RealmResults;

import static com.fka.rememberwords.data.realm.DictionaryRealm.KEY_DICTIONARY;
import static com.fka.rememberwords.data.realm.WordRealm.KEY_WORD;

//класс управления БД Realm
public class RealmController {
    private Realm realm;

    public RealmController() {
        realm = Realm.getDefaultInstance();     //открыте базы данных
    }

    //получить все данные из таблицы Dictionary
    public RealmResults<DictionaryRealm> getDictionariesInfo(){
        return realm.where(DictionaryRealm.class).findAll();
    }

    //получить все данные из таблицы Word
    public RealmResults<WordRealm> getWordsInfo(){
        return realm.where(WordRealm.class).findAll();
    }

    //получить все слова Словаря
    public RealmList<WordRealm> getWordsInfoByParent(Class<DictionaryRealm> dictionaryModel, Integer id){

        return realm.where(dictionaryModel).equalTo(KEY_DICTIONARY, id).findFirst().getWords();
    }

    //получить словарь по его id
    public DictionaryRealm getDictionaryById(int id){
        return realm.where(DictionaryRealm.class).equalTo(KEY_DICTIONARY, id).findFirst();
    }

    //получить слово по id
    public WordRealm getWordById (int wordId){
        return realm.where(WordRealm.class).equalTo(KEY_WORD, wordId).findFirst();
    }

    //добавить новый словарь
    public void addDictionary (String dictionaryTitle){
        realm.beginTransaction();
        DictionaryRealm dictionary = realm.createObject(DictionaryRealm.class, getNextDictionaryKey());
        dictionary.setDictionaryTitle(dictionaryTitle);
        realm.commitTransaction();
    }

    //обновить словарь
    public void updateDictionary (int id, String dictionaryTitle){
        realm.beginTransaction();

        //DictionaryRealm dictionary = realm.where(DictionaryRealm.class).equalTo(KEY_DICTIONARY, id).findFirst();
        DictionaryRealm dictionary = getDictionaryById(id);
        dictionary.setDictionaryTitle(dictionaryTitle);

        realm.commitTransaction();
    }

    //удалить словарь
    public void removeDictionary(int id){
        realm.beginTransaction();

        RealmResults<DictionaryRealm> rows = realm.where(DictionaryRealm.class).equalTo(KEY_DICTIONARY, id).findAll();
        rows.deleteAllFromRealm();

        realm.commitTransaction();
    }

    //добавить новое слово
    public void addWord (int dictionaryId, String wordTitle, String translation){
       // DictionaryRealm dictionary = realm.where(DictionaryRealm.class).equalTo(KEY_DICTIONARY, dictionaryId).findFirst();
        DictionaryRealm dictionary = getDictionaryById(dictionaryId);
        realm.beginTransaction();

        WordRealm word = realm.createObject(WordRealm.class, getNextWordKey());
        word.setWordTitle(wordTitle);
        word.setTranslation(translation);
        word.setChecked(false);
        dictionary.getWords().add(word);

        realm.commitTransaction();
    }

    //обновить слово
    public void updateWord (WordRealm word, String wordTitle, String translation, boolean isChecked){
        realm.beginTransaction();

        word.setWordTitle(wordTitle);
        word.setTranslation(translation);
        word.setChecked(isChecked);

        realm.commitTransaction();
    }

    public void setCheckedForWord(WordRealm word, boolean isChecked){
        realm.beginTransaction();
        word.setChecked(isChecked);
        realm.commitTransaction();
    }

    //удалить слово
    public void removeWord(int wordId){
        realm.beginTransaction();

        RealmResults<WordRealm> rows = realm.where(WordRealm.class).equalTo(KEY_WORD, wordId).findAll();
        rows.deleteAllFromRealm();

        realm.commitTransaction();
    }

    //получить следущее значение ключа для словаря
    private int getNextDictionaryKey(){
        Number maxValue = realm.where(DictionaryRealm.class).max(KEY_DICTIONARY);
        int nextKey = (maxValue != null)? maxValue.intValue() + 1 : 0;
        return nextKey + 1;
    }
    //получить следущее значение ключа для слова
    private int getNextWordKey(){
        Number maxValue = realm.where(WordRealm.class).max(KEY_WORD);
        int nextKey = (maxValue != null)? maxValue.intValue() + 1 : 0;
        return nextKey + 1;
    }

    //закрыть БД
    public void closeRealm(){
        if (realm != null){
            realm.close();
        }
    }

}
