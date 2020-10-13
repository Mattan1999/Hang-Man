package com.example.hangman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
    private String wordToGuess;
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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
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
        userInput = findViewById(R.id.user_input);
        wrongGuesses = findViewById(R.id.wrong_guesses);
        wordToGuess = hiddenWordToFind();
        lettersInWordFound = new char[wordToGuess.length()];

        for (int i = 0; i < lettersInWordFound.length; i++){
            lettersInWordFound[i] = '_';
        }
    }

    public void startGame(){
        userErrors = -1;
        letters.clear();
        updateImg(userErrors);
        wordToGuessTv.setText(printWordToGuess(lettersInWordFound));
    }

    // Method returning randomly next word to find
    private String hiddenWordToFind() {
        return myListOfWords[random.nextInt(myListOfWords.length)];
    }

    private String printWordToGuess(char[] array) {
        StringBuilder hiddenWord = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            hiddenWord.append(array[i]);
            if (i < array.length - 1) {
                hiddenWord.append(" ");
            }
        }
        return hiddenWord.toString();
    }

    private void revealWordToGuess(String letter) {
        int indexOfLetter = wordToGuess.indexOf(letter);

        while (indexOfLetter >= 0) {
            lettersInWordFound[indexOfLetter] = wordToGuess.charAt(indexOfLetter);
            indexOfLetter = wordToGuess.indexOf(letter, indexOfLetter + 2);
        }

        wordToGuessTv.setText(printWordToGuess(lettersInWordFound));
    }

    private void updateImg(int play) {
        int resImg = getResources().getIdentifier("hangman_" + play, "drawable",
                getPackageName());
        img.setImageResource(resImg);
    }

    public void onGuessClick(View view){
        String input = userInput.getText().toString();
        if (userErrors < MAX_ERRORS) {
            try {
                if (input.length() < 2) {
                    if (!letters.contains(input)) {
                        if (wordToGuess.contains(input)) {
                            revealWordToGuess(input);
                            userInput.setText("");
                        } else {
                            userErrors++;
                            userInput.setText("");
                        }
                        letters.add(input);
                        wrongGuesses.setText(letters.toString());
                    } else {
                        Toast.makeText(this, R.string.character_already_exist, Toast.LENGTH_SHORT)
                                .show();
                    }
                    wordToGuessTv.setText(printWordToGuess(lettersInWordFound));
                    updateImg(userErrors);
                } else {
                    Toast.makeText(this, R.string.too_many_characters, Toast.LENGTH_SHORT)
                            .show();
                }
            } catch (Exception e) {
                Toast.makeText(this, R.string.no_characters, Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

}