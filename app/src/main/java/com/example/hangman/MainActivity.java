package com.example.hangman;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        androidx.appcompat.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
    }

    // Open a new activity showing info
    public void onInfoClick(View view) {
        Intent showInfoIntent = new Intent(this, InfoActivity.class);
        startActivity(showInfoIntent);
    }

    // Opens a new activity and starts the game
    public void onStartGameClick(View view) {
        Intent startGameIntent = new Intent(this, GameActivity.class);
        startActivity(startGameIntent);
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
                Intent startGameIntent = new Intent(this, GameActivity.class);
                startActivity(startGameIntent);
                break;

            case R.id.info_screen:
                Intent showInfoIntent = new Intent(this, InfoActivity.class);
                startActivity(showInfoIntent);
                break;

            default:
                //
        }
        return super.onOptionsItemSelected(item);
    }
}