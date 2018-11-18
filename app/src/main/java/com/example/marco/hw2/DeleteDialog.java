package com.example.marco.hw2;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

public class DeleteDialog extends DialogFragment {

    public interface NoticeDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativelick(DialogFragment dialog);
    };
    NoticeDialogListener mListener;

    public DeleteDialog(){};

    static DeleteDialog newInstance(){
        return new DeleteDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getResources().getString(R.string.dialog_title)).setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onDialogPositiveClick(DeleteDialog.this);

            }
        })
                .setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onDialogNegativelick(DeleteDialog.this);
                    }
                });
        return builder.create();
    }

    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (NoticeDialogListener) activity;
        }
        catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + "must implement NoticeDialogListener");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = getActivity();

        try {
            mListener = (NoticeDialogListener) activity;
        }
        catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + "must implement NoticeDialogListener");
        }
    }

}
