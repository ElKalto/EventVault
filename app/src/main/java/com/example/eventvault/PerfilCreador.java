package com.example.eventvault;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
        Button btnEventos = findViewById(R.id.btnEventos);  // Agregado

        btnCrearEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent para iniciar la actividad CreacionEvento
                Intent intent = new Intent(PerfilCreador.this, CreacionEvento.class);
                startActivity(intent);
            }
        });

        btnEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent para iniciar la actividad EventosSemana
                Intent intent = new Intent(PerfilCreador.this, EventosSemana.class);
                startActivity(intent);
            }
        });

        btnCerrarSesionCreador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cerrar sesión con Firebase Auth
                mAuth.signOut();

                // Después de cerrar la sesión, inicia la actividad EventVault
                startActivity(new Intent(PerfilCreador.this, EventVault.class));

                // Cierra la actividad actual (PerfilCreador)
                finish();
            }
        });
    }
}
