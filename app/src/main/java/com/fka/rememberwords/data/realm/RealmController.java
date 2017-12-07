package com.fka.rememberwords.data.realm;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

import static com.fka.rememberwords.data.realm.DictionaryRealm.KEY_DICTIONARY;
import static com.fka.rememberwords.data.realm.WordRealm.KEY_IS_CHECKED;
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

    //получить список слов для повторения (RememberFragment)
    public List<WordRealm> getWordsForRemember (){
        ArrayList<WordRealm> list = new ArrayList<>();
        RealmResults<WordRealm> realms = realm.where(WordRealm.class).equalTo(KEY_IS_CHECKED, true).findAll();
        list.addAll(realms);
        return list;
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
        DictionaryRealm dictionary = getDictionaryById(id);
        dictionary.setDictionaryTitle(dictionaryTitle);

        realm.commitTransaction();
    }

    //удалить словарь
    public void removeDictionary(final int id){

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                DictionaryRealm dictionary = realm.where(DictionaryRealm.class).equalTo(KEY_DICTIONARY, id).findFirst();
                RealmList<WordRealm> words = dictionary.getWords();
                if (words == null){
                    dictionary.deleteFromRealm();
                } else {
                    words.deleteAllFromRealm();
                    dictionary.deleteFromRealm();
                }
            }
        });
    }

    //добавить новое слово
    public void addWord (int dictionaryId, String wordTitle, String translation){
       // DictionaryRealm dictionary = realm.where(DictionaryRealm.class).equalTo(KEY_DICTIONARY, dictionaryId).findFirst();
        DictionaryRealm dictionary = getDictionaryById(dictionaryId);
        realm.beginTransaction();

        WordRealm word = realm.createObject(WordRealm.class, getNextWordKey());
        word.setWordTitle(wordTitle);
        word.setTranslation(translation);
        word.setLearn(false);
        word.setRemember(false);
        dictionary.getWords().add(word);

        realm.commitTransaction();
    }

    //обновить слово
    public void updateWord (WordRealm word, String wordTitle, String translation, boolean isChecked){
        realm.beginTransaction();

        word.setWordTitle(wordTitle);
        word.setTranslation(translation);
        word.setLearn(isChecked);

        realm.commitTransaction();
    }

    //устоновить Изучать по параметру Слова
    public void setLearnForWord(WordRealm word, boolean isLearn){
        realm.beginTransaction();
        word.setLearn(isLearn);
        realm.commitTransaction();
    }

    //устоновить Изучать по параметру id
    public void setLearnForWord(int idWord, boolean isChecked){
        WordRealm word = getWordById(idWord);
        setLearnForWord(word, isChecked);
    }

    //устоновить Remember по параметру Слова
    public void setRememberForWord(WordRealm word, boolean isRemember){
        realm.beginTransaction();
        word.setRemember(isRemember);
        realm.commitTransaction();
    }

    //устоновить Remember по параметру id
    public void setRememberForWord(int idWord, boolean isRemember){
        WordRealm word = getWordById(idWord);
        setRememberForWord(word, isRemember);
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
