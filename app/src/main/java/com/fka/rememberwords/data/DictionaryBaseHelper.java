package com.fka.rememberwords.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fka.rememberwords.data.DictionaryDB.DictionaryTable;

/**
 * Created by 074FrantsuzovKA on 03.10.2017.
 */

public class DictionaryBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "dictionaryBase.db";

    //конструктор
    public DictionaryBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    //создание базы
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + DictionaryTable.NAME +
        "(" +
        " _id integer primary key autoincrement, " +
        DictionaryTable.Cols.UUID + ", " +
        DictionaryTable.Cols.TITLE +
        ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
