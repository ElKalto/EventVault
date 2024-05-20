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
    private boolean isUserLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_vault);

        mAuth = FirebaseAuth.getInstance();

        // Comprobar si el usuario ya está conectado
        if (mAuth.getCurrentUser() != null) {
            isUserLoggedIn = true;
            redirectToProfile();
        }

        Button btnRegistro = findViewById(R.id.btnRegistro);
        Button btnContinuar = findViewById(R.id.buttonContinuar);

        // Recuperar el color de los botones desde SharedPreferences
        SharedPreferences btnSharedPreferences = getSharedPreferences("ColorBotones", MODE_PRIVATE);
        int colorBotones = btnSharedPreferences.getInt("ColorBotones", Color.BLUE); // Color azul por defecto

        // Aplicar el color a los botones
        btnRegistro.setBackgroundColor(colorBotones);
        btnContinuar.setBackgroundColor(colorBotones);

        // Recuperar el color del texto desde SharedPreferences
        SharedPreferences textSharedPreferences = getSharedPreferences("ColorTextos", MODE_PRIVATE);
        int colorTextos = textSharedPreferences.getInt("ColorTextos", Color.BLACK); // Color negro por defecto

        TextView textViewPoliticaPrivacidad = findViewById(R.id.textViewPoliticaPrivacidad);
        TextView TxMail = findViewById(R.id.TxMail);
        TextView TxPass = findViewById(R.id.TxPass);

        EditText edtMail = findViewById(R.id.TxMail);
        EditText edtPass = findViewById(R.id.TxPass);

        // Aplicar el color a los TextView
        textViewPoliticaPrivacidad.setTextColor(colorTextos);
        TxMail.setTextColor(colorTextos);
        TxPass.setTextColor(colorTextos);

        // Aplicar el color a los hints de los EditText
        edtMail.setHintTextColor(colorTextos);
        edtPass.setHintTextColor(colorTextos);

        textViewPoliticaPrivacidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirPoliticaPrivacidad(); // Llamada al método sin pasar el argumento 'v'
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
                                        redirectToProfile();
                                    } else {
                                        // Mensaje de error general
                                        Toast.makeText(EventVault.this, "Datos erróneos", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });


        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para iniciar la actividad Registro
                Intent intent = new Intent(EventVault.this, Registro.class);
                startActivity(intent); // Iniciar la actividad Registro
            }
        });
    }

    // Método para abrir la actividad de política de privacidad
    public void abrirPoliticaPrivacidad() {
        Intent intent = new Intent(this, PoliticaPrivacidad.class);
        startActivity(intent);
    }

    private void redirectToProfile() {
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
                                Class destinationActivity;
                                if ("Creador".equals(tipoUsuario)) {
                                    destinationActivity = PerfilCreador.class;
                                } else {
                                    destinationActivity = PerfilBasico.class;
                                }
                                startActivity(new Intent(EventVault.this, destinationActivity));
                                finish();
                            } else {
                                Toast.makeText(EventVault.this, "El tipo de usuario no está definido", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(EventVault.this, "Error al obtener el tipo de usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
