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

//AlertDialog добовления словоря

public class NewDictionaryFragment extends DialogFragment {

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
                        new RealmController().addDictionary(title);
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
