package com.maciek.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class SendActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    User loggedUser = null;
    Entry newEntry;
    Handler handler = new Handler();

    EntriesDatabase db;
    Button button1;
    Button button2;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        button1 = (Button) findViewById(R.id.button);
        button1.setEnabled(false);
        button2 = (Button) findViewById(R.id.button2);
        button2.setEnabled(false);

        db = new EntriesDatabase(this);

        loggedUser = (User) getIntent().getSerializableExtra("loggedUser");
        newEntry = (Entry) getIntent().getSerializableExtra("newEntry");

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();


        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(10000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);




        //newEntry.setLatitude(currentLocation.getLatitude()+"");
        //newEntry.setLongitude(currentLocation.getLongitude()+"");
    }

    public void onClickSend(View view)
    {
        if(loggedUser != null)
        {
            int entryId = saveEntry(newEntry);
            NetworkSend sendAttempt = new NetworkSend(newEntry.toJSON(), this, handler);
            if(sendAttempt.getSendResult())
            {
                db.setEntrySent(entryId);
                Intent i = new Intent(this, LoggedMenuActivity.class);
                i.putExtra("loggedUser", loggedUser);
                startActivity(i);
            }
            else
            {
                Toast.makeText(this, R.string.error_connecting, Toast.LENGTH_SHORT).show();

            }
        }
        else
        {
            Intent intent = new Intent(this, LogInToSendActivity.class);
            intent.putExtra("newEntry", newEntry);
            startActivity(intent);
        }
    }

    public void onClickSave(View view)
    {
        saveEntry(newEntry);
        if(loggedUser != null)
        {
            Intent intent = new Intent(this, LoggedMenuActivity.class);
            intent.putExtra("loggedUser", loggedUser);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, R.string.successfully_saved, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

    }

    private int saveEntry(Entry entry) {
        EntriesDatabase entryDb = new EntriesDatabase(this);

        int createdEntryId = entryDb.addEntry(entry);

        Toast.makeText(this, R.string.successfully_saved, Toast.LENGTH_SHORT).show();
        finish();
        return createdEntryId;

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (mGoogleApiClient.isConnected() && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            button1.setEnabled(true);

                            button2.setEnabled(true);
                            newEntry.setLatitude(location.getLatitude()+"");
                            newEntry.setLongitude(location.getLongitude()+"");
                            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

                        }
                    }
            );
        }
        else
        {

            Toast.makeText(this, R.string.location_error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, R.string.location_error, Toast.LENGTH_LONG).show();
    }
}
