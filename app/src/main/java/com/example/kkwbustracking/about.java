package com.example.kkwbustracking;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class about extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("About");

        actionBar.setDisplayHomeAsUpEnabled(true);      // For back button to be displayed on toolbar

        TextView textView = (TextView) findViewById(R.id.my_website);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
    // For back button on toolbar
    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }

}

