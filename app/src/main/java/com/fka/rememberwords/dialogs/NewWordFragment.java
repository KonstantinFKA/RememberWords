package com.fka.rememberwords.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.fka.rememberwords.R;
import com.fka.rememberwords.data.realm.RealmController;

import java.util.List;

//AlertDialog добовления словоря

public class NewWordFragment extends DialogFragment {
    public static final String ARG_ID_DICTIONARY = "dictionaryId";

    public static NewWordFragment newInstance(int dictionaryId) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_ID_DICTIONARY, dictionaryId);

        NewWordFragment fragment = new NewWordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final int dictionaryId = getArguments().getInt(ARG_ID_DICTIONARY);

        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_new_word, null);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.new_word_title_alertdialog)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        String word = wordInputLayout.getEditText().getText().toString();
//                        List<Word> words = WordLab.getWordLab(getActivity()).getWords();
//
//                        for (int y = 0; y < words.size(); y++) {
//                            String wordString = words.get(y).getWord();
//                            if (wordString.equals(word)) {
//                                Toast.makeText(getActivity(), "Слово уже существует", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                        }
                        String title = ((TextInputLayout) view.findViewById(R.id.word_input_layout)).getEditText().getText().toString();
                        String translation = ((TextInputLayout) view.findViewById(R.id.translation_input_layout)).getEditText().getText().toString();
                        new RealmController().addWord(dictionaryId, title, translation);      //создать слово
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }

    private void sendResult (int resultCode) {
        if (getTargetFragment() == null) {
            return;
        }

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, null);
    }
}
