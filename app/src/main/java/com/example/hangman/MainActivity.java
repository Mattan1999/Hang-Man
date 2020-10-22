package com.example.hangman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class MainActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton swedish, english;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        radioGroup = findViewById(R.id.radioGroup);
        swedish = findViewById(R.id.swedish);
        english = findViewById(R.id.english);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                changeLanguage(swedish, english);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.start_game_btn:
                Intent gameOver = new Intent(this, GameActivity.class);
                startActivity(gameOver);
                break;

            case R.id.info_screen:
                Intent showInfoIntent = new Intent(this, InfoActivity.class);
                startActivity(showInfoIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // Open a new activity showing info
    public void onInfoClick(View view) {
        Intent showInfoIntent = new Intent(this, InfoActivity.class);
        showInfoIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(showInfoIntent);
    }

    // Opens a new activity and starts the game
    public void onStartGameClick(View view) {
        Intent startGame = new Intent(this, GameActivity.class);
        startGame.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(startGame);
    }

    public void changeLanguage(RadioButton swedish, RadioButton english) {
        if (swedish.isChecked()) {

        } else if (english.isChecked()) {
            
        }
    }

}