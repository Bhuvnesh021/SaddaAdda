package com.sadda.adda.panchratan.saddaadda.activities;

import android.content.Context;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;

/**
 * Created by user on 05-12-2017.
 */

public class Test implements Runnable{
    public void main(){
        Thread t = new Thread(){
            @Override
            public void run() {
                super.run();
            }
        };
        t.start();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });
        View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        };
        Context context= null;
        Button button = new Button(context);
        button.setOnClickListener(listener);
    }
public static void main(String a, int b){

}
    @Override
    public void run() {

    }
}
