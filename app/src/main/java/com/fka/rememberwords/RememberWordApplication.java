package com.fka.rememberwords;

import android.app.Application;

import io.realm.Realm;

//создаем свой Application для инициализации Realm

public class RememberWordApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
