package com.example.eventvault.vista;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import com.example.eventvault.R;

public class PoliticaPrivacidad extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_politica_privacidad);

        // Recuperar el color guardado en SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("ColorTextos", MODE_PRIVATE);
        int colorTexto = sharedPreferences.getInt("ColorTextos", Color.WHITE); // Color blanco por defecto

        // Aplicar el color a los elementos de texto
        TextView textViewTitulo = findViewById(R.id.textViewTitulo);
        TextView textViewContenido = findViewById(R.id.textViewContenido);
        textViewTitulo.setTextColor(colorTexto);
        textViewContenido.setTextColor(colorTexto);
    }
}
