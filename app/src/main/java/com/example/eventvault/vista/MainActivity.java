package com.example.eventvault.vista;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.example.eventvault.servicios.EventVault;
import com.example.eventvault.R;

public class MainActivity extends AppCompatActivity {
    private static final int SPLASH_TIMEOUT = 2000;
    private static final String COLOR_BOTONES_PREF = "ColorBotones";
    private static final String COLOR_BOTONES_KEY = "ColorBotones";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Verificar si es la primera ejecución de la aplicación
        SharedPreferences sharedPreferences = getSharedPreferences(COLOR_BOTONES_PREF, MODE_PRIVATE);
        boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);

        if (isFirstRun) {
            // Guardar el color "azulmarino" en SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(COLOR_BOTONES_KEY, getResources().getColor(R.color.colorDefefectoBotones));
            editor.putBoolean("isFirstRun", false);
            editor.apply();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, EventVault.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIMEOUT);
    }
}
