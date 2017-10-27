package com.fka.rememberwords.data;

/**
 * Created by 074FrantsuzovKA on 03.10.2017.
 */

public class DictionaryDB {

    //класс описания таблицы Словаря
    public static final class DictionaryTable {
        public static final String NAME = "dictionaries"; //наименование таблицы

        //наименование столбцов таблицы
        public static final class Cols {
            public static final String UUID = "id";
            public static final String TITLE = "title";
        }
    }
}
