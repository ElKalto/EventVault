package com.example.eventvault.modelo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.eventvault.EditarPerfil;
import com.example.eventvault.EventVault;
import com.example.eventvault.EventosSemana;
import com.example.eventvault.R;
import com.google.firebase.auth.FirebaseAuth;

public class PerfilBasico extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_basico);

        mAuth = FirebaseAuth.getInstance();

        Button btnEventos = findViewById(R.id.btnEventos);
        Button btnPerf = findViewById(R.id.btnPerfil);
        Button btnCerrarSesionCreador = findViewById(R.id.btnCerrarSesionCreador);

        SharedPreferences sharedPreferences = getSharedPreferences("ColorBotones", MODE_PRIVATE);
        int color = sharedPreferences.getInt("ColorBotones", Color.WHITE); // Color blanco por defecto

        btnCerrarSesionCreador.setBackgroundColor(color);
        btnEventos.setBackgroundColor(color);
        btnPerf.setBackgroundColor(color);

        btnEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent para iniciar la actividad Eventos
                Intent intent = new Intent(PerfilBasico.this, EventosSemana.class);
                startActivity(intent);
            }
        });

        btnPerf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfilBasico.this, EditarPerfil.class);
                startActivity(intent);
            }
        });
        btnCerrarSesionCreador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();

                Intent intent = new Intent(PerfilBasico.this, EventVault.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Borra todas las actividades anteriores
                startActivity(intent);
                finish();
            }
        });
    }
}
