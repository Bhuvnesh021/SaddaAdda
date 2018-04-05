package com.sadda.adda.panchratan.saddaadda.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.sadda.adda.panchratan.saddaadda.R;
import com.sadda.adda.panchratan.saddaadda.activities.MainActivity;
import com.sadda.adda.panchratan.saddaadda.objects.Item;
import com.sadda.adda.panchratan.saddaadda.objects.PersonDetails;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by user on 03-07-2017.
 */
public class Utils {
    private static final String TAG = "Utils";

    public static boolean checkNetworkAvailability(Context context) {
        boolean isAvailable = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo infos = connectivityManager.getActiveNetworkInfo();
        if (infos != null && infos.isConnected()) {
            isAvailable = true;
        }

        Log.d(TAG, "checkNetworkAvailability() returned: " + isAvailable);
        return isAvailable;

    }

    public static boolean checkForNull(String value) {
        if (value == null || value.isEmpty() || value.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public static void hideSoftKeyboard(Activity context) {
        View view = context.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static boolean validateEmail(String user_name) {
        Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(user_name);
        return matcher.find();
    }

    public static void showDialod(Activity activity, String title, String message, String positiveText, String negativeText, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveText, listener);
        builder.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public static int[] getIndvidualAmounts(List<Item> itemList) {
        int bhuvneshCounter = 0, kamleshCounter = 0, akshayCounter = 0, vinayCounter = 0, jatinCounter = 0, sandeepCounter = 0;
        for (Item item : itemList) {
            if (item.getUserName().equals("Bhuvnesh Jain")) {
                bhuvneshCounter += Integer.parseInt(item.getPrice());
            } else if (item.getUserName().equals("Kamlesh sawner")) {
                kamleshCounter += Integer.parseInt(item.getPrice());
            } else if (item.getUserName().equals("Akshay Dashore")) {
                akshayCounter += Integer.parseInt(item.getPrice());
            } else if (item.getUserName().equals("Vinay Dixit")) {
                vinayCounter += Integer.parseInt(item.getPrice());
            } else if (item.getUserName().equals("Jatin Gujrati")) {
                jatinCounter += Integer.parseInt(item.getPrice());
            } else if (item.getUserName().equals("Sanddep Yadav")) {
                sandeepCounter += Integer.parseInt(item.getPrice());
            }
        }
        int personArray[] = {bhuvneshCounter, kamleshCounter, akshayCounter, vinayCounter, jatinCounter, sandeepCounter};
        return personArray;
    }

    public static int getTotalAmount(List<Item> itemList) {
        Log.d(TAG, "getTotalAmount() called with: itemList = [" + itemList.size() + "]");
        int totalPrice = 0;
        for (Item item : itemList) {
            String price = item.getPrice();
            Log.i(TAG, "getTotalAmount: price: " + price);
            totalPrice = totalPrice + Integer.parseInt(price);
            Log.i(TAG, "getTotalAmount: totalPrice: " + totalPrice);
        }
        return totalPrice;
    }

    public static String convertGMTToIST(String timeAndDate1) {
//        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//        formatter.setTimeZone(TimeZone.getTimeZone("IST")); // Or whatever IST is supposed to be
//        return formatter.format(timeAndDate1);
        String time = (timeAndDate1.split("\\s"))[1];
        String date = (timeAndDate1.split("\\s"))[0];
        String deliverydate = date;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dateObject = null;

        try {
            dateObject = sdf.parse(deliverydate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        sdf = new SimpleDateFormat("dd-MM-yyyy");
        System.out.println(sdf.format(dateObject));
        String formatedDate = sdf.format(dateObject);
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        Date d = null;
        try {
            d = df.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.MINUTE, 330);
        String newTime = df.format(cal.getTime());
//        DateFormat utcFormat = new SimpleDateFormat(time);
//        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
//        DateFormat indianFormat = new SimpleDateFormat(time);
//        utcFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
//        Date timestamp = null;
//        try {
//            timestamp = utcFormat.parse(time);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        String output = indianFormat.format(timestamp);
        return formatedDate + " " + newTime;
    }

    public static String getFormatedDate(String deliverydate) {
        Log.d(TAG, "getFormatedDate() called with: deliverydate = [" + deliverydate + "]");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dateObject = null;

        try {
            dateObject = sdf.parse(deliverydate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        sdf = new SimpleDateFormat("dd-MM-yyyy");
        String formatedDate = sdf.format(dateObject);
        return formatedDate;
    }

    public static int[] getIndvidualStatistics(List<Item> itemList) {
        /***
         * First calculate without * entries.
         *
         */
        int[] array = getIndvidualAmounts(itemList);
        int bhuvneshAmount = array[0];
        int kamleshAmount = array[1];
        int akshayAmount = array[2];
        int vinayAmount = array[3];
        int jatinAmount = array[4];
        int sandeepAmount = array[5];


        return new int[5];
    }


}

