package com.fka.rememberwords.data;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.fka.rememberwords.objects.Dictionary;
import com.fka.rememberwords.data.DictionaryDB.DictionaryTable;

import java.util.UUID;

/**
 * Created by 074FrantsuzovKA on 03.10.2017.
 */

public class DictionaryCursorWrapper extends CursorWrapper {

    public DictionaryCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Dictionary getDictionary() {
        String uuidString = getString(getColumnIndex(DictionaryTable.Cols.UUID));
        String title = getString(getColumnIndex(DictionaryTable.Cols.TITLE));

        Dictionary dictionary = new Dictionary(UUID.fromString(uuidString));
        dictionary.setTitle(title);

        return dictionary;
    }
}
