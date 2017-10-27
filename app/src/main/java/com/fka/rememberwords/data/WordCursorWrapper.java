package com.fka.rememberwords.data;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.fka.rememberwords.objects.Word;
import com.fka.rememberwords.data.WordDB.WordTable;

import java.util.UUID;

/**
 * Created by 074FrantsuzovKA on 03.10.2017.
 */

public class WordCursorWrapper extends CursorWrapper {

    public WordCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Word getWord() {
        String uuidString = getString(getColumnIndex(WordTable.Cols.UUID_DICTIONARY));
        String wordString = getString(getColumnIndex(WordTable.Cols.WORD));
        String translation = getString(getColumnIndex(WordTable.Cols.TRANSLATION));
        int isChecked = getInt(getColumnIndex(WordTable.Cols.IS_CHECKED));

        Word word = new Word(UUID.fromString(uuidString), wordString, translation);
        word.setWord(wordString);
        word.setTranslation(translation);
        word.setId(UUID.fromString(uuidString));
        word.setChecked(isChecked != 0);

        return word;
    }
}
