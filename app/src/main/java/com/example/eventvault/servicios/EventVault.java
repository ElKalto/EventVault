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
        Button btnContinuar = findViewById(R.id.buttonContinuar);

        SharedPreferences sharedPreferences = getSharedPreferences("ColorBotones", MODE_PRIVATE);
        int color = sharedPreferences.getInt("ColorBotones", Color.BLUE); // Color azul por defecto

        TextView textViewPoliticaPrivacidad = findViewById(R.id.textViewPoliticaPrivacidad);
        TextView TxMail = findViewById(R.id.TxMail);
        TextView TxPass = findViewById(R.id.TxPass);

        EditText edtMail = findViewById(R.id.TxMail);
        EditText edtPass = findViewById(R.id.TxPass);

        textViewPoliticaPrivacidad.setTextColor(color);
        TxMail.setTextColor(color);
        TxPass.setTextColor(color);

// Aplicar el color a los hints de los EditText
        edtMail.setHintTextColor(color);
        edtPass.setHintTextColor(color);

        // Aplicar el color al botón
        btnRegistro.setBackgroundColor(color);
        btnContinuar.setBackgroundColor(color);

        textViewPoliticaPrivacidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirPoliticaPrivacidad(); // Llamada al método sin pasar el argumento 'v'
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

                // Verificar si los campos de correo electrónico y contraseña están vacíos
                if (email.isEmpty() || password.isEmpty()) {
                    // Mostrar un mensaje de error indicando que los campos son obligatorios
                    Toast.makeText(EventVault.this, "Por favor, ingresa tu correo electrónico y contraseña", Toast.LENGTH_SHORT).show();
                } else {
                    // Continuar con el proceso de autenticación
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Verificar el tipo de usuario y redirigir
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
                                        // Mensaje de error general
                                        Toast.makeText(EventVault.this, "Datos erróneos", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    // Método para abrir la actividad de política de privacidad
    public void abrirPoliticaPrivacidad() {
        Intent intent = new Intent(this, PoliticaPrivacidad.class);
        startActivity(intent);
    }
}
