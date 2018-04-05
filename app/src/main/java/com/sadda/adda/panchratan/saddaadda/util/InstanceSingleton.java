package com.sadda.adda.panchratan.saddaadda.util;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by user on 08-07-2017.
 */
public class InstanceSingleton {
    static Context mContext ;
    static InstanceSingleton mInstanceSingleton;
    RequestQueue requestQueue;
    public InstanceSingleton(Context context){
        mContext = context;
        getRequestqueue();
    }
    public  RequestQueue getRequestqueue(){
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return requestQueue;
    }
    public static synchronized InstanceSingleton getInstance(Context context){
        if(mInstanceSingleton == null){
            mInstanceSingleton = new InstanceSingleton(context);
        }
        return mInstanceSingleton;
    }
    public<T> void addToRequestQueue(Request<T> request){
        getRequestqueue().add(request);
    }

}
