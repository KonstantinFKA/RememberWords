package com.fka.rememberwords.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.fka.rememberwords.data.realm.RealmController;

public class RepeatWordService extends IntentService {
    private static final String SERVICE_NAME = "RepeatWordService";


    public RepeatWordService() {
        super(SERVICE_NAME);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        int repeatWordCount = new RealmController().getWordsForRepeat().size();
        Log.i("service", "Слов для повторения " + repeatWordCount);
    }

}
