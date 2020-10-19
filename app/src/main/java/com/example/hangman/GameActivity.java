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
import android.view.WindowManager;
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
    public static final Random RANDOM = new Random();
    public static final int MAX_ERRORS = 6;
    private int userErrors;
    private TextView txtWordToGuess;
    private String wordToGuess;
    private String wordDisplayedString;
    private char[] wordDisplayedCharArray;
    private TextView txtWrongGuesses;
    private String wrongGuesses;
    private TextView txtRemainingTries;
    private int remainingTries;
    private TextInputEditText userInput;
    private ImageView img;
    private final String MESSAGE_WITH_LETTERS_TRIED = "Fel gissningar: ";

    void initializeGame(){
        img = findViewById(R.id.img);
        txtRemainingTries = findViewById(R.id.remaining_tries);
        txtWordToGuess = findViewById(R.id.word_to_find);
        userInput = findViewById(R.id.user_input);
        txtWrongGuesses = findViewById(R.id.wrong_guesses);
        wordToGuess = hiddenWordToFind();
        wordDisplayedCharArray = wordToGuess.toCharArray();

        for (int i = 0; i < wordDisplayedCharArray.length; i++) {
            wordDisplayedCharArray[i] = '_';
        }

        wordDisplayedString = String.valueOf(wordDisplayedCharArray);

        displayWordOnScreen();

        userInput.setText("");
        userErrors = -1;
        updateImg(userErrors);

        wrongGuesses = "";
        txtWrongGuesses.setText(MESSAGE_WITH_LETTERS_TRIED);

        remainingTries = 7;
        txtRemainingTries.setText(getString(R.string.remaining_tries, remainingTries));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Hang Man");
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initializeGame();
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
                Intent showInfoIntent = new Intent(this, InfoActivity.class);
                startActivity(showInfoIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Intent getSupportParentActivityIntent() {
        return getParentActivityIntentImplement();
    }

    @Override
    public Intent getParentActivityIntent() {
        return getParentActivityIntentImplement();
    }

    private Intent getParentActivityIntentImplement() {
        Intent intent = null;

        Bundle extras = getIntent().getExtras();
        String goToIntent = extras.getString("goto");

        if (goToIntent.equals("MainActivity")) {
            intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }
        return intent;
    }

    // Method returning randomly next word to find
    private String hiddenWordToFind() {
        return myListOfWords[RANDOM.nextInt(myListOfWords.length)];
    }

    private void checkIfLetterIsInWord(String letter) {
        if (wordToGuess.contains(letter)) {
            if (!wordDisplayedString.contains(letter)) {
                revealLetterInWord(letter);
                displayWordOnScreen();

                if (!wordDisplayedString.contains("_")) {
                    youWon();
                }
            }
        } else {
            userInput.setText("");
            //Display tried letter
            if (!wrongGuesses.contains(letter)) {
                wrongGuesses += letter + ", ";
                String messageToBeDisplayed = MESSAGE_WITH_LETTERS_TRIED + wrongGuesses;
                txtWrongGuesses.setText(messageToBeDisplayed);

                decreaseAndDisplayRemainingTries();
                updateImg(userErrors);
            } else {
                userInput.setText("");
                Toast.makeText(this, R.string.character_already_exist,
                        Toast.LENGTH_SHORT).show();
            }
            //Check if user lost the game
            if (remainingTries == 0 || userErrors >= MAX_ERRORS) {
                youLost();
            }
        }
    }

    private void decreaseAndDisplayRemainingTries() {
        if (!(remainingTries == 0)) {
            remainingTries--;
            userErrors++;
            txtRemainingTries.setText(getString(R.string.remaining_tries, remainingTries));
        }
    }

    private void displayWordOnScreen() {
        StringBuilder formattedString = new StringBuilder();

        for (char character: wordDisplayedCharArray) {
            formattedString.append(character).append(" ");
        }
        txtWordToGuess.setText(formattedString.toString());
    }

    private void revealLetterInWord(String input) {
        int indexOfLetter = wordToGuess.indexOf(input);

        while (indexOfLetter >= 0) {
            wordDisplayedCharArray[indexOfLetter] = wordToGuess.charAt(indexOfLetter);
            indexOfLetter = wordToGuess.indexOf(input, indexOfLetter + 1);
        }
        wordDisplayedString = String.valueOf(wordDisplayedCharArray);
    }

    private void updateImg(int number) {
        int resImg = getResources().getIdentifier("hangman_" + number, "drawable",
                getPackageName());
        img.setImageResource(resImg);
    }

    public void onGuessClick(View view){
        if (userErrors < MAX_ERRORS) {
            String input = userInput.getText().toString();
            try {
                if (input.length() < 2) {
                    checkIfLetterIsInWord(input);
                } else {
                    userInput.setText("");
                    Toast.makeText(this, R.string.too_many_characters,
                            Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, R.string.no_characters,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void youWon() {
        Bundle bundle = new Bundle();
        bundle.putString("goto", "MainActivity");

        Intent gameOver = new Intent(this, GameOverActivity.class);
        gameOver.putExtras(bundle);
        startActivity(gameOver);
    }

    private void youLost() {
        Bundle bundle = new Bundle();
        bundle.putString("goto", "MainActivity");

        Intent gameOver = new Intent(this, GameOverActivity.class);
        gameOver.putExtras(bundle);
        startActivity(gameOver);
    }

}