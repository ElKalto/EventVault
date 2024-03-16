package com.example.eventvault;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

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
        // Obtener los valores de progreso de las barras de búsqueda roja, verde y azul
        int red = seekBarRed.getProgress();
        int green = seekBarGreen.getProgress();
        int blue = seekBarBlue.getProgress();

        // Calcular el color combinado
        int color = Color.rgb(red, green, blue);

        // Guardar el color en preferencias compartidas
        SharedPreferences sharedPreferences = getSharedPreferences("ColorBotones", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("ColorBotones", color);
        editor.apply();

        // Aplicar el color al botón actual
        btnAtras.setBackgroundColor(color);
        BtnAplicarColor.setBackgroundColor(color);
        // También puedes aplicar el color a otros botones si lo deseas
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

        // Aplica el color al botón actual
        btnAtras.setBackgroundColor(color);
        // También puedes aplicar el color a otros botones si los tienes
    }
}
