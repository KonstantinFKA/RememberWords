package com.fka.rememberwords.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.fka.rememberwords.R;
import com.fka.rememberwords.data.realm.RealmController;

import java.util.UUID;

//AlertDialog удаления словаря

public class DeleteWordFragment extends DialogFragment {
    private static final String ARG_DEL_WORD = "word";

    public static DeleteWordFragment newInstance(int wordId) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_DEL_WORD, wordId);

        DeleteWordFragment fragment = new DeleteWordFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final int wordId = getArguments().getInt(ARG_DEL_WORD);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.delete_word_text_alertdialog)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new RealmController().removeWord(wordId);
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
