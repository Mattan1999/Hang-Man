package com.example.hangman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    public static final String[] myListOfWords = {
            "ABSTRACT", "ASSERT", "BOOLEAN", "BREAK", "BYTE",
            "CASE", "CATCH", "CHAR", "CLASS", "CONST",
            "CONTINUE", "DEFAULT", "DOUBLE", "DO", "ELSE",
            "ENUM", "EXTENDS", "FALSE", "FINAL", "FINALLY",
            "FLOAT", "FOR", "GOTO", "IF", "IMPLEMENTS",
            "IMPORT", "INSTANCE", "INT", "INTERFACE",
            "LONG", "NATIVE", "NEW", "NULL", "PACKAGE",
            "PRIVATE", "PROTECTED", "PUBLIC", "RETURN",
            "SHORT", "STATIC", "STRICT", "SUPER", "SWITCH",
            "SYNCHRONIZED", "THIS", "THROW", "THROWS",
            "TRANSIENT", "TRUE", "TRY", "VOID", "VOLATILE", "WHILE"
    };
    public static final Random random = new Random();
    // Max errors before user lose
    public static final int MAX_ERRORS = 6;
    // Word to reveal
    private char[] wordToGuess;
    // Word to reveal stored in a char array to show progression of user
    private char[] lettersInWordFound;
    private int userErrors;
    // letters already entered by user
    private ArrayList<String> letters = new ArrayList<>();
    private ImageView img;
    private TextView remainingTries;
    private int tries = 7;
    private TextView wordToGuessTv;
    private TextView wrongGuesses;
    private TextInputEditText userInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        androidx.appcompat.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Hang Man");
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        initializeGame();
        startGame();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.info_screen:
                Intent showInfoIntent = new Intent(this, GameActivityInfo.class);
                startActivity(showInfoIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initializeGame(){
        img = findViewById(R.id.img);
        remainingTries = findViewById(R.id.remaining_tries);
        wordToGuessTv = findViewById(R.id.word_to_find);
        wordToGuess = hiddenWordToFind();
    }

    public void startGame(){
        userErrors = -1;
        letters.clear();

        lettersInWordFound = new char[wordToGuess.length];

        for (int i = 0; i < lettersInWordFound.length; i++){
            lettersInWordFound[i] = '_';
        }
        updateImg(userErrors);
        wordToGuessTv.setText();
    }

    // Method returning randomly next word to find
    private char[] hiddenWordToFind() {
        return myListOfWords[random.nextInt(myListOfWords.length)].toCharArray();
    }

    private void updateImg(int play) {
        int resImg = getResources().getIdentifier("hangman_" + play, "drawable",
                getPackageName());
        img.setImageResource(resImg);
    }

    public void onGuessClick(View view){
        if (userErrors < MAX_ERRORS) {
            userInput.setText("");
            updateImg(userErrors);
        }
    }



}