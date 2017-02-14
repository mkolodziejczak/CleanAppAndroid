package com.maciek.myapplication;

import android.os.Bundle;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public void onClick(View view) throws IOException, JSONException
    {
        TextView errorMessage = (TextView) findViewById(R.id.errorMessage);
        EditText emailLogin = (EditText) findViewById(R.id.emailLogin);
        EditText passwordLogin = (EditText) findViewById(R.id.passwordLogin);

        NetworkLogin loginAttempt = new NetworkLogin(emailLogin.getText().toString(), passwordLogin.getText().toString(), this, handler);
        if(loginAttempt.getAuthorizationResult())
        {
            Intent i = new Intent(this, LoggedMenuActivity.class);
            i.putExtra("loggedUser", loginAttempt.getLoggedUser());
            startActivity(i);
        }
        else
        {
            errorMessage.setText(R.string.bad_login_data_error);
        }


    }

    public void onClickOffline(View view)
    {
        Intent i = new Intent(this, OfflineFormActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
