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
import com.fka.rememberwords.objects.Word;

import java.util.UUID;

//AlertDialog удаления словаря

public class DeleteWordFragment extends DialogFragment {
    public static final String EXTRA_DEL_WORD = "com.fka.rememberwords.id.delete.word";
    private static final String ARG_DEL_WORD = "word";

    public static DeleteWordFragment newInstance(String word) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_DEL_WORD, word);

        DeleteWordFragment fragment = new DeleteWordFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String word = (String) getArguments().getSerializable(ARG_DEL_WORD);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.delete_word_text_alertdialog)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendResult(Activity.RESULT_OK, word);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }

    private void sendResult(int resultCode, String word) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DEL_WORD, word);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
