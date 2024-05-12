package com.example.eventvault.servicios;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventvault.R;
import com.example.eventvault.modelo.PerfilBasico;
import com.example.eventvault.modelo.PerfilCreador;
import com.example.eventvault.vista.PoliticaPrivacidad;
import com.example.eventvault.vista.Registro;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EventVault extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_vault);

        mAuth = FirebaseAuth.getInstance();

        Button btnRegistro = findViewById(R.id.btnRegistro);
        Button btnContinuar = findViewById(R.id.button2);
        TextView textViewPoliticaPrivacidad = findViewById(R.id.textViewPoliticaPrivacidad);

        SharedPreferences sharedPreferences = getSharedPreferences("ColorBotones", MODE_PRIVATE);
        int color = sharedPreferences.getInt("ColorBotones", Color.WHITE); // Color blanco por defecto

        // Aplicar el color al botón
        btnRegistro.setBackgroundColor(color);
        btnContinuar.setBackgroundColor(color);

        textViewPoliticaPrivacidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirPoliticaPrivacidad(v); // Pasar el argumento 'v'
            }
        });

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent para iniciar la actividad Registro
                Intent intent = new Intent(EventVault.this, Registro.class);
                startActivity(intent);
            }
        });

        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ((EditText) findViewById(R.id.TxMail)).getText().toString();
                String password = ((EditText) findViewById(R.id.TxPass)).getText().toString();

                // Verificar las credenciales
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    String userID = mAuth.getCurrentUser().getUid();
                                    FirebaseFirestore.getInstance().collection("usuarios")
                                            .document(userID)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        if (document.exists()) {
                                                            String tipoUsuario = document.getString("TipoUsuario");
                                                            if ("Creador".equals(tipoUsuario)) {
                                                                startActivity(new Intent(EventVault.this, PerfilCreador.class));
                                                            } else {
                                                                startActivity(new Intent(EventVault.this, PerfilBasico.class));
                                                            }
                                                            finish();
                                                        } else {
                                                            Toast.makeText(EventVault.this, "El tipo de usuario no está definido", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } else {
                                                        Toast.makeText(EventVault.this, "Error al obtener el tipo de usuario", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else {
                                    Exception exception = task.getException();
                                    String errorMessage = exception != null ? exception.getMessage() : "Error desconocido";
                                    Toast.makeText(EventVault.this, "Credenciales no válidas", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    // Declara el método fuera del método onCreate
    public void abrirPoliticaPrivacidad(View view) {
        Intent intent = new Intent(this, PoliticaPrivacidad.class);
        startActivity(intent);
    }

}
