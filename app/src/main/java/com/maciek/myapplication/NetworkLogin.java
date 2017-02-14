package com.maciek.myapplication;

import android.content.Context;

import android.os.Handler;

import android.util.Log;

import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class NetworkLogin{
    private static final String TAG = NetworkLogin.class.getSimpleName();
    private static final String LOGIN_URL = "http://demo8684667.mockable.io/login/";

    private boolean successfulAuthorization = false;
    private User loggedUser = new User();
    public NetworkLogin(final String emailLogin, final String passwordLogin, final Context context, final Handler handler) {


        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("login", emailLogin);
                    jsonObject.put("password", passwordLogin);
                    String result = new NetworkRequest(LOGIN_URL, HttpMethod.POST,
                            jsonObject.toString()).execute();
                    Log.v(TAG, " result " + result);
                    successfulAuthorization = checkResult(result);

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
        if(responseObject.getString("msg").equals("success"))
        {
            loggedUser.setEmail(responseObject.getString("email"));
            loggedUser.setFirstName(responseObject.getString("firstName"));
            return true;
        }
        else
            return false;
    }

    public User getLoggedUser()
    {
        return loggedUser;
    }

    public boolean getAuthorizationResult()
    {
        return successfulAuthorization;
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
