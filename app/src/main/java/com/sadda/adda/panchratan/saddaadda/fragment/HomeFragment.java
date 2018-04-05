package com.sadda.adda.panchratan.saddaadda.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.sadda.adda.panchratan.saddaadda.R;
import com.sadda.adda.panchratan.saddaadda.adapters.CommentAdapter;
import com.sadda.adda.panchratan.saddaadda.adapters.ItemAdapter;
import com.sadda.adda.panchratan.saddaadda.database.DBHelper;
import com.sadda.adda.panchratan.saddaadda.interfaces.OnCommentReceievedInterface;
import com.sadda.adda.panchratan.saddaadda.objects.Comment;
import com.sadda.adda.panchratan.saddaadda.objects.Item;
import com.sadda.adda.panchratan.saddaadda.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 26-07-2017.
 */
public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,OnCommentReceievedInterface{
   private SwipeRefreshLayout swipeRefreshLayout;
    private static final String TAG = "HomeFragment";
   private RecyclerView recyclerView;
    DBHelper dbHelper;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment,container,false);
        dbHelper = new DBHelper(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_all_items);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_items);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(this);
        DBHelper dbHelper = new DBHelper(getActivity());
        List<Item> itemList = dbHelper.getAllItems();

        if (itemList != null) {
            Log.i(TAG, "onCreateView: itemList size: "+itemList.size());
            ItemAdapter mAdapter = new ItemAdapter(itemList);
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            int totalAmount = Utils.getTotalAmount(itemList);
            Toast.makeText(getActivity(), "Total Amoount: "+totalAmount, Toast.LENGTH_SHORT).show();
        }
        Utils.hideSoftKeyboard(getActivity());
        return view;
    }

    @Override
    public void onRefresh() {
        Log.d(TAG, "onRefresh() called with: ");
        swipeRefreshLayout.setRefreshing(true);
        if (Utils.checkNetworkAvailability(getActivity())) {
            GetAllItemsAsyncTask getAllItemsAsyncTask = new GetAllItemsAsyncTask();
            getAllItemsAsyncTask.setOnCommentReceievedInterface(this);
            getAllItemsAsyncTask.execute();
        }else {
            Toast.makeText(getActivity(), "Oops! No internet connection.", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onAllCommentsReceieved(List<Comment> list) {

    }

    @Override
    public void onAllItemsReceived(List<Item> list) {
        if (list != null) {
            ItemAdapter mAdapter = new ItemAdapter(list);
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
            int totalAmount = Utils.getTotalAmount(list);
            Toast.makeText(getActivity(), "Total Amoount: "+totalAmount, Toast.LENGTH_SHORT).show();
        }
    }


    class GetAllItemsAsyncTask extends AsyncTask<Void, Void, String> {
        String urlComments = null;
        OnCommentReceievedInterface onCommentReceievedInterface;

        public void setOnCommentReceievedInterface(OnCommentReceievedInterface onCommentReceievedInterface) {
            this.onCommentReceievedInterface = onCommentReceievedInterface;
        }

        @Override
        protected void onPreExecute() {
            urlComments = getString(R.string.get_items_list);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL(urlComments);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
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
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String jsonString) {
            if (jsonString == null || jsonString.isEmpty()) {
                Toast.makeText(getActivity(), "Failed to get response, Try Again", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            } else {
                try {
                    Log.i(TAG, "onPostExecute: server Response: " + jsonString);
                    JSONObject jsonObject = new JSONObject(jsonString);
                    Log.i(TAG, "onPostExecute: jsonObject: " + jsonObject.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("server_response");
                    if (jsonArray == null) {
                        // print message;
                        swipeRefreshLayout.setRefreshing(false);
                    } else {
                        ArrayList<Item> commentsArrayList = new ArrayList<>();
                        dbHelper.clearAllDataItemsTable();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject responseObject = jsonArray.getJSONObject(i);
                            Log.i(TAG, "onPostExecute: responseObject: " + responseObject.toString());
                            String itemName = responseObject.getString("item");
                            String quantity = responseObject.getString("quantity");
                            String price = responseObject.getString("price");
                            String names = responseObject.getString("names");
                            String date = responseObject.getString("date");
                            String userName = responseObject.getString("userName");
                            String category = responseObject.getString("category");
                            String description = responseObject.getString("description");
                            Log.i(TAG, "onPostExecute: itemName: " + itemName + " \nquantity: " + quantity + " \n date: " + date + "\nprice: " + price + "\nnames: " + names);

                            Item item = new Item();
                            item.setDate(date);
                            item.setItemName(itemName);
                            item.setQuantity(quantity);
                            item.setPrice(price);
                            item.setNames(names);
                            item.setUserName(userName);
                            item.setCategory(category);
                            item.setDescription(description);
                            commentsArrayList.add(item);
                            dbHelper.addItem(item);
                        }
                        onCommentReceievedInterface.onAllItemsReceived(commentsArrayList);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

        }

    }
}
