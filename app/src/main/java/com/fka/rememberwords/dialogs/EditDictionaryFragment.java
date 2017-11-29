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

import com.fka.rememberwords.R;
import com.fka.rememberwords.data.realm.RealmController;

import java.util.UUID;

/**
 * Created by 074FrantsuzovKA on 05.10.2017.
 */

public class EditDictionaryFragment extends DialogFragment {
    private static final String ARG_TITLE = "title";
    private static final String ARG_ID_RENAME = "id_rename";

    private TextInputLayout titleInputLayout;

    public static EditDictionaryFragment newInstance(String title, int  id) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_TITLE, title);
        args.putSerializable(ARG_ID_RENAME, id);

        EditDictionaryFragment fragment = new EditDictionaryFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String title = (String) getArguments().getSerializable(ARG_TITLE);
        final int id = getArguments().getInt(ARG_ID_RENAME);

        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_rename_dictionary, null);
        titleInputLayout = view.findViewById(R.id.title_rename_input_layout);
        titleInputLayout.getEditText().setText(title);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.rename_dictionary_title_alertdialog)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newTitle = titleInputLayout.getEditText().getText().toString();
                        new RealmController().updateDictionary(id, newTitle);
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
