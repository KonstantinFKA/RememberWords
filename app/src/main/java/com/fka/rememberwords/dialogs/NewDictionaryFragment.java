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

//AlertDialog добовления словоря

public class NewDictionaryFragment extends DialogFragment {
    public static final String EXTRA_TITLE = "com.fka.rememberwords.title";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_new_dictionary, null);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.new_dictionary_title_alertdialog)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String title = ((TextInputLayout) view.findViewById(R.id.title_name_input_layout)).getEditText().getText().toString();
                        sendResult(Activity.RESULT_OK, title);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }

    private void sendResult (int resultCode, String title) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_TITLE, title);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
