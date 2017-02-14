package com.maciek.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class LoggedMenuActivity extends AppCompatActivity {

    User loggedUser;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loggedUser = (User) getIntent().getSerializableExtra("loggedUser");

        TextView welcome = (TextView) findViewById(R.id.textView);
        welcome.setText(welcome.getText().toString()+", "+loggedUser.getFirstName());

    }

    public void onClick(View view)
    {
        Intent i = new Intent(this, FormActivity.class);
        i.putExtra("loggedUser", loggedUser);
        startActivity(i);
    }

    public void onClickMyEntries(View view)
    {
        Intent i = new Intent(this, MapsActivity.class);
        i.putExtra("loggedUser", loggedUser);
        startActivity(i);
    }

    public void onClickSendRemaining(View view)
    {
        EntriesDatabase db = new EntriesDatabase(this);
        int entriesNum = db.getEntriesNumber();

        for(int i=0; i<entriesNum; i++)
        {
            Entry tempEntry = db.getEntry(i);
            NetworkSend sendAttempt = new NetworkSend(tempEntry.toJSON(), this, handler);
            if(sendAttempt.getSendResult())
            {
                Toast.makeText(this, R.string.successfully_sent, Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, R.string.error_connecting, Toast.LENGTH_SHORT).show();

            }
        }

    }
}
