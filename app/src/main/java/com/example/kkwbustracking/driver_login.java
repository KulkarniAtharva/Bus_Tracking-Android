package com.example.kkwbustracking;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class driver_login extends AppCompatActivity
{
    TextInputEditText licence_no_textInputEditText,name_textInputEditText,phone_textInputEditText,email_textInputEditText;
    Button login;
    String licence_no,name,phone,email;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_login);

        licence_no_textInputEditText = (TextInputEditText)findViewById(R.id.licence_no);
        phone_textInputEditText = (TextInputEditText)findViewById(R.id.phone_no);
        name_textInputEditText = (TextInputEditText)findViewById(R.id.name);
        email_textInputEditText = (TextInputEditText)findViewById(R.id.email);

        login = findViewById(R.id.login);
        databaseHelper = new DatabaseHelper(this);

        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(driver_login.this, driver_tracklocation.class);

                name = name_textInputEditText.getText().toString();
                phone = phone_textInputEditText.getText().toString();
                licence_no = licence_no_textInputEditText.getText().toString();
                email = email_textInputEditText.getText().toString();

                if(TextUtils.isEmpty(name))
                    name_textInputEditText.setError("Name is Mandatory");
                if(TextUtils.isEmpty(phone))
                    phone_textInputEditText.setError("Phone No. is Mandatory");
                if(TextUtils.isEmpty(licence_no))
                    licence_no_textInputEditText.setError("Licence No. is Mandatory");
                if(!(TextUtils.isEmpty(name) && TextUtils.isEmpty(phone) && TextUtils.isEmpty(licence_no)))
                {
                    databaseHelper.insertData(name, phone, licence_no, email);               // Insert data into SqLite Database

                    // intent.putExtra("licence_no",licence_no);
                    // intent.putExtra("name",name);
                    // intent.putExtra("phone",phone);
                    //intent.putExtra("email",email);

                    startActivity(intent);
                }
            }
        });

        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("Driver Login");
        actionBar.setDisplayHomeAsUpEnabled(true);      // For back button to be displayed on toolbar


    }

    // For back button on toolbar
    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }
}
