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
import com.fka.rememberwords.objects.Word;
import com.fka.rememberwords.labs.WordLab;

import java.util.List;

//AlertDialog добовления словоря

public class NewWordFragment extends DialogFragment {
    public static final String EXTRA_TITLE = "com.fka.rememberwords.title";
    public static final String EXTRA_TRANSLATION = "com.fka.rememberwords.translation";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_new_word, null);
        final TextInputLayout wordInputLayout = (TextInputLayout) view.findViewById(R.id.word_input_layout);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.new_word_title_alertdialog)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String word = wordInputLayout.getEditText().getText().toString();
                        List<Word> words = WordLab.getWordLab(getActivity()).getWords();

                        for (int y = 0; y < words.size(); y++) {
                            String wordString = words.get(y).getWord();
                            if (wordString.equals(word)) {
                                Toast.makeText(getActivity(), "Слово уже существует", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        String title = ((TextInputLayout) view.findViewById(R.id.word_input_layout)).getEditText().getText().toString();
                        String translation = ((TextInputLayout) view.findViewById(R.id.translation_input_layout)).getEditText().getText().toString();
                        sendResult(Activity.RESULT_OK, title, translation);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }

    private void sendResult (int resultCode, String title, String translation) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_TRANSLATION, translation);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
