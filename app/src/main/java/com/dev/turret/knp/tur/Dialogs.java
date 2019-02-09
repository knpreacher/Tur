package com.dev.turret.knp.tur;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;

public class Dialogs {
    public static AlertDialog getWaitDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        ProgressBar v = new ProgressBar(context);
        builder.setView(v);
        return builder.create();
    }

    interface YesNoDialogCallBack{
        void onYes();
        void onNo();
    }

    public static void showYesNoDialog(Context context, String title, String mes, final YesNoDialogCallBack callBack){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setMessage(mes);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                callBack.onYes();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                callBack.onNo();
            }
        });
        builder.create().show();
    }
}
