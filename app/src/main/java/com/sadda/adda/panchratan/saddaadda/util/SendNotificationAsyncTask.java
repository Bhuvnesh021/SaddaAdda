package com.sadda.adda.panchratan.saddaadda.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by user on 25-07-2017.
 */
public class SendNotificationAsyncTask extends AsyncTask<Void,Void,Void> {
    ProgressDialog progressDialog;
    private Context context;

    public SendNotificationAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
    }

    @Override
    protected Void doInBackground(Void... params) {
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if((progressDialog!=null && progressDialog.isShowing())){
            progressDialog.dismiss();
        }

    }
}
