package com.example.eventvault.vista;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.example.eventvault.R;

public class EditarPerfil extends AppCompatActivity {

    private Button btnAtras;
    private SeekBar seekBarRed, seekBarGreen, seekBarBlue;
    private Button BtnAplicarColor;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        btnAtras = findViewById(R.id.BtnAtras);
        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finalizar la actividad actual
                finish();
            }
        });

        seekBarRed = findViewById(R.id.seekBarRed);
        seekBarGreen = findViewById(R.id.seekBarGreen);
        seekBarBlue = findViewById(R.id.seekBarBlue);
        BtnAplicarColor = findViewById(R.id.BtnAplicarColor);
        constraintLayout = findViewById(R.id.constraintLayout);

        BtnAplicarColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aplicarColorBotones();
            }
        });
    }

    private void aplicarColorBotones() {
        int red = seekBarRed.getProgress();
        int green = seekBarGreen.getProgress();
        int blue = seekBarBlue.getProgress();

        int color = Color.rgb(red, green, blue);

        SharedPreferences sharedPreferences = getSharedPreferences("ColorBotones", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("ColorBotones", color);
        editor.apply();

        btnAtras.setBackgroundColor(color);
        BtnAplicarColor.setBackgroundColor(color);
    }



    private void AplicarColorBoton() {
        int red = seekBarRed.getProgress();
        int green = seekBarGreen.getProgress();
        int blue = seekBarBlue.getProgress();
        int color = Color.rgb(red, green, blue);

        // Guarda el color en preferencias compartidas
        SharedPreferences sharedPreferences = getSharedPreferences("ButtonColorPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("buttonColor", color);
        editor.apply();

        btnAtras.setBackgroundColor(color);
    }
}