package com.example.eventvault.vista;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventvault.R;
import com.example.eventvault.modelo.PerfilBasico;
import com.example.eventvault.modelo.PerfilCreador;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity {

    private EditText editTextMailReg, editTextPassword, edTextPassRepReg, edTextNomAsoReg;
    private CheckBox checkBoxReg;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        mAuth = FirebaseAuth.getInstance();

        editTextMailReg = findViewById(R.id.edTextMailReg);
        editTextPassword = findViewById(R.id.editTextPassword);
        edTextPassRepReg = findViewById(R.id.edTextPassRepReg);
        edTextNomAsoReg = findViewById(R.id.edTextNomAsoReg);
        checkBoxReg = findViewById(R.id.checkBoxReg);

        Button btnAcpReg = findViewById(R.id.btnAcpReg);
        final TextView textView4 = findViewById(R.id.txtViewAsociacion);
        final TextView textView7 = findViewById(R.id.txtViewNombAsoc);

        edTextNomAsoReg.setVisibility(View.GONE);

        checkBoxReg.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                textView7.setVisibility(View.VISIBLE);
                edTextNomAsoReg.setVisibility(View.VISIBLE);
            } else {
                textView7.setVisibility(View.GONE);
                edTextNomAsoReg.setVisibility(View.GONE);
            }
        });

        btnAcpReg.setOnClickListener(view -> {
            if (validarCampos()) {
                boolean esCreador = checkBoxReg.isChecked();
                registrarUsuario(esCreador);
            }
        });
    }

    private boolean validarCampos() {
        if (editTextMailReg.getText().toString().isEmpty() ||
                editTextPassword.getText().toString().isEmpty() ||
                edTextPassRepReg.getText().toString().isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!editTextPassword.getText().toString().equals(edTextPassRepReg.getText().toString())) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (checkBoxReg.isChecked() && edTextNomAsoReg.getText().toString().isEmpty()) {
            Toast.makeText(this, "Ingrese el nombre de la asociación o empresa", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void registrarUsuario(final boolean esCreador) {
        String email = editTextMailReg.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        String tipoUsuario = esCreador ? "Creador" : "Basico";

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String userID = mAuth.getCurrentUser().getUid();
                            Map<String, Object> user = new HashMap<>();
                            user.put("TipoUsuario", tipoUsuario);

                            if (esCreador) {
                                String nombreAsociacion = edTextNomAsoReg.getText().toString().trim();
                                user.put("Asociacion", nombreAsociacion);
                            }

                            FirebaseFirestore.getInstance().collection("usuarios")
                                    .document(userID)
                                    .set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            if (esCreador) {
                                                startActivity(new Intent(Registro.this, PerfilCreador.class));
                                            } else {
                                                startActivity(new Intent(Registro.this, PerfilBasico.class));
                                            }
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Registro.this, "Error al registrar usuario en la base de datos", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(Registro.this, "Fallo en el registro.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
