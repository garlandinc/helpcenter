package com.garland.helpcenter.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.garland.helpcenter.MainActivity;

/**
 * Created by lemon on 10/29/2017.
 */

public class DialogInsert extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle(MainActivity.TITLE).setMessage(MainActivity.MESSAGE);
        builder.setNegativeButton("Ok",null);
        return builder.create();
    }
}
