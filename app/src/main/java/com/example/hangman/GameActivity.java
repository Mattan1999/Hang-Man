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
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.app_bar);
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
        wordToGuessTv.setText(printWordToGuess());
    }

    // Method returning randomly next word to find
    private String hiddenWordToFind() {
        return myListOfWords[random.nextInt(myListOfWords.length)];
    }

    private String printWordToGuess() {
        StringBuilder hiddenWord = new StringBuilder();
        for (int i = 0; i < lettersInWordFound.length; i++){
            hiddenWord.append(lettersInWordFound[i]);
            if (i < lettersInWordFound.length - 1) {
                hiddenWord.append(" ");
            }
        }
        return hiddenWord.toString();
    }

    private void revealWordToGuess(String letter) {
        int indexOfLetter = wordToGuess.indexOf(letter);
        while (indexOfLetter >= 0) {
            lettersInWordFound[indexOfLetter] = wordToGuess.charAt(indexOfLetter);
            indexOfLetter = wordToGuess.indexOf(letter, indexOfLetter + 1);
        }
        wordToGuessTv.setText(printWordToGuess());
    }

    private void updateImg(int play) {
        int resImg = getResources().getIdentifier("hangman_" + play, "drawable",
                getPackageName());
        img.setImageResource(resImg);
    }

    public void onGuessClick(View view){
        if (userErrors < MAX_ERRORS) {
            String input = userInput.getText().toString();
            try {
                if (input.length() < 2) {
                    if (letters.contains(input)) {
                        userInput.setText("");
                        Toast.makeText(this, R.string.character_already_exist,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        if (wordToGuess.contains(input)) {
                            revealWordToGuess(input);
                        } else {
                            userErrors++;
                        }
                        userInput.setText("");
                        letters.add(input);
                        wrongGuesses.setText(letters.toString());
                    }
                    wordToGuessTv.setText(printWordToGuess());
                    updateImg(userErrors);
                    checkGameStatus();
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

    private void checkGameStatus() {
        if (wordIsFound()) {
            Bundle bundle = new Bundle();
            bundle.putString("goto", "MainActivity");

            Intent gameOver = new Intent(this, GameOverActivity.class);
            gameOver.putExtras(bundle);
            startActivity(gameOver);
        } else {
            if (userErrors >= MAX_ERRORS) {
                Bundle bundle = new Bundle();
                bundle.putString("goto", "MainActivity");

                Intent gameOver = new Intent(this, GameOverActivity.class);
                gameOver.putExtras(bundle);
                startActivity(gameOver);
            }
        }
    }

    private boolean wordIsFound() {
        return wordToGuess.contentEquals(new String(lettersInWordFound));
    }

}