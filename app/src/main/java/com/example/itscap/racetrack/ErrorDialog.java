package com.example.itscap.racetrack;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;


public class ErrorDialog extends DialogFragment {

    private static final String TAG = "debugtag";
    private static final String TITLE_TAG = "titletag";
    private static final String MESSAGE_TAG = "messagetag";

    private IErrorCallback mListner;
    private static ErrorDialog instance = null;
    private String title, message;


    public interface IErrorCallback{

        public void retry();
    }


    public static ErrorDialog getInstance(String title, String message) {

        Bundle bundle = new Bundle();
        bundle.putString(TITLE_TAG, title);
        bundle.putString(MESSAGE_TAG, message);

        if (instance == null)
            instance = new ErrorDialog();

        instance.setArguments(bundle);
        return instance;
    }


    public ErrorDialog() {
        super();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        Bundle bundle = getArguments();
        title = bundle.getString(TITLE_TAG);
        message = bundle.getString(MESSAGE_TAG);

        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("Yep!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListner.retry();
                    }
                })
                .setNegativeButton("Nope", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ErrorDialog.this.getDialog().dismiss();
                    }
                });


        return builder.create();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListner=null;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(getActivity() instanceof IErrorCallback){
            mListner=(IErrorCallback)getActivity();
        }

    }
}
