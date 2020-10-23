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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.Arrays;
import java.util.Random;


public class GameActivity extends AppCompatActivity {

    public static final String[] myListOfEnglishWords = {
            "ASSERT", "KNOT", "CORRUPTION", "INTOXICANT", "AXIS",
            "LITTLE", "CRAWLER", "KANGAROO", "SMILE", "CENTRAL",
            "APPARENT", "FOREIGN", "CHARISMA", "TREMOR", "ALLOTMENT",
            "PISTOL", "LULLABY", "BEAUTY", "AUTHORITY", "PILL"
    };
    public static final String[] myListOfSwedishWords = {
            "GLÖMMA", "PAUS", "FLYTTAT", "VÄNJA", "GJORDES",
            "PEKA", "STOREBROR", "KONJAK", "SKAKADE", "SAMLATS",
            "LÄPPSTIFT", "RABATT", "KONFLIKT", "JORDNÖTSSMÖR", "FUNKAR",
            "FÄNGELSET", "SLÄPPTE", "INTERNATIONELLA", "TÄLTET", "HÅLAN"
    };
    public static final Random RANDOM = new Random();
    private String YOU_WON;
    private String YOU_LOST;
    private int userErrors;
    private TextView txtWordToGuess;
    private String wordToGuess;
    private String wordDisplayedString;
    private char[] wordDisplayedCharArray;
    private TextView txtWrongGuesses;
    private String wrongLetters;
    private TextView txtTriesLeft;
    private int triesLeft;
    private ImageView img;
    private String MESSAGE_WITH_LETTERS_TRIED;


    void initializeGame() {
        img = findViewById(R.id.img);
        txtTriesLeft = findViewById(R.id.remaining_tries);
        txtWordToGuess = findViewById(R.id.word_to_find);
        txtWrongGuesses = findViewById(R.id.wrong_guesses);
        YOU_WON = getText(R.string.you_won).toString();
        YOU_LOST = getText(R.string.you_lost).toString();
        MESSAGE_WITH_LETTERS_TRIED = getText(R.string.wrong_guesses).toString() + " ";
        wordToGuess = hiddenEnglishWordToFind();
        wordDisplayedCharArray = wordToGuess.toCharArray();
        Arrays.fill(wordDisplayedCharArray, '_');
        wordDisplayedString = String.valueOf(wordDisplayedCharArray);

        displayWordOnScreen();

        userErrors = -1;
        updateImg(userErrors);

        wrongLetters = "";
        txtWrongGuesses.setText(MESSAGE_WITH_LETTERS_TRIED);

        triesLeft = 7;
        txtTriesLeft.setText(getString(R.string.remaining_tries, triesLeft));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

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
        if (item.getItemId() == R.id.info_screen) {
            Intent showInfoIntent = new Intent(this, InfoActivity.class);
            startActivity(showInfoIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent home = new Intent(this, MainActivity.class);
        home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home);
    }

    // Method returning randomly a word to find
    private String hiddenEnglishWordToFind() {
        return myListOfEnglishWords[RANDOM.nextInt(myListOfEnglishWords.length)];
    }

    private void checkIfLetterIsInWord(String letter) {
        if (wordToGuess.contains(letter)) {
            revealLetterInWord(letter);
            displayWordOnScreen();
            if (!wordDisplayedString.contains("_")) {
                youWon();
            }
        } else {
            //Display tried letter
            if (!wrongLetters.contains(letter)) {
                Toast.makeText(this, R.string.wrong_guess,
                        Toast.LENGTH_SHORT).show();
                wrongLetters += letter + ", ";
                String messageToBeDisplayed = MESSAGE_WITH_LETTERS_TRIED + wrongLetters;
                txtWrongGuesses.setText(messageToBeDisplayed);

                decreaseAndDisplaytriesLeft();
                updateImg(userErrors);
            }
            //Check if user lost the game
            if (triesLeft == 0) {
                youLost();
            }
        }
    }

    private void decreaseAndDisplaytriesLeft() {
        if (!(triesLeft == 0)) {
            triesLeft--;
            userErrors++;
            txtTriesLeft.setText(getString(R.string.remaining_tries, triesLeft));
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
        String url = "https://raw.githubusercontent.com/Mattan1999/Hang-Man/master/app/src/main/res/drawable-v24/hangman_" + number + ".png";
        Glide.with(this).load(url).into(img);
    }

    public void touchLetter(View view){
        boolean buttonPressed = ((Button) view).isPressed();
        if (buttonPressed) {
            ((Button) view).setEnabled(false);
        }
        String input = ((Button) view).getText().toString();
        checkIfLetterIsInWord(input);
    }

    private void youWon() {
        Intent gameOver = new Intent(this, GameOverActivity.class);
        gameOver.putExtra("WORD", wordToGuess);
        gameOver.putExtra("MSG", YOU_WON);
        gameOver.putExtra("TRIES_LEFT", triesLeft);
//        gameOver.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(gameOver);
    }

    private void youLost() {
        Intent gameOver = new Intent(this, GameOverActivity.class);
        gameOver.putExtra("WORD", wordToGuess);
        gameOver.putExtra("MSG", YOU_LOST);
        gameOver.putExtra("TRIES_LEFT", triesLeft);
//        gameOver.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(gameOver);
    }

}