package com.example.eventvault;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PerfilCreador extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_creador);

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
                // Aquí implementa la lógica para cerrar la sesión
                cerrarSesion();

                // Después de cerrar la sesión, inicia la actividad EventVault
                startActivity(new Intent(PerfilCreador.this, EventVault.class));

                // Cierra la actividad actual (PerfilCreador)
                finish();
            }
        });
    }

    private void cerrarSesion() {
        // Crear una instancia del SessionManager
        SessionManager sessionManager = new SessionManager(getApplicationContext());

        // Llamar al método cerrarSesion() de SessionManager para realizar las acciones necesarias
        sessionManager.cerrarSesion();
    }
}
