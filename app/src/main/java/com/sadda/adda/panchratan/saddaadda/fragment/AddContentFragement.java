package com.sadda.adda.panchratan.saddaadda.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sadda.adda.panchratan.saddaadda.R;
import com.sadda.adda.panchratan.saddaadda.database.DBHelper;
import com.sadda.adda.panchratan.saddaadda.objects.Item;
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
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by user on 06-07-2017.
 */
public class AddContentFragement extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static final String TAG = "AddContentFragement";
    private Spinner sp_itemCategory, sp_itemValues, sp_itemQuantity;
    private Button btn_reset, btn_add;
    private TextView txt_addStar, txt_added_names;
    private EditText et_price;
    final boolean[] checkedNames = {false, false, false, false, false, false};
    String[] selectedNames = {"", "", "", "", "", ""};
    private EditText et_description;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_content_layout, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private void makeItemsClickableForCategory(boolean isClickable) {
        sp_itemQuantity.setEnabled(isClickable);
        sp_itemQuantity.setSelection(0);
        sp_itemValues.setEnabled(isClickable);
        sp_itemValues.setSelection(0);
        txt_addStar.setEnabled(isClickable);
        txt_added_names.setText(null);
        btn_add.setClickable(isClickable);
        et_price.setEnabled(isClickable);
        et_price.setText(null);
    }

    private void makeItemsClickableForItems(boolean isClickable) {
        sp_itemQuantity.setEnabled(isClickable);
        txt_addStar.setEnabled(isClickable);
        btn_add.setClickable(isClickable);
        et_price.setEnabled(isClickable);

    }

    private void initView(View view) {
        sp_itemCategory = (Spinner) view.findViewById(R.id.sp_category);
        sp_itemCategory.setOnItemSelectedListener(this);
        sp_itemQuantity = (Spinner) view.findViewById(R.id.sp_quantity);
        sp_itemQuantity.setOnItemSelectedListener(this);

        sp_itemValues = (Spinner) view.findViewById(R.id.sp_item_values);
        sp_itemValues.setOnItemSelectedListener(this);

        txt_addStar = (TextView) view.findViewById(R.id.txt_add_star);
        txt_addStar.setOnClickListener(this);
        txt_added_names = (TextView) view.findViewById(R.id.txt_marked_names);
        btn_add = (Button) view.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);

        btn_reset = (Button) view.findViewById(R.id.btn_reset);
        btn_reset.setOnClickListener(this);
        et_price = (EditText) view.findViewById(R.id.et_price);
        et_description = (EditText) view.findViewById(R.id.et_post_description);
        makeItemsClickableForCategory(false);
    }

    private ArrayList showNamesForStar() {
        final String[] items = {"Bhuvnesh Jain", "Akshay Dashore", "Kamlesh sawner", "Sandeep yadav ", "Rishabh dixit", "Vinay dixit"};
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("Select The Names")
                .setMultiChoiceItems(items, checkedNames, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                        if (isChecked) {
                            // If the user checked the item, add it to the selected items
                            checkedNames[indexSelected] = true;
                            selectedNames[indexSelected] = items[indexSelected];
                        } else {
                            // Else, if the item is already in the array, remove it
                            checkedNames[indexSelected] = false;
                            selectedNames[indexSelected] = "";
                        }
                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on OK
                        //  You can write the code  to save the selected item here
                        txt_added_names.setText(null);
                        for (int i = 0; i < selectedNames.length; i++) {
                            if (!(selectedNames[i] == "")) {
                                txt_added_names.setText(txt_added_names.getText().toString() + selectedNames[i] + ", ");
                            }
                        }
                        if (txt_added_names.getText().toString() != null) {
                            String str = txt_added_names.getText().toString();
                            try {
                                txt_added_names.setText(str.substring(0, str.length() - 2));
                            } catch (Exception e) {
                                Log.e(TAG, "onClick: " + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                        dialog.dismiss();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on Cancel
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_add_star:
                showNamesForStar();
                break;
            case R.id.btn_reset:
                sp_itemCategory.setSelection(0);
                makeItemsClickableForItems(false);
                break;
            case R.id.btn_add:
                validateData();
                break;
        }
    }

    private void validateData() {
        Log.d(TAG, "validateData() called with: ");
        et_price.setError(null);
        final String category = sp_itemCategory.getSelectedItem().toString();
        final String itemName = sp_itemValues.getSelectedItem().toString();
        final String quantity = sp_itemQuantity.getSelectedItem().toString();
        final String totalPrice = et_price.getText().toString();
        final String selectedNames = txt_added_names.getText().toString();
        if (!Utils.checkForNull(totalPrice)) {
            Log.i(TAG, "validateData: Following data are inserted: \n category: " + category + "\n itemName: " + itemName + "\n quantity: " + quantity + "\n totalPrice: " + totalPrice + "\n selectedNames: " + selectedNames);
            String messagee = perpareMessage(category,itemName,quantity,totalPrice,selectedNames);
            Utils.showDialod(getActivity(),"Items Details",String.valueOf(Html.fromHtml(messagee)),"OK","No, I want update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //proceed
                    AddItemAsyncTask addItemAsyncTask = new AddItemAsyncTask(true);
                    addItemAsyncTask.execute(category,itemName,quantity,totalPrice,selectedNames);
                }
            });

        }else {
            et_price.setError("Can't be null");
        }
    }

    private String perpareMessage(String category, String itemName, String quantity, String totalPrice, String selectedNames) {
        String message = "<b>Category</b> <br>" + category + "<br><br><b>Item Name</b><br>" + itemName + "<br><br><b>Quantity of Items</b><br>" + quantity +
                "<br><br><b>Total Price</b><br>" + totalPrice + "<br><br><b>Selected Names</b><br>" + selectedNames.replaceAll(", ","<br>");
        return message;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onItemSelected() called with: " + "parent = [" + parent + "], view = [" + view + "], position = [" + position + "], id = [" + id + "]");
        switch (parent.getId()) {
            case R.id.sp_category:
                if ((position == 0)) {
                    Toast.makeText(getActivity(), "select a cetegory", Toast.LENGTH_SHORT).show();
                    makeItemsClickableForCategory(false);
                    sp_itemValues.setEnabled(true);
                    Toast.makeText(getActivity(), parent.getSelectedItem().toString() + " Selected", Toast.LENGTH_SHORT).show();
                } else {
                    if(position == 1){
                        sp_itemValues.setEnabled(true);
                        sp_itemValues.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.valuesOfVegitables)));
                    }else if(position == 2){
                        sp_itemValues.setEnabled(true);
                        sp_itemValues.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.valuesOfGrocery)));
                    }else if(position == 3){
                        sp_itemValues.setEnabled(true);
                        sp_itemValues.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.valuesOfBillPay)));
                    }
