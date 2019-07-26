package com.example.coursmanager.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.coursmanager.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences sharedPref = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String currentTheme = sharedPref.getString("theme", "light");

        switch (currentTheme){
            case "light":
                setTheme(R.style.AppTheme_Light);
                break;
            case "dark":
                setTheme(R.style.AppTheme_Dark);
                break;
            default:
                break;
        }

        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Switch switchDarkTheme = findViewById(R.id.darkTheme);
        switchDarkTheme.setChecked(currentTheme.equals("dark"));
        switchDarkTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked)
                    sharedPref.edit().putString("theme", "dark").apply();
                else
                    sharedPref.edit().putString("theme", "light").apply();

                recreate();
            }
        });
    }
}
