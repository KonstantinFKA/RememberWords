package com.fka.rememberwords;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.fka.rememberwords.data.realm.RealmController;

//главная Activity приложения, управляющая всеми фрагментами

public class RememberWordsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remember_words);

        //запуск фрагмента DictionaryListFragment
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = new DictionaryListFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }

    @Override
    protected void onDestroy() {
        new RealmController().closeRealm(); //закрыть БД Realm при завершении приложения
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FragmentManager manager = getSupportFragmentManager();
        //manager.findFragmentByTag()
        manager.popBackStack("dictionary_fragment_stack", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
