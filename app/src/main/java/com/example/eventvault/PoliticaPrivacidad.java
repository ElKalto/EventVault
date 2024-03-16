package com.example.eventvault;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PoliticaPrivacidad extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_politica_privacidad);
        Button btnVolver = findViewById(R.id.btnVolver);
        SharedPreferences sharedPreferences = getSharedPreferences("ColorBotones", MODE_PRIVATE);
        int color = sharedPreferences.getInt("ColorBotones", Color.WHITE); // Color blanco por defecto

        // Aplicar el color al botón
        btnVolver.setBackgroundColor(color);

        // Agregar un OnClickListener al botón
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la actividad EventVault
                Intent intent = new Intent(PoliticaPrivacidad.this, EventVault.class);
                startActivity(intent);
                // Finalizar la actividad actual
                finish();
            }
        });
    }
}
