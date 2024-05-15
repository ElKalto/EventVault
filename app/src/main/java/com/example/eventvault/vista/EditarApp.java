package com.example.eventvault.vista;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import com.example.eventvault.R;

public class EditarApp extends AppCompatActivity {

    private SeekBar seekBarColorRojoBotones, seekBarColorVerdeBotones, seekBarColorAzulBotones;
    private SeekBar seekBarColorRojoFuentes, seekBarColorVerdeFuentes, seekBarColorAzulFuentes;
    private Button btnAplicarColor;
    private ConstraintLayout constraintLayout;
    private TextView textViewTituloEdit, textViewColorBotones, textViewTamañoLetra, textViewColorTexto;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_app);

        textViewTituloEdit = findViewById(R.id.textViewTituloEdit);
        textViewColorTexto = findViewById(R.id.textViewColorTexto);
        textViewTamañoLetra = findViewById(R.id.textViewTamañoLetra);
        textViewColorBotones = findViewById(R.id.textViewColorBotones);

        seekBarColorRojoBotones = findViewById(R.id.seekBarColorRojoBotones);
        seekBarColorVerdeBotones = findViewById(R.id.seekBarColorVerdeBotones);
        seekBarColorAzulBotones = findViewById(R.id.seekBarColorAzulBotones);
        seekBarColorRojoFuentes = findViewById(R.id.seekBarRojoFuente);
        seekBarColorVerdeFuentes = findViewById(R.id.seekBarVerdeFuente);
        seekBarColorAzulFuentes = findViewById(R.id.seekBarAzulFuente);
        btnAplicarColor = findViewById(R.id.BtnAplicarColor);
        constraintLayout = findViewById(R.id.constraintLayout);

        // Agregar listeners a los SeekBars de los botones y los textos
        seekBarColorRojoBotones.setOnSeekBarChangeListener(seekBarChangeListener);
        seekBarColorVerdeBotones.setOnSeekBarChangeListener(seekBarChangeListener);
        seekBarColorAzulBotones.setOnSeekBarChangeListener(seekBarChangeListener);
        seekBarColorRojoFuentes.setOnSeekBarChangeListener(seekBarChangeListener);
        seekBarColorVerdeFuentes.setOnSeekBarChangeListener(seekBarChangeListener);
        seekBarColorAzulFuentes.setOnSeekBarChangeListener(seekBarChangeListener);

        btnAplicarColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aplicarColorBotones();
                aplicarColorFuentes();
            }
        });

        // Recuperar el color guardado en SharedPreferences y aplicarlo a los elementos de texto
        SharedPreferences sharedPreferences = getSharedPreferences("ColorTextos", MODE_PRIVATE);
        int colortexto = sharedPreferences.getInt("ColorTextos", Color.BLACK); // Color por defecto si no se encuentra

        // Llamar al método para aplicar el color a los textos en toda la aplicación
        aplicarColorFuentes(colortexto);
    }

    // Listener para los SeekBars de los botones y los textos
    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (seekBar == seekBarColorRojoBotones || seekBar == seekBarColorVerdeBotones || seekBar == seekBarColorAzulBotones) {
                // Aplicar el color de los botones según el progreso de los SeekBars
                aplicarColorBotones();
            } else if (seekBar == seekBarColorRojoFuentes || seekBar == seekBarColorVerdeFuentes || seekBar == seekBarColorAzulFuentes) {
                // Aplicar el color de los textos según el progreso de los SeekBars
                aplicarColorFuentes();
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // No necesitamos implementar esta función
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // No necesitamos implementar esta función
        }
    };

    private void aplicarColorBotones() {
        int red = seekBarColorRojoBotones.getProgress();
        int green = seekBarColorVerdeBotones.getProgress();
        int blue = seekBarColorAzulBotones.getProgress();

        int color = Color.rgb(red, green, blue);

        // Aplicar el color a los botones y guardar en SharedPreferences para uso futuro
        SharedPreferences sharedPreferences = getSharedPreferences("ColorBotones", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("ColorBotones", color);
        editor.apply();

        btnAplicarColor.setBackgroundColor(color);
    }

    private void aplicarColorFuentes() {
        // Obtener los valores de progreso de los SeekBars
        int red = seekBarColorRojoFuentes.getProgress();
        int green = seekBarColorVerdeFuentes.getProgress();
        int blue = seekBarColorAzulFuentes.getProgress();

        // Crear el color con los valores obtenidos
        int colortexto = Color.rgb(red, green, blue);

        // Llamar al método para aplicar el color a los textos en toda la aplicación
        aplicarColorFuentes(colortexto);
    }

    // Método para aplicar el color a los textos en toda la aplicación
    private void aplicarColorFuentes(int colortexto) {
        // Aplicar el color a los TextViews correspondientes
        textViewTituloEdit.setTextColor(colortexto);
        textViewColorBotones.setTextColor(colortexto);
        textViewTamañoLetra.setTextColor(colortexto);
        textViewColorTexto.setTextColor(colortexto);
        // Añade más TextViews aquí si es necesario

        // Guardar el color en SharedPreferences para uso futuro
        SharedPreferences sharedPreferences = getSharedPreferences("ColorTextos", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("ColorTextos", colortexto);
        editor.apply();
    }
}
