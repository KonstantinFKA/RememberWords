package com.fka.rememberwords.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fka.rememberwords.data.DictionaryDB.DictionaryTable;
import com.fka.rememberwords.objects.Dictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//класс сингелтон для хранения списка словарей

public class DictionaryLab {
    private static  DictionaryLab dictionaryLab;

    private Context context;
    private SQLiteDatabase database;

    //конструктор
    private DictionaryLab(Context context) {
        this.context = context.getApplicationContext();
        database = new DictionaryBaseHelper(this.context).getWritableDatabase();
    }

    public static DictionaryLab getDictionaryLab(Context context) {
        if (dictionaryLab == null){
            dictionaryLab = new DictionaryLab(context);
        }
        return dictionaryLab;
    }

    //добовление нового словаря
    public void addDictionary (Dictionary dictionary){
        ContentValues values = getContentValues(dictionary);
        database.insert(DictionaryTable.NAME, null, values);
    }

    //удаление словаря
    public void deleteDictionary(Dictionary dictionary){
        String uuidString = dictionary.getId().toString();
        database.delete(DictionaryTable.NAME, DictionaryTable.Cols.UUID + " = ?", new String[]{uuidString});
    }

    //обновление словоря
    public void updateDictionary (Dictionary dictionary){
        String uuidString = dictionary.getId().toString();
        ContentValues values = getContentValues(dictionary);

        database.update(DictionaryTable.NAME, values, DictionaryTable.Cols.UUID + " = ?", new String[]{uuidString});
    }

    public List<Dictionary> getDictionaries() {
        List<Dictionary> dictionaries = new ArrayList<>();

        DictionaryCursorWrapper cursor = queryDictionaries(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                dictionaries.add(cursor.getDictionary());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return dictionaries;
    }

    public Dictionary getDictionary (UUID id){
        DictionaryCursorWrapper cursor = queryDictionaries(DictionaryTable.Cols.UUID + " = ?", new String[]{id.toString()});

        try {
            if (cursor.getCount() == 0){
                return null;
            }

            cursor.moveToFirst();
            return cursor.getDictionary();
        } finally {
            cursor.close();
        }
    }

    private static ContentValues getContentValues (Dictionary dictionary){
        ContentValues values = new ContentValues();
        values.put(DictionaryTable.Cols.UUID, dictionary.getId().toString());
        values.put(DictionaryTable.Cols.TITLE, dictionary.getTitle());

        return values;
    }

    private DictionaryCursorWrapper queryDictionaries (String whereClause, String[] whereArgs){
        Cursor cursor = database.query(
                DictionaryTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new DictionaryCursorWrapper(cursor);
    }
}
