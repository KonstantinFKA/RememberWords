package com.fka.rememberwords.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fka.rememberwords.data.WordDB.WordTable;
import com.fka.rememberwords.objects.Word;

import java.util.ArrayList;
import java.util.List;

//класс сингелтон для хранения списка слов

public class WordLab {
    private static WordLab wordLab;

    private Context context;
    private SQLiteDatabase database;

    //конструктор
    private WordLab(Context context) {
        this.context = context.getApplicationContext();
        database = new WordBaseHelper(this.context).getWritableDatabase();
    }

    public static WordLab getWordLab(Context context) {
        if (wordLab == null){
            wordLab = new WordLab(context);
        }
        return wordLab;
    }

    //добовление нового слова
    public void addWord (Word word){
        ContentValues values = getContentValues(word);
        database.insert(WordTable.NAME, null, values);
    }

    //удаление слова
    public void deleteWord(Word word){
        String wordString = word.getWord();
        database.delete(WordTable.NAME, WordTable.Cols.WORD + " = ?", new String[]{wordString});
    }

    //обновление слова
    public void updateWord (Word word){
        String wordString = word.getWord();
        ContentValues values = getContentValues(word);

        database.update(WordTable.NAME, values, WordTable.Cols.WORD + " = ?", new String[]{wordString});
    }

    public List<Word> getWords() {
        List<Word> words = new ArrayList<>();

        WordCursorWrapper cursor = queryWord(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                words.add(cursor.getWord());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return words;
    }

    public Word getWord (String word){
        WordCursorWrapper cursor = queryWord(WordTable.Cols.WORD + " = ?", new String[]{word});

        try {
            if (cursor.getCount() == 0){
                return null;
            }

            cursor.moveToFirst();
            return cursor.getWord();
        } finally {
            cursor.close();
        }
    }

    private static ContentValues getContentValues (Word word){
        ContentValues values = new ContentValues();
        values.put(WordTable.Cols.UUID_DICTIONARY, word.getId().toString());
        values.put(WordTable.Cols.WORD, word.getWord());
        values.put(WordTable.Cols.TRANSLATION, word.getTranslation());
        values.put(WordTable.Cols.IS_CHECKED, word.isChecked());

        return values;
    }

    private WordCursorWrapper queryWord (String whereClause, String[] whereArgs){
        Cursor cursor = database.query(
                WordTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new WordCursorWrapper(cursor);
    }
}
