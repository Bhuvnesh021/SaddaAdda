package com.sadda.adda.panchratan.saddaadda.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sadda.adda.panchratan.saddaadda.R;
import com.sadda.adda.panchratan.saddaadda.database.DBHelper;
import com.sadda.adda.panchratan.saddaadda.interfaces.process;
import com.sadda.adda.panchratan.saddaadda.util.InstanceSingleton;
import com.sadda.adda.panchratan.saddaadda.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements process {
    public static final String FAILURE = "failure";
    public static final String IMAGE_URL = "image_url";
    public static final String NAME = "name";
    public static final String USER_NAME = "user_name";
    private EditText et_user_name = null;
    private EditText et_password = null;
    private Button btn_login = null;
    private ProgressDialog progressDialog = null;
    private static final String TAG = "MainActivity";
    private boolean isFirstClicked = true;
    private String url = "http://demoappbhuvnesh.000webhostapp.com/demo/fcm_token_insert.php";
    private CheckBox check_staySigned;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new DBHelper(getApplicationContext());
        setContentView(R.layout.activity_main);
        et_user_name = (EditText) findViewById(R.id.user_name_ET);
        et_password = (EditText) findViewById(R.id.password_ET);
        check_staySigned = (CheckBox) findViewById(R.id.check_staysigned);
        btn_login = (Button) findViewById(R.id.floating_login);
        decideLauncherActivity();
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                performLogin();
            }
        });
    }

    private void performLogin() {

        String user_name = et_user_name.getText().toString();
        String password = et_password.getText().toString();
        if (!(Utils.checkForNull(user_name) || Utils.checkForNull(password))) {
            if (!Utils.validateEmail(user_name)) {
                et_user_name.setError("Enter valid email address");
                return;
            }
            try {
                if (Utils.checkNetworkAvailability(MainActivity.this)) {
                    DataValidationAsyncTask dataValidationAsyncTask = new DataValidationAsyncTask(MainActivity.this);
                    dataValidationAsyncTask.setProcess(MainActivity.this);
                    dataValidationAsyncTask.execute(user_name, password);
                } else {
                    Toast.makeText(MainActivity.this, "Oops! No Internet Connection.", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(MainActivity.this, "Enter Valid Credentials", Toast.LENGTH_SHORT).show();
        }
    }

    private void decideLauncherActivity() {
        boolean isStaySignedInChecked = getSharedPreferences(getString(R.string.stay_signed_in),MODE_PRIVATE).getBoolean(getString(R.string.isStaySignedInChecked),false);
        if(isStaySignedInChecked){
            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.stay_signed_in),MODE_PRIVATE);
            String user_id = sharedPreferences.getString("user_id",null);
            String password = sharedPreferences.getString("password",null);
            if(user_id!=null && password!=null){
                Log.i(TAG, "decideLauncherActivity: user_id: "+user_id);
                Log.i(TAG, "decideLauncherActivity: password: " + password);
                et_user_name.setText(user_id);
                et_password.setText(password);
                check_staySigned.setChecked(true);
                performLogin();
            }
        }
    }


    class DataValidationAsyncTask extends AsyncTask<String, Void, String> {
        String loginPasswordUrl = null;
        Activity activity;
        process process;

        public void setProcess(process process) {
            this.process = process;
        }

        public DataValidationAsyncTask(Activity activity) {
            this.activity = activity;
            progressDialog = new ProgressDialog(activity, ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);

        }

        @Override
        protected void onPreExecute() {

            loginPasswordUrl = getString(R.string.userid_password_verifier_php);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String user_name, password;
            user_name = params[0];
            password = params[1];
            try {
                URL url = new URL(loginPasswordUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                String fcmToken = getSharedPreferences(getString(R.string.FCM_PREFS), MODE_PRIVATE).getString(getString(R.string.FCM_TOKEN), "");
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String dataString = URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") + "&" +
                        URLEncoder.encode("fcm_token", "UTF-8") + "=" + URLEncoder.encode(fcmToken, "UTF-8");
                Log.i(TAG, "doInBackground: dataString: " + dataString);
                bufferedWriter.write(dataString);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                Log.i(TAG, "doInBackground: outputStream close success");
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String tempJSON = null;
                while ((tempJSON = bufferedReader.readLine()) != null) {
                    Log.i(TAG, "doInBackground: tempJSON: " + tempJSON);
                    stringBuilder.append(tempJSON);
                }
                Log.i(TAG, "doInBackground: After receiving server response: " + stringBuilder.toString());
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String jsonString) {
            Log.d(TAG, "onPostExecute() called with: " + "jsonString = [" + jsonString + "]");
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (jsonString == null || jsonString.isEmpty()) {
                Toast.makeText(MainActivity.this, "Failed to get response, Try Again", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    Log.i(TAG, "onPostExecute: server Response: " + jsonString);
                    JSONObject jsonObject = new JSONObject(jsonString);
                    Log.i(TAG, "onPostExecute: jsonObject: " + jsonObject.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("server_response");
                    if (jsonArray == null) {
                        process.onProcessCompleted(false);
                    } else {
                        Log.i(TAG, "onPostExecute: jsonArray length: " + jsonArray.length());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject responseObject = jsonArray.getJSONObject(i);
                            Log.i(TAG, "onPostExecute: responseObject: " + responseObject.toString());
                            String response = responseObject.getString("response");
                            Log.i(TAG, "onPostExecute: response: " + response);
                            if ((response.toLowerCase().equals(FAILURE))) {
                                process.onProcessCompleted(false);
                            } else if (response.toLowerCase().equals("success")) {
                                String user_name = responseObject.getString("user_name");
                                Log.i(TAG, "onPostExecute: user_name: " + user_name);
                                String name = responseObject.getString("name");
                                Log.i(TAG, "onPostExecute: name: " + name);
                                String imageURL = responseObject.getString(IMAGE_URL);
                                Log.i(TAG, "onPostExecute: imageURL: "+imageURL);
                                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.userCredentials), MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(USER_NAME, user_name);
                                editor.putString(NAME, name);
                                editor.putString("image_url",imageURL);
                                editor.commit();
                                process.onProcessCompleted(true);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public void onProcessCompleted(boolean response) {
        if (response) {
            Utils.hideSoftKeyboard(MainActivity.this);
            Toast.makeText(MainActivity.this, "Successfully login ", Toast.LENGTH_SHORT).show();
            SharedPreferences sh= getSharedPreferences(getString(R.string.stay_signed_in),MODE_PRIVATE);
            SharedPreferences.Editor editor = sh.edit();
            editor.putBoolean(getString(R.string.isStaySignedInChecked),check_staySigned.isChecked());
            editor.putString("user_id", et_user_name.getText().toString());
            editor.putString("password",et_password.getText().toString());
            editor.commit();
            startActivity(new Intent(MainActivity.this, SaddaAddaLauncherActivity.class));
        } else {
            Toast.makeText(MainActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        Toast toast = Toast.makeText(MainActivity.this, "Back Press one more time to EXIT.", Toast.LENGTH_SHORT);

        if (isFirstClicked) {

            toast.show();
            isFirstClicked = false;
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    isFirstClicked = true;
                }
            }.start();
        } else {
            toast.cancel();
            finish();
        }

    }

    public void sendTokenToServer() {
        SharedPreferences sharedPreferences = getSharedPreferences("fcm_prefs", MODE_PRIVATE);
        final String token = sharedPreferences.getString("fcm_token", "");
        Log.i(TAG, "sendTokenToServer: token: " + token);
        StringRequest request = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse: some error occured: " + error.getMessage().toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("fcm_token", token);
                return params;
            }
        };
        InstanceSingleton.getInstance(MainActivity.this).addToRequestQueue(request);
    }
}
