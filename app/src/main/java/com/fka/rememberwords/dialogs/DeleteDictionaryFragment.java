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

public class DeleteDictionaryFragment extends DialogFragment {
  //  public static final String EXTRA_DEL_UUID = "com.fka.rememberwords.id.delete";
    private static final String ARG_DEL_ID = "id";

    public static DeleteDictionaryFragment newInstance(int id) {

        Bundle args = new Bundle();
        args.putInt(ARG_DEL_ID, id);

        DeleteDictionaryFragment fragment = new DeleteDictionaryFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final int id = getArguments().getInt(ARG_DEL_ID);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.delete_dictionary_text_alertdialog)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new RealmController().removeDictionary(id);
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
