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
import com.example.eventvault.R;

public class EditarApp extends AppCompatActivity {

    private SeekBar seekBarColorRojoBotones, seekBarColorVerdeBotones, seekBarColorAzulBotones;
    private SeekBar seekBarColorRojoFuentes, seekBarColorVerdeFuentes, seekBarColorAzulFuentes;
    private Button btnAplicarColor;
    private ConstraintLayout constraintLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_app);

        seekBarColorRojoBotones = findViewById(R.id.seekBarColorRojoBotones);
        seekBarColorVerdeBotones = findViewById(R.id.seekBarColorVerdeBotones);
        seekBarColorAzulBotones = findViewById(R.id.seekBarColorAzulBotones);
        seekBarColorRojoFuentes = findViewById(R.id.seekBarTamLetra);
        seekBarColorVerdeFuentes = findViewById(R.id.seekBarBlueFuente);
        seekBarColorAzulFuentes = findViewById(R.id.seekBarRedFuente);
        btnAplicarColor = findViewById(R.id.BtnAplicarColor);
        constraintLayout = findViewById(R.id.constraintLayout);

        // Agregar listeners a los SeekBars de los botones
        seekBarColorRojoBotones.setOnSeekBarChangeListener(seekBarChangeListener);
        seekBarColorVerdeBotones.setOnSeekBarChangeListener(seekBarChangeListener);
        seekBarColorAzulBotones.setOnSeekBarChangeListener(seekBarChangeListener);

        btnAplicarColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aplicarColorBotones();
                aplicarColorFuentes();
            }
        });
    }

    // Listener para los SeekBars de los botones
    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // Aplicar el color de los botones según el progreso de los SeekBars
            aplicarColorBotones();
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

        SharedPreferences sharedPreferences = getSharedPreferences("ColorBotones", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("ColorBotones", color);
        editor.apply();

        btnAplicarColor.setBackgroundColor(color);
    }

    private void aplicarColorFuentes() {
        int red = seekBarColorRojoFuentes.getProgress();
        int green = seekBarColorVerdeFuentes.getProgress();
        int blue = seekBarColorAzulFuentes.getProgress();

        int color = Color.rgb(red, green, blue);

        // Aquí debes aplicar el color a las fuentes de tu aplicación, por ejemplo, a través de estilos o directamente a los TextViews
    }
}
