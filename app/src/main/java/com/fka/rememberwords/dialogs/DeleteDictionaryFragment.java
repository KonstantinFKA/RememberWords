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

import java.util.UUID;

//AlertDialog удаления словаря

public class DeleteDictionaryFragment extends DialogFragment {
    public static final String EXTRA_DEL_UUID = "com.fka.rememberwords.id.delete";
    private static final String ARG_DEL_UUID = "uuid";

    public static DeleteDictionaryFragment newInstance(UUID id) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_DEL_UUID, id);

        DeleteDictionaryFragment fragment = new DeleteDictionaryFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final UUID id = (UUID) getArguments().getSerializable(ARG_DEL_UUID);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.delete_dictionary_text_alertdialog)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendResult(Activity.RESULT_OK, id);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }

    private void sendResult(int resultCode, UUID id) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DEL_UUID, id);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
