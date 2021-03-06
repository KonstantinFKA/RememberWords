package com.fka.rememberwords.data.realm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

import static com.fka.rememberwords.data.realm.DictionaryRealm.KEY_DICTIONARY;
import static com.fka.rememberwords.data.realm.WordRealm.KEY_DATE_REPEAT;
import static com.fka.rememberwords.data.realm.WordRealm.KEY_IS_LEARN;
import static com.fka.rememberwords.data.realm.WordRealm.KEY_IS_REMEMBER;
import static com.fka.rememberwords.data.realm.WordRealm.KEY_REP_1;
import static com.fka.rememberwords.data.realm.WordRealm.KEY_REP_2;
import static com.fka.rememberwords.data.realm.WordRealm.KEY_TITLE;
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

    //получить список слов для запоминания (RememberFragment)
    public List<WordRealm> getWordsForRemember (){
        ArrayList<WordRealm> list = new ArrayList<>();
        RealmResults<WordRealm> realms = realm.where(WordRealm.class).equalTo(KEY_IS_REMEMBER, true).findAll();
        list.addAll(realms);
        return list;
    }

    //получить список слов для повторения (RepeatFragment)
    public List<WordRealm> getWordsForRepeat (){
        ArrayList<WordRealm> list = new ArrayList<>();
        RealmResults<WordRealm> realms = realm.where(WordRealm.class).equalTo(KEY_IS_LEARN, true)
                .beginGroup()
                .equalTo(KEY_REP_1, false)
                .or().equalTo(KEY_REP_2, false)
                .endGroup()
                .lessThan(KEY_DATE_REPEAT, new Date())
                .findAll();
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
        DictionaryRealm dictionary = getDictionaryById(dictionaryId);
        realm.beginTransaction();

        WordRealm word = realm.createObject(WordRealm.class, getNextWordKey());
        word.setWordTitle(wordTitle);
        word.setTranslation(translation);
        word.setLearn(false);
        word.setRemember(true);
        word.setRep1(false);
        word.setRep2(false);
        word.setDateRepeat(new Date());
        word.setCountRepeat(0);
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

    //установить повторено дла первой версии повтора
    public void setRep1ForWord (WordRealm word, boolean isRepeat){
        realm.beginTransaction();
        word.setRep1(isRepeat);
        realm.commitTransaction();
    }

    //установить повторено дла второй версии повтора
    public void setRep2ForWord (WordRealm word, boolean isRepeat){
        realm.beginTransaction();
        word.setRep2(isRepeat);
        realm.commitTransaction();
    }

    //установка новой даты повторения
    public void setNewDateRepeat(WordRealm word){
        int countRepeat = word.getCountRepeat();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        switch (countRepeat){
            case 0:
                calendar.add(Calendar.MINUTE, 30);      //первое повторение через 30 минут
                break;
            case 1:
                calendar.add(Calendar.DAY_OF_MONTH, 1); //второе повторение через 1 день
                break;
            case 2:
                calendar.add(Calendar.WEEK_OF_MONTH, 2);    //третье повторение через 2 недели
                break;
            case 3:
                calendar.add(Calendar.MONTH, 2);        //четвертое повторение через 2 месяца
                break;
            default:
                calendar.add(Calendar.MONTH, 6);        //повторение по умолчанию через 6 месяцев
        }
        Date newDate = calendar.getTime();

        realm.beginTransaction();
        word.setCountRepeat(countRepeat + 1);
        word.setDateRepeat(newDate);
        word.setRep1(false);
        word.setRep2(false);
        if (countRepeat >= 4){
            word.setLearn(false);
            word.setRemember(false);
            word.setDateRepeat(new Date());
            word.setCountRepeat(0);
        }
        realm.commitTransaction();
    }

    //получить следущее значение ключа для словаря
    private int getNextDictionaryKey(){
        Number maxValue = realm.where(DictionaryRealm.class).max(KEY_DICTIONARY);
        int nextKey = (maxValue != null)? maxValue.intValue() + 1 : 0;
        return nextKey;
    }
    //получить следущее значение ключа для слова
    private int getNextWordKey(){
        Number maxValue = realm.where(WordRealm.class).max(KEY_WORD);
        int nextKey = (maxValue != null)? maxValue.intValue() + 1 : 0;
        return nextKey;
    }

    //закрыть БД
    public void closeRealm(){
        if (realm != null){
            realm.close();
        }
    }

}
