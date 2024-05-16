package com.example.eventvault.modelo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.eventvault.R;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetallesEvento extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_evento);

        // Recuperar el color del texto desde SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("ColorTextos", MODE_PRIVATE);
        int colorTexto = sharedPreferences.getInt("ColorTextos", Color.BLACK); // Color negro por defecto

        Intent intent = getIntent();

        // Log para verificar los datos recibidos
        Log.d("DetallesEvento", "Intent recibido: " + intent.getExtras());

        String nombre = intent.getStringExtra("nombre");
        String descripcion = intent.getStringExtra("descripcion");
        String ubicacion = intent.getStringExtra("ubicacion");
        long fecha = intent.getLongExtra("fecha", 0); // Asegúrate de obtener correctamente el tiempo en milisegundos
        String nombreAsociacion = intent.getStringExtra("nombreAsociacion");

        TextView textViewNombre = findViewById(R.id.textViewNombre);
        TextView textViewDescripcion = findViewById(R.id.textViewDescripcion);
        TextView textViewUbicacion = findViewById(R.id.textViewUbicacion);
        TextView textViewFecha = findViewById(R.id.textViewFecha);
        TextView textViewHora = findViewById(R.id.textViewHora);
        TextView textViewAsociacion = findViewById(R.id.textViewAsociacion);

        // Aplicar el color a los TextView
        textViewNombre.setTextColor(colorTexto);
        textViewDescripcion.setTextColor(colorTexto);
        textViewUbicacion.setTextColor(colorTexto);
        textViewFecha.setTextColor(colorTexto);
        textViewHora.setTextColor(colorTexto);
        textViewAsociacion.setTextColor(colorTexto);

        textViewNombre.setText(nombre != null ? nombre : "Sin nombre");
        textViewDescripcion.setText(descripcion != null ? descripcion : "Sin descripción");
        textViewUbicacion.setText(ubicacion != null ? ubicacion : "Sin ubicación");
        textViewAsociacion.setText(nombreAsociacion != null ? nombreAsociacion : "Sin asociación");

        if (fecha > 0) {
            SimpleDateFormat sdfFecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm", Locale.getDefault());

            textViewFecha.setText(sdfFecha.format(new Date(fecha)));
            textViewHora.setText(sdfHora.format(new Date(fecha)));
        } else {
            textViewFecha.setText("Sin fecha");
            textViewHora.setText("Sin hora");
        }
    }
}
