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

        btnEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent para iniciar la actividad Eventos
                Intent intent = new Intent(PerfilBasico.this, EventosSemana.class);
                startActivity(intent);
            }
        });


    }
}
