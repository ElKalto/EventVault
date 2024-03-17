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
        String ubicacion = intent.getStringExtra("ubicacion");
        long fecha = intent.getLongExtra("fecha", 0);
        String creador = intent.getStringExtra("creador");
        String nombreAsociacion = intent.getStringExtra("nombreAsociacion");

        // Mostrar los detalles del evento en la interfaz de usuario
        TextView textViewNombre = findViewById(R.id.textViewNombre);
        TextView textViewDescripcion = findViewById(R.id.textViewDescripcion);
        TextView textViewUbicacion = findViewById(R.id.textViewUbicacion);
        TextView textViewFecha = findViewById(R.id.textViewFecha);
        TextView textViewHora = findViewById(R.id.textViewHora);
        TextView textViewCreador = findViewById(R.id.textViewAsociacion);

        textViewNombre.setText(nombre);
        textViewDescripcion.setText(descripcion);
        textViewUbicacion.setText(ubicacion);
        textViewCreador.setText(nombreAsociacion);

        // Convertir la fecha y la hora a formatos legibles y mostrarlos
        SimpleDateFormat sdfFecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String fechaFormateada = sdfFecha.format(new Date(fecha));
        textViewFecha.setText(fechaFormateada);

        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String horaFormateada = sdfHora.format(new Date(fecha));
        textViewHora.setText(horaFormateada);
    }
}