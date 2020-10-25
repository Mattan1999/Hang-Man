package com.example.hangman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.PorterDuff;
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


    public static String[] myListOfWords;
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
    SharedPref sharedPref;
    private Button a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,aa,ab,ac;


    void initializeGame() {
        img = findViewById(R.id.img);
        txtTriesLeft = findViewById(R.id.remaining_tries);
        txtWordToGuess = findViewById(R.id.word_to_find);
        txtWrongGuesses = findViewById(R.id.wrong_guesses);
        YOU_WON = getText(R.string.you_won).toString();
        YOU_LOST = getText(R.string.you_lost).toString();
        myListOfWords = getIntent().getStringArrayExtra("words_array");
        MESSAGE_WITH_LETTERS_TRIED = getText(R.string.wrong_guesses).toString() + " ";
        wordToGuess = hiddenWordToFind();
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
        sharedPref = new SharedPref(this);
        loadTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        a = findViewById(R.id.a);
        b = findViewById(R.id.b);
        c = findViewById(R.id.c);
        d = findViewById(R.id.d);
        e = findViewById(R.id.e);
        f = findViewById(R.id.f);
        g = findViewById(R.id.g);
        h = findViewById(R.id.h);
        i = findViewById(R.id.i);
        j = findViewById(R.id.j);
        k = findViewById(R.id.k);
        l = findViewById(R.id.l);
        m = findViewById(R.id.m);
        n = findViewById(R.id.n);
        o = findViewById(R.id.o);
        p = findViewById(R.id.p);
        q = findViewById(R.id.q);
        r = findViewById(R.id.r);
        s = findViewById(R.id.s);
        t = findViewById(R.id.t);
        u = findViewById(R.id.u);
        v = findViewById(R.id.v);
        w = findViewById(R.id.w);
        x = findViewById(R.id.x);
        y = findViewById(R.id.y);
        z = findViewById(R.id.z);
        aa = findViewById(R.id.aa);
        ab = findViewById(R.id.ab);
        ac = findViewById(R.id.ac);
        if (sharedPref.loadNightModeState()){
            setDarkKeyboardTheme();
        }
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
    private String hiddenWordToFind() {
        return myListOfWords[RANDOM.nextInt(myListOfWords.length)];
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
        gameOver.putExtra("words_array", myListOfWords);
//        gameOver.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(gameOver);
    }

    private void youLost() {
        Intent gameOver = new Intent(this, GameOverActivity.class);
        gameOver.putExtra("WORD", wordToGuess);
        gameOver.putExtra("MSG", YOU_LOST);
        gameOver.putExtra("TRIES_LEFT", triesLeft);
        gameOver.putExtra("words_array", myListOfWords);
//        gameOver.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(gameOver);
    }

    public void loadTheme() {
        if (!sharedPref.loadNightModeState()) {
            setTheme(R.style.AppTheme);
        } else if (sharedPref.loadNightModeState()) {
            setTheme(R.style.DarkTheme);
        }
    }

    private void setDarkKeyboardTheme() {
        a.getBackground().setColorFilter(ContextCompat.getColor(this,
                R.color.darkButtonColor), PorterDuff.Mode.MULTIPLY);
        b.getBackground().setColorFilter(ContextCompat.getColor(this,
                R.color.darkButtonColor), PorterDuff.Mode.MULTIPLY);
        c.getBackground().setColorFilter(ContextCompat.getColor(this,
                R.color.darkButtonColor), PorterDuff.Mode.MULTIPLY);
        d.getBackground().setColorFilter(ContextCompat.getColor(this,
                R.color.darkButtonColor), PorterDuff.Mode.MULTIPLY);
        e.getBackground().setColorFilter(ContextCompat.getColor(this,
                R.color.darkButtonColor), PorterDuff.Mode.MULTIPLY);
        f.getBackground().setColorFilter(ContextCompat.getColor(this,
                R.color.darkButtonColor), PorterDuff.Mode.MULTIPLY);
        g.getBackground().setColorFilter(ContextCompat.getColor(this,
                R.color.darkButtonColor), PorterDuff.Mode.MULTIPLY);
        h.getBackground().setColorFilter(ContextCompat.getColor(this,
                R.color.darkButtonColor), PorterDuff.Mode.MULTIPLY);
        i.getBackground().setColorFilter(ContextCompat.getColor(this,
                R.color.darkButtonColor), PorterDuff.Mode.MULTIPLY);
        j.getBackground().setColorFilter(ContextCompat.getColor(this,
                R.color.darkButtonColor), PorterDuff.Mode.MULTIPLY);
        k.getBackground().setColorFilter(ContextCompat.getColor(this,
                R.color.darkButtonColor), PorterDuff.Mode.MULTIPLY);
        l.getBackground().setColorFilter(ContextCompat.getColor(this,
                R.color.darkButtonColor), PorterDuff.Mode.MULTIPLY);
        m.getBackground().setColorFilter(ContextCompat.getColor(this,
                R.color.darkButtonColor), PorterDuff.Mode.MULTIPLY);
        n.getBackground().setColorFilter(ContextCompat.getColor(this,
                R.color.darkButtonColor), PorterDuff.Mode.MULTIPLY);
        o.getBackground().setColorFilter(ContextCompat.getColor(this,
                R.color.darkButtonColor), PorterDuff.Mode.MULTIPLY);
        p.getBackground().setColorFilter(ContextCompat.getColor(this,
                R.color.darkButtonColor), PorterDuff.Mode.MULTIPLY);
        q.getBackground().setColorFilter(ContextCompat.getColor(this,
                R.color.darkButtonColor), PorterDuff.Mode.MULTIPLY);
        r.getBackground().setColorFilter(ContextCompat.getColor(this,
                R.color.darkButtonColor), PorterDuff.Mode.MULTIPLY);
        s.getBackground().setColorFilter(ContextCompat.getColor(this,
                R.color.darkButtonColor), PorterDuff.Mode.MULTIPLY);
        t.getBackground().setColorFilter(ContextCompat.getColor(this,
                R.color.darkButtonColor), PorterDuff.Mode.MULTIPLY);
        u.getBackground().setColorFilter(ContextCompat.getColor(this,
                R.color.darkButtonColor), PorterDuff.Mode.MULTIPLY);
        v.getBackground().setColorFilter(ContextCompat.getColor(this,
                R.color.darkButtonColor), PorterDuff.Mode.MULTIPLY);
        w.getBackground().setColorFilter(ContextCompat.getColor(this,
                R.color.darkButtonColor), PorterDuff.Mode.MULTIPLY);
        x.getBackground().setColorFilter(ContextCompat.getColor(this,
                R.color.darkButtonColor), PorterDuff.Mode.MULTIPLY);
        y.getBackground().setColorFilter(ContextCompat.getColor(this,
                R.color.darkButtonColor), PorterDuff.Mode.MULTIPLY);
        z.getBackground().setColorFilter(ContextCompat.getColor(this,
                R.color.darkButtonColor), PorterDuff.Mode.MULTIPLY);
        aa.getBackground().setColorFilter(ContextCompat.getColor(this,
                R.color.darkButtonColor), PorterDuff.Mode.MULTIPLY);
        ab.getBackground().setColorFilter(ContextCompat.getColor(this,
                R.color.darkButtonColor), PorterDuff.Mode.MULTIPLY);
        ac.getBackground().setColorFilter(ContextCompat.getColor(this,
                R.color.darkButtonColor), PorterDuff.Mode.MULTIPLY);
    }

}