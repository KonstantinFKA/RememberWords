package com.fka.rememberwords;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;

import com.fka.rememberwords.data.realm.RealmController;
import com.fka.rememberwords.data.realm.WordRealm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


/**
 * Created by 074FrantsuzovKA on 08.12.2017.
 */

public class RepeatFragment extends Fragment {
    private static final int REPEAT_FRAGMENT_V1 = 0;
    private static final int REPEAT_FRAGMENT_V2 = 1;

    private int wrongCount;
    private TextView wordForRepeat;
    private EditText translationForRepeat;
    private GridLayout gridLayoutRepeat;
    private TextView repeatTextView;
    private FloatingActionButton okRepeatButton;
    private List<WordRealm> words;
    private View view;
    private boolean isCorrectWord = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        words = new RealmController().getWordsForRepeat();
        final ColorStateList green = getResources().getColorStateList(R.color.repeat_button_green);
        final ColorStateList red = getResources().getColorStateList(R.color.repeat_button_red);
        final WordRealm word = words.get(new Random().nextInt(words.size()));
        final String wordTranslation = word.getTranslation();
        final String wordTitle = word.getWordTitle();
        //final int randomFragment = new Random().nextInt(2);
        final int randomFragment = getRandomFragmentInt(word);

        //слушатель изменения текста
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (randomFragment == REPEAT_FRAGMENT_V2) {
                    if (translationForRepeat.getText().toString().equals(wordTranslation)) {
                        okRepeatButton.setBackgroundTintList(green);
                        okRepeatButton.setImageResource(R.drawable.ic_check_24dp);
                        isCorrectWord = true;
                    } else {
                        okRepeatButton.setBackgroundTintList(red);
                        okRepeatButton.setImageResource(R.drawable.ic_close_24dp);
                        isCorrectWord = false;
                    }
                }

                if (randomFragment == REPEAT_FRAGMENT_V1) {
                    if (wordTitle.equals(repeatTextView.getText().toString())) {
                        okRepeatButton.setBackgroundTintList(green);
                        okRepeatButton.setImageResource(R.drawable.ic_check_24dp);
                        isCorrectWord = true;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        if (randomFragment == REPEAT_FRAGMENT_V1) {
            view = inflater.inflate(R.layout.fargment_repeat_v1, container, false);

            wordForRepeat = (TextView) view.findViewById(R.id.word_for_repeat1);
            wordForRepeat.setText(wordTranslation);
            repeatTextView = (TextView) view.findViewById(R.id.repeat_edit_text1);
            repeatTextView.addTextChangedListener(textWatcher);

            gridLayoutRepeat = (GridLayout) view.findViewById(R.id.grid_layout_repeat1);
            getCharsButton(wordTitle, gridLayoutRepeat);

            okRepeatButton = (FloatingActionButton) view.findViewById(R.id.ok_repeat_button_v1);
            okRepeatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (wrongCount < 2 && isCorrectWord){
//                        new RealmController().setRep1ForWord(word, true);
//                    }
//                    FragmentManager manager = getFragmentManager();
//                    manager.beginTransaction()
//                            .replace(R.id.fragment_container, new RepeatFragment())
//                            .addToBackStack(null)
//                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//                            .commit();
                    nextRepeatWord(word, REPEAT_FRAGMENT_V1);
                }
            });
        } else if (randomFragment == REPEAT_FRAGMENT_V2){

            view = inflater.inflate(R.layout.fargment_repeat_v2, container, false);

            wordForRepeat = (TextView) view.findViewById(R.id.word_for_repeat2);
            wordForRepeat.setText(wordTitle);
            translationForRepeat = (EditText) view.findViewById(R.id.translation_for_repeat2);
            translationForRepeat.addTextChangedListener(textWatcher);

            okRepeatButton = (FloatingActionButton) view.findViewById(R.id.ok_repeat_button_v2);
            okRepeatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    FragmentManager manager = getFragmentManager();
//                    manager.beginTransaction()
//                            .replace(R.id.fragment_container, new RepeatFragment())
//                            .addToBackStack(null)
//                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//                            .commit();
                    nextRepeatWord(word, REPEAT_FRAGMENT_V2);
                }
            });
        }

        return view;
    }

    //получить кнопки с буквами
    private void getCharsButton (String word, GridLayout gridLayout){
        final char[] chars = word.toCharArray();
        int charsCount = chars.length;
        ArrayList<Character> characters = new ArrayList<>();
        for (int i = 0; i < charsCount; i++) {
            characters.add(chars[i]);
        }
        Collections.shuffle(characters);

        for (int i = 0; i < charsCount; i++) {
            final Button button = new Button(getActivity());
            button.setText(String.valueOf(characters.get(i)));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = repeatTextView.length();
                    String ch = String.valueOf(chars[position]);
                    if (ch.equals(button.getText())) {
                        repeatTextView.append(button.getText());
                        button.setEnabled(false);
                    } else {
                        wrongCount++;
                    }
                }
            });
            gridLayout.addView(button);
        }
    }

    private void nextRepeatWord(WordRealm word, int repNumber){
        if (wrongCount <= 2 && isCorrectWord){
            if (repNumber == REPEAT_FRAGMENT_V1) {
                new RealmController().setRep1ForWord(word, true);
            } else if (repNumber == REPEAT_FRAGMENT_V2){
                new RealmController().setRep2ForWord(word, true);
            }
        }
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction()
                .replace(R.id.fragment_container, new RepeatFragment())
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    private int getRandomFragmentInt(WordRealm word){
        if (word.isRep1() && !word.isRep2()){
            return REPEAT_FRAGMENT_V2;
        } else if (word.isRep2() && !word.isRep1()){
            return REPEAT_FRAGMENT_V1;
        } else {
            return new Random().nextInt(2);
        }
    }
}
