package com.example.eventvault.modelo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.eventvault.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetallesEvento extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_evento);

        // Obtener los extras del Intent
        Intent intent = getIntent();
        String nombre = intent.getStringExtra("nombre");
        String descripcion = intent.getStringExtra("descripcion");
        long fecha = intent.getLongExtra("fecha", 0);
        String creador = intent.getStringExtra("creador");

        // Mostrar los detalles del evento en la interfaz de usuario
        TextView textViewNombre = findViewById(R.id.textViewNombre);
        TextView textViewDescripcion = findViewById(R.id.textViewDescripcion);
        TextView textViewFecha = findViewById(R.id.textViewFecha);
        TextView textViewCreador = findViewById(R.id.textViewCreador);

        textViewNombre.setText(nombre);
        textViewDescripcion.setText(descripcion);
        textViewCreador.setText(creador);

        // Convertir la fecha a un formato legible y mostrarla
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String fechaFormateada = sdf.format(new Date(fecha));
        textViewFecha.setText(fechaFormateada);
    }
}
