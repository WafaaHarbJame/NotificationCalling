package com.call.callnotification.Classes;

import android.content.Context;
import android.widget.Toast;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeInfoDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeProgressDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.call.callnotification.R;


public class GlobalData {

    public static final String BetaBaseURL = "https://risteh.com/Cashiers/";
    public static final String BaseURL = BetaBaseURL;
    public static final String ApiURL = BaseURL + "api/";

    public static AwesomeErrorDialog errorDialog;
    public static AwesomeInfoDialog infoDialog;
    static AwesomeProgressDialog progressDialog;

    public static void progressDialog(Context c, int title, int msg) {
        progressDialog = new AwesomeProgressDialog(c);
        progressDialog.setTitle(title).setMessage(msg)
                .setColoredCircle(R.color.colorPrimary)
                .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
                .setCancelable(false);
        progressDialog.show();

    }

    public static void hideProgressDialog() {
        if (progressDialog != null) progressDialog.hide();

    }

    public static void errorDialog(Context c, int title, String msg) {
        errorDialog = new AwesomeErrorDialog(c);
        errorDialog.setTitle(title);
        errorDialog.setMessage(msg);
        errorDialog.setColoredCircle(R.color.dialogErrorBackgroundColor).setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                .setCancelable(true).setButtonBackgroundColor(R.color.dialogErrorBackgroundColor);
        errorDialog.show();

    }

    public static void infoDialog(Context c, String title, String msg) {
        infoDialog = new AwesomeInfoDialog(c);
        infoDialog.setMessage(msg);
        infoDialog.setTitle(title);
        infoDialog.setColoredCircle(R.color.dialogInfoBackgroundColor).setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white).setCancelable(true);
        infoDialog.show();

    }





    public static void Toast(Context context, String msg) {

        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void Toast(Context context, int resId) {

        Toast.makeText(context, context.getString(resId), Toast.LENGTH_SHORT).show();
    }


}
