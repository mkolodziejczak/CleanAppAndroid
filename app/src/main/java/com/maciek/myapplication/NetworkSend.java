package com.maciek.myapplication;

import android.content.Context;

import android.os.Handler;

import android.util.Log;

import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class NetworkSend{
    private static final String TAG = NetworkSend.class.getSimpleName();
    private static final String LOGIN_URL = "http://demo8684667.mockable.io/entries/";

    private boolean successfullySent = false;

    public NetworkSend(final String entry, final Context context, final Handler handler) {


        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String result = new NetworkRequest(LOGIN_URL, HttpMethod.POST,
                            entry).execute();
                    Log.v(TAG, " result " + result);
                    successfullySent = checkResult(result);

                } catch (IOException | JSONException e) {
                    //TODO handle this exception properly
                    Log.e(TAG, "IOException or JSONException while logging in", e);
                    showErrorToast(context, handler);
                }
            }
        });


        t.start();

        try {
            t.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
            showErrorToast(context, handler);
        }

    }



    private boolean checkResult(String result) throws JSONException {
        JSONObject responseObject = new JSONObject(result);
        return responseObject.getString("msg").equals("success");
    }


    public boolean getSendResult()
    {
        return successfullySent;
    }

    private void showErrorToast(final Context context, Handler handler) {
        showToast(context, handler, R.string.error_connecting);
    }

    private void showToast(final Context context, Handler handler, final int resId) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
