package com.fka.rememberwords.data;

/**
 * Created by 074FrantsuzovKA on 03.10.2017.
 */

public class WordDB {

    //класс описания таблицы Словаря
    public static final class WordTable {
        public static final String NAME = "words"; //наименование таблицы

        //наименование столбцов таблицы
        public static final class Cols {
            public static final String UUID_DICTIONARY = "id_dictionary";
            public static final String WORD = "title";
            public static final String TRANSLATION = "translation";
            public static final String IS_CHECKED = "is_checked";
        }
    }
}
