package com.example.eventvault;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EventVault extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_vault);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Button btnRegistro = findViewById(R.id.btnRegistro);
        Button btnContinuar = findViewById(R.id.button2);

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
                                    // Inicio de sesión exitoso, redirigir al usuario según el tipo
                                    Toast.makeText(EventVault.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(EventVault.this, PerfilBasico.class));
                                    finish();
                                } else {
                                    // Mostrar mensaje de error si las credenciales no son válidas
                                    Toast.makeText(EventVault.this, "Credenciales no válidas", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    // Método para registrar un nuevo usuario en Firebase Authentication
    public void registrarUsuario(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registro exitoso, redirigir al usuario según el tipo
                            Toast.makeText(EventVault.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(EventVault.this, PerfilBasico.class));
                            finish();
                        } else {
                            // Si falla el registro, mostrar un mensaje al usuario.
                            Toast.makeText(EventVault.this, "Fallo en el registro.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Método para agregar datos adicionales del usuario a Firestore
    public void agregarDatosUsuario(String email, String nombreAsociacion, boolean esCreador) {
        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("nombreAsociacion", nombreAsociacion);
        user.put("esCreador", esCreador);

        // Add a new document with a generated ID
        db.collection("usuarios")
                .add(user)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            // Documento agregado exitosamente
                            Toast.makeText(EventVault.this, "Datos de usuario agregados a Firestore", Toast.LENGTH_SHORT).show();
                        } else {
                            // Error al agregar documento
                            Toast.makeText(EventVault.this, "Error al agregar datos de usuario a Firestore", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
