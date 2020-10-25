package com.example.hangman;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class InfoActivity extends AppCompatActivity {

    SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);
        loadTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void loadTheme() {
        if (!sharedPref.loadNightModeState()) {
            setTheme(R.style.AppTheme);
        } else if (sharedPref.loadNightModeState()) {
            setTheme(R.style.DarkTheme);
        }
    }
}