package my.ilpsdk.sms2android.Util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import my.ilpsdk.sms2android.Interface.MyDialogOkClick;

/**
 * Created by Nurzaly on 20/08/2016.
 */
public class MyDialog {
    public static void msgbox(Context context, String title, String msg, final MyDialogOkClick callback){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);
        alertDialog.setCancelable(false);

        // Setting Dialog Message
        alertDialog.setMessage(msg);

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.tick);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if(callback != null){
                    callback.onOkClick();
                }
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
    public static void msgbox2(Context context, String title, String msg, final MyDialogOkClick callback){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(title);
        builder.setMessage(msg);

        // Add the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                callback.onOkClick();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
