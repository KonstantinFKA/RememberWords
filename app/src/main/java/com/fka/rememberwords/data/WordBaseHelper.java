package com.fka.rememberwords.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fka.rememberwords.data.WordDB.WordTable;

/**
 * Created by 074FrantsuzovKA on 03.10.2017.
 */

public class WordBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "wordBase.db";

    //конструктор
    public WordBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    //создание базы
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + WordTable.NAME +
        "(" +
        " _id integer primary key autoincrement, " +
                WordTable.Cols.UUID_DICTIONARY + ", " +
                WordTable.Cols.WORD + ", " +
                WordTable.Cols.TRANSLATION + ", " +
                WordTable.Cols.IS_CHECKED +
        ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
