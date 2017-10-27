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

import java.util.UUID;

/**
 * Created by 074FrantsuzovKA on 05.10.2017.
 */

public class RenameDictionaryFragment extends DialogFragment {
    public static final String EXTRA_TITLE = "com.fka.rememberwords.title";
    public static final String EXTRA_RENAME_UUID = "com.fka.rememberwords.id.rename";
    private static final String ARG_TITLE = "title";
    private static final String ARG_UUID_RENAME = "uuid_rename";

    private TextInputLayout titleInputLayout;

    public static RenameDictionaryFragment newInstance(String title, UUID  id) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_TITLE, title);
        args.putSerializable(ARG_UUID_RENAME, id);

        RenameDictionaryFragment fragment = new RenameDictionaryFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String title = (String) getArguments().getSerializable(ARG_TITLE);
        final UUID id = (UUID) getArguments().getSerializable(ARG_UUID_RENAME);

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
                        sendResult(Activity.RESULT_OK, id, newTitle);

                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }

    private void sendResult(int resultCode, UUID id, String title) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_RENAME_UUID, id);
        intent.putExtra(EXTRA_TITLE, title);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
