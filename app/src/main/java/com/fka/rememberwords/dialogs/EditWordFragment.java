package com.fka.rememberwords.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.fka.rememberwords.R;
import com.fka.rememberwords.data.realm.RealmController;
import com.fka.rememberwords.data.realm.WordRealm;

/**
 * Created by 074FrantsuzovKA on 05.10.2017.
 */

public class EditWordFragment extends DialogFragment {
    private static final String ARG_WORD_ID = "word_id";

    private TextInputLayout titleInputLayout;
    private TextInputLayout translationInputLayout;

    public static EditWordFragment newInstance(int wordId) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_WORD_ID, wordId);

        EditWordFragment fragment = new EditWordFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final int wordId = getArguments().getInt(ARG_WORD_ID);
        final WordRealm word = new RealmController().getWordById(wordId);

        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_edit_word, null);
        titleInputLayout = view.findViewById(R.id.new_word_input_layout);
        titleInputLayout.getEditText().setText(word.getWordTitle());
        translationInputLayout = view.findViewById(R.id.new_translation_input_layout);
        translationInputLayout.getEditText().setText(word.getTranslation());

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.edit_word_title_alertdialog)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newTitle = titleInputLayout.getEditText().getText().toString();
                        String newTranslation = translationInputLayout.getEditText().getText().toString();
                        new RealmController().updateWord(word, newTitle, newTranslation, word.isChecked());
                        sendResult(Activity.RESULT_OK);

                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null) {
            return;
        }
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, null);
    }
}
