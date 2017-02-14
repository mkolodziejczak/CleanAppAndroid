package com.maciek.myapplication;

import android.content.Intent;

import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class FormActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView imageView;
    ImageView imageView2;

    String chosenView="";
    User loggedUser;

    static final Entry newEntry = new Entry();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Spinner trashTypes = (Spinner) findViewById(R.id.trashTypes);

        trashTypes.setOnItemSelectedListener(this);

        List<String> categories = new ArrayList<String>();
        categories.add("odpady komunalne");
        categories.add("opakowania wielomateriałowe");
        categories.add("odpady biodegradowalne");
        categories.add("odpady zielone");
        categories.add("odpady niebezpieczne");
        categories.add("odpady wielkogabarytowe");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        trashTypes.setAdapter(dataAdapter);

        int id = getResources().getIdentifier("android:drawable/ic_menu_camera", null, null);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView2 = (ImageView) findViewById(R.id.imageView2);


        imageView.setImageResource(id);
        imageView2.setImageResource(id);

        loggedUser = (User) getIntent().getSerializableExtra("loggedUser");

    }


    public void onClickSend(View view)
    {
        EditText mEdit = (EditText) findViewById(R.id.editText);
        newEntry.setDescription(mEdit.getText().toString());
        newEntry.setUserEmail(loggedUser.getEmail());
        newEntry.setDate();
        Intent i = new Intent(this, SendActivity.class);
        i.putExtra("newEntry", newEntry);
        i.putExtra("loggedUser", loggedUser);
        startActivity(i);
    }

    public void launchCamera(View view)
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        chosenView=view.getTag().toString();

        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");

            if(chosenView.equals("photo1"))
            {
                imageView.setImageBitmap(photo);
                newEntry.setImage1(photo);
            }
            else if(chosenView.equals("photo2")) {
                imageView2.setImageBitmap(photo);
                newEntry.setImage2(photo);
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        newEntry.setCategory(item);
        // Showing selected spinner item
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}
