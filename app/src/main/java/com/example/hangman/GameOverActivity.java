package com.example.hangman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class GameOverActivity extends AppCompatActivity {

    private TextView txtPlayResult;
    private TextView txtWord;
    private TextView txtTriesLeft;
    private Intent intent;
    private String playResult;
    private String word;
    private int triesLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        txtPlayResult = findViewById(R.id.play_result);
        txtWord = findViewById(R.id.word);
        txtTriesLeft = findViewById(R.id.tries_left);

        intent = getIntent();

        playResult = intent.getStringExtra("MSG");
        word = intent.getStringExtra("WORD");
        triesLeft = intent.getIntExtra("TRIES_LEFT", 0);

        txtPlayResult.setText(playResult);
        txtWord.setText((getString(R.string.word_to_guess) + " " + word));
        txtTriesLeft.setText(getString(R.string.remaining_tries, triesLeft));
    }

    public void onHomeClick(View view) {
        Intent homePage = new Intent(this, MainActivity.class);
        homePage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homePage);
    }

    @Override
    public void onBackPressed() {
        Intent home = new Intent(this, MainActivity.class);
        home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home);
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

}