//                    Toast.makeText(getActivity(), parent.getSelectedItem().toString() + " Selected", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.sp_item_values:
                if (!(position == 0)) {
                    makeItemsClickableForItems(true);
                    Toast.makeText(getActivity(), parent.getSelectedItem().toString() + " Selected", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "select a item", Toast.LENGTH_SHORT).show();
                    makeItemsClickableForItems(false);
                }
                break;
            case R.id.sp_quantity:


                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }




    class AddItemAsyncTask extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String loginPasswordUrl = null;
        boolean isNotificationCalled;
        private String description;
        private String userName;

        public AddItemAsyncTask(boolean isNotificationCalled) {
            this.isNotificationCalled = isNotificationCalled;
        }

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(getActivity(), ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            loginPasswordUrl = getString(R.string.add_item_url);
            progressDialog.show();
            description = et_description.getText().toString();
            userName = getActivity().getSharedPreferences(getString(R.string.userCredentials),Context.MODE_PRIVATE).getString("name",null);

        }

        @Override
        protected String doInBackground(String... params) {
            Item item = null;
            String category = params[0];
            String itemName = params[1];
            String quantity = params[2];
            String totalPrice = params[3];
            String selectedNames = params[4];
            String response = null;
            try {
                URL url = new URL(loginPasswordUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                item = new Item();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                Log.i(TAG, "doInBackground: category: "+category+"\n quantity: "+quantity+"\n totalPrice: "+totalPrice+"\n selectedNames: "+selectedNames+"\n itemName: "+itemName);
                String dataString = URLEncoder.encode("category", "UTF-8") + "=" + URLEncoder.encode(category, "UTF-8") + "&" +
                        URLEncoder.encode("item", "UTF-8") + "=" + URLEncoder.encode(itemName, "UTF-8") + "&" +
                        URLEncoder.encode("quantity", "UTF-8") + "=" + URLEncoder.encode(quantity, "UTF-8") + "&" +
                        URLEncoder.encode("price", "UTF-8") + "=" + URLEncoder.encode(totalPrice, "UTF-8") + "&" +
                        URLEncoder.encode("names", "UTF-8") + "=" + URLEncoder.encode(selectedNames, "UTF-8") + "&" +
                        URLEncoder.encode("description", "UTF-8") + "=" + URLEncoder.encode(description, "UTF-8") + "&" +
                        URLEncoder.encode("userName", "UTF-8") + "=" + URLEncoder.encode(userName, "UTF-8");
                item.setQuantity(quantity);
                item.setItemName(itemName);
                item.setCategory(category);
                item.setPrice(totalPrice);
                item.setNames(selectedNames);
                item.setDescription(description);
                item.setUserName(userName);
                item.setDate("2017-10-14 10:10:10");
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
                DBHelper dbHelper = new DBHelper(getActivity());
                dbHelper.addItem(item);
                try {
                    URL url = new URL("http://demoappbhuvnesh.000webhostapp.com/demo/generate_notification.php");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String userName = getActivity().getSharedPreferences(getString(R.string.userCredentials), Context.MODE_PRIVATE).getString("name", null);
                    String dataString = URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(userName, "UTF-8") + "&" +
                            URLEncoder.encode("message", "UTF-8") + "=" + URLEncoder.encode(userName + " add "+itemName+" worth Rs. "+totalPrice, "UTF-8");
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
                Utils.hideSoftKeyboard(getActivity());
                Toast.makeText(getActivity(), "Sucessfully inserted", Toast.LENGTH_SHORT).show();
                btn_reset.performClick();
                et_description.setText(null);
            } else {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }

        }
    }

}
