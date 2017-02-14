package com.maciek.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LogInToSendActivity extends AppCompatActivity {

    private Handler handler = new Handler();
    Entry newEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_to_send);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        newEntry = (Entry) getIntent().getSerializableExtra("newEntry");
        Log.v("Network", getIntent().getExtras().toString());
    }

    public void onClickLogin(View view)
    {
        TextView errorMessage = (TextView) findViewById(R.id.errorMessage);
        EditText emailLogin = (EditText) findViewById(R.id.emailLogin);
        EditText passwordLogin = (EditText) findViewById(R.id.passwordLogin);

        NetworkLogin loginAttempt = new NetworkLogin(emailLogin.getText().toString(), passwordLogin.getText().toString(), this, handler);

        if(loginAttempt.getAuthorizationResult())
        {
            newEntry.setUserEmail(loginAttempt.getLoggedUser().getEmail());
            NetworkSend sendAttempt = new NetworkSend(newEntry.toJSON(), this, handler);
            if(sendAttempt.getSendResult())
            {
                Toast.makeText(this, R.string.successfully_sent, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, LoggedMenuActivity.class);
                i.putExtra("loggedUser", loginAttempt.getLoggedUser());
                startActivity(i);
            }
            else
            {
                Toast.makeText(this, R.string.error_connecting, Toast.LENGTH_SHORT).show();

            }
        }
        else
        {
            errorMessage.setText(R.string.bad_login_data_error);
        }

    }

}
