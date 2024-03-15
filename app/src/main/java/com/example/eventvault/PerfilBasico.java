package com.example.eventvault;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class PerfilBasico extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_basico);

        mAuth = FirebaseAuth.getInstance();

        Button btnEventos = findViewById(R.id.btnEventos);
        Button btnCalenEvent = findViewById(R.id.btnCalenEvent);
        Button btnPerf = findViewById(R.id.btnPerfil);
        Button btnCerrarSesionCreador = findViewById(R.id.btnCerrarSesionCreador);

        btnEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent para iniciar la actividad Eventos
                Intent intent = new Intent(PerfilBasico.this, EventosSemana.class);
                startActivity(intent);
            }
        });

        btnCalenEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para el botón Calendario de Eventos
                // Agrega aquí la lógica para abrir el calendario de eventos

                // Crear un Intent para abrir la actividad del calendario de eventos
                Intent intent = new Intent(PerfilBasico.this, CalendarioEventos.class);
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
                // Cerrar sesión con Firebase Auth
                mAuth.signOut();

                // Redirigir al usuario a la pantalla EventVault
                Intent intent = new Intent(PerfilBasico.this, EventVault.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Borra todas las actividades anteriores
                startActivity(intent);
                finish(); // Cierra la actividad actual
            }
        });
    }
}
