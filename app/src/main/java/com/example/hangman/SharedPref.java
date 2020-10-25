package com.example.hangman;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    SharedPreferences mySharedPref;

    public SharedPref(Context context) {
        mySharedPref = context.getSharedPreferences("filename", Context.MODE_PRIVATE);
    }

    public void setNightModeState(boolean state) {
        SharedPreferences.Editor editor = mySharedPref.edit();
        editor.putBoolean("NightMode", state);
        editor.apply();
    }

    public Boolean loadNightModeState() {
        Boolean state = mySharedPref.getBoolean("NightMode", false);
        return state;
    }
}
