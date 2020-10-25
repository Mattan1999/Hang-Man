package com.example.hangman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private RadioButton swedish, english, light, dark;
    public static final String RADIOBUTTON_SAVE_STATE = "RadioButtonSave";
    boolean swedishBoolean, englishBoolean, lightMode, darkMode;
    private String[] myListOfWords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadLocale();
        loadTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        swedish = findViewById(R.id.rbSwedish);
        english = findViewById(R.id.rbEnglish);
        light = findViewById(R.id.rbLight);
        dark = findViewById(R.id.rbDark);

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
            light.setChecked(true);
        } else {
            dark.setChecked(true);
        }

        SharedPreferences prefs = getSharedPreferences(RADIOBUTTON_SAVE_STATE, MODE_PRIVATE);
        swedishBoolean = prefs.getBoolean("swe", true);
        englishBoolean = prefs.getBoolean("eng", false);
        lightMode = prefs.getBoolean("light", true);
        darkMode = prefs.getBoolean("dark", false);

        if (swedishBoolean) {
            swedish.setChecked(true);
            myListOfWords = new String[]{
                    "GLÖMMA", "PAUS", "FLYTTAT", "VÄNJA", "GJORDES",
                    "PEKA", "STOREBROR", "KONJAK", "SKAKADE", "SAMLATS",
                    "LÄPPSTIFT", "RABATT", "KONFLIKT", "JORDNÖTSSMÖR", "FUNKAR",
                    "FÄNGELSET", "SLÄPPTE", "INTERNATIONELLA", "TÄLTET", "HÅLAN"
            };
        } else if (englishBoolean) {
            english.setChecked(true);
            myListOfWords = new String[] {
                    "ASSERT", "KNOT", "CORRUPTION", "INTOXICANT", "AXIS",
                    "LITTLE", "CRAWLER", "KANGAROO", "SMILE", "CENTRAL",
                    "APPARENT", "FOREIGN", "CHARISMA", "TREMOR", "ALLOTMENT",
                    "PISTOL", "LULLABY", "BEAUTY", "AUTHORITY", "PILL"
            };
        }

        if (lightMode) {
            light.setChecked(true);
        } else if (darkMode) {
            dark.setChecked(true);
        }
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
        startGame.putExtra("words_array", myListOfWords);
        startActivity(startGame);
    }

    public void changeLanguage(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.rbSwedish: {
                if (checked) {
                    SharedPreferences.Editor editor = getSharedPreferences(RADIOBUTTON_SAVE_STATE, MODE_PRIVATE).edit();
                    editor.putBoolean("swe", swedish.isChecked());
                    editor.putBoolean("eng", english.isChecked());
                    editor.apply();
                    changeLang("sv");
                    Intent i = new Intent(this, MainActivity.class);
                    startActivity(i);
                    finish();
                    overridePendingTransition(0, 0);
                    break;
                }
            }
            case R.id.rbEnglish: {
                if (checked) {
                    SharedPreferences.Editor editor = getSharedPreferences(RADIOBUTTON_SAVE_STATE, MODE_PRIVATE).edit();
                    editor.putBoolean("eng", english.isChecked());
                    editor.putBoolean("swe", swedish.isChecked());
                    editor.apply();
                    changeLang("en");
                    Intent i = new Intent(this, MainActivity.class);
                    startActivity(i);
                    finish();
                    overridePendingTransition(0, 0);
                    break;
                }
            }
        }
    }

    public void changeTheme(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.rbLight: {
                if (checked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    break;
                }
            }
            case R.id.rbDark: {
                if (checked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    break;
                }
            }
        }
    }

    public void loadLocale() {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs", MODE_PRIVATE);
        String language = prefs.getString(langPref, "sv");
        changeLang(language);
    }

    public void changeLang(String lang) {
        if (lang.equalsIgnoreCase(""))
            return;
        Locale myLocale = new Locale(lang);
        saveLocale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    public void saveLocale(String lang) {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(langPref, lang);
        editor.apply();
    }

    public void loadTheme() {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
            setTheme(R.style.AppTheme);
        } else if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        }
    }

}