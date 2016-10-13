package my.ilpsdk.sms2android.Util;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;

/**
 * Created by Nurzaly on 13/09/2016.
 */
public class MyProgressbar {
    private static ProgressDialog pDialog;
    private static String str = "Loading...";

    public MyProgressbar(View view, String str) {
        this.str = str;
        pDialog = new ProgressDialog(view.getContext());
        pDialog.setMessage(this.str);
        pDialog.setCancelable(false);
        showProgressDialog();
    }

    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    public void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }

}
