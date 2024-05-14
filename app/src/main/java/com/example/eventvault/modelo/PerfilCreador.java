package com.example.eventvault.modelo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.eventvault.vista.CreacionEvento;
import com.example.eventvault.vista.EditarEvento;
import com.example.eventvault.vista.EditarApp;
import com.example.eventvault.servicios.EventVault;
import com.example.eventvault.vista.EventosSemana;
import com.example.eventvault.R;
import com.example.eventvault.vista.MisEventos;
import com.google.firebase.auth.FirebaseAuth;

public class PerfilCreador extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_creador);

        mAuth = FirebaseAuth.getInstance();

        Button btnCerrarSesionCreador = findViewById(R.id.btnCerrarSesionCreador);
        Button btnCrearEvento = findViewById(R.id.btnCrearEvento);
        Button btnEventos = findViewById(R.id.btnEventos);
        Button btnEditEvento = findViewById(R.id.btnEditEvento);
        Button btnMisEventos = findViewById(R.id.btnMisEventos);
        Button btnPerf = findViewById(R.id.btnPerfil);

        SharedPreferences sharedPreferences = getSharedPreferences("ColorBotones", MODE_PRIVATE);
        int color = sharedPreferences.getInt("ColorBotones", Color.WHITE); // Color blanco por defecto

        // Aplicar el color al botón
        btnCerrarSesionCreador.setBackgroundColor(color);
        btnCrearEvento.setBackgroundColor(color);
        btnEventos.setBackgroundColor(color);
        btnEditEvento.setBackgroundColor(color);
        btnMisEventos.setBackgroundColor(color);
        btnPerf.setBackgroundColor(color);
        btnCrearEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfilCreador.this, CreacionEvento.class);
                startActivity(intent);
            }
        });

        btnEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfilCreador.this, EventosSemana.class);
                startActivity(intent);
            }
        });

        btnEditEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfilCreador.this, EditarEvento.class);
                startActivity(intent);
            }
        });

        btnMisEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfilCreador.this, MisEventos.class);
                startActivity(intent);
            }
        });

        btnPerf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfilCreador.this, EditarApp.class);
                startActivity(intent);
            }
        });

        btnCerrarSesionCreador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(PerfilCreador.this, EventVault.class));
                finish();
            }
        });
    }
}
