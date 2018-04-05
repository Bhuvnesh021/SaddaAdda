package com.sadda.adda.panchratan.saddaadda.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sadda.adda.panchratan.saddaadda.R;
import com.sadda.adda.panchratan.saddaadda.interfaces.process;
import com.sadda.adda.panchratan.saddaadda.util.Constants;
import com.sadda.adda.panchratan.saddaadda.util.Utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by user on 16-07-2017.
 */
public class CommentPostFragment extends Fragment implements View.OnClickListener, process {
    private static final String TAG = "CommentPostFragment";
    private EditText et_post_comment;
    private Button btn_post_comment_submit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.commet_post_fragment, container, false);
        view.setBackgroundResource(R.drawable.commet_post);
        btn_post_comment_submit = (Button) view.findViewById(R.id.btn_post_comment_submit);
        btn_post_comment_submit.setOnClickListener(this);
        et_post_comment = (EditText) view.findViewById(R.id.et_post_comment);
        return view;
    }

    @Override
    public void onClick(View v) {
        String comment = et_post_comment.getText().toString();
        if (Utils.checkForNull(comment)) {
            Toast.makeText(getActivity(), "Comment Can't be null", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (comment.length() > 6) {
                PostCommentAsyncTask postCommentAsyncTask = new PostCommentAsyncTask(false);
                postCommentAsyncTask.setProcess(this);
                postCommentAsyncTask.execute(comment);
            } else {
                Toast.makeText(getActivity(), "Comment length should be more then 6", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onProcessCompleted(boolean response) {
        Utils.hideSoftKeyboard(getActivity());
        if(response) {
                et_post_comment.setText(null);
            }
    }

    class PostCommentAsyncTask extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String loginPasswordUrl = null;
        boolean isNotificationCalled;
        process process;

        public void setProcess(process process) {
            this.process = process;
        }

        public PostCommentAsyncTask(boolean isNotificationCalled) {
            this.isNotificationCalled = isNotificationCalled;
        }

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(getActivity(), ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            loginPasswordUrl = getString(R.string.post_comment_url);
            progressDialog.show();


        }

        @Override
        protected String doInBackground(String... params) {
            String comment = params[0];
            String response = null;
            try {
                URL url = new URL(loginPasswordUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String userName = getActivity().getSharedPreferences(getString(R.string.userCredentials), Context.MODE_PRIVATE).getString("name", null);
                String dataString = URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(userName, "UTF-8") + "&" +
                        URLEncoder.encode("comment", "UTF-8") + "=" + URLEncoder.encode(comment, "UTF-8");
                Log.i(TAG, "doInBackground: dataString: " + dataString);
                bufferedWriter.write(dataString);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                response = bufferedReader.readLine();
                Log.i(TAG, "doInBackground: After receiving server response: " + response);
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

            } catch (IOException e) {
                e.printStackTrace();
            }
            if (response != null && response.equalsIgnoreCase(Constants.SUCCESS)) {
                try {
                    URL url = new URL("http://demoappbhuvnesh.000webhostapp.com/demo/generate_notification.php");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String userName = getActivity().getSharedPreferences(getString(R.string.userCredentials), Context.MODE_PRIVATE).getString("name", null);
                    String dataString = URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(userName, "UTF-8") + "&" +
                            URLEncoder.encode("message", "UTF-8") + "=" + URLEncoder.encode(comment, "UTF-8");
                    Log.i(TAG, "doInBackground: dataString: " + dataString);
                    bufferedWriter.write(dataString);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                    String readLine = bufferedReader.readLine();
                    Log.i(TAG, "doInBackground: After receiving server response: " + readLine);
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            Log.d(TAG, "onPostExecute() called with: " + "response = [" + response + "]");
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (response != null && response.equalsIgnoreCase(Constants.SUCCESS)) {
                Toast.makeText(getActivity(), "Sucessfully inserted", Toast.LENGTH_SHORT).show();
                process.onProcessCompleted(true);
            } else {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
