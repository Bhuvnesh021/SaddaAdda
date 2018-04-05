package com.sadda.adda.panchratan.saddaadda.util;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by user on 03-07-2017.
 */
public class ReqLoaderAsyncTask extends AsyncTask<Void,Void,Void> {
    private static String REQUEST_TYPE;
    private  Activity activity;
    public ReqLoaderAsyncTask(Activity activity, String requestType) {
        requestType = REQUEST_TYPE;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        switch (REQUEST_TYPE){
            case Constants.CHECK_INTERNET:
                break;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
