package com.example.eventvault;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class CreacionEvento extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creacion_evento);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Button btnAceptarCreacion = findViewById(R.id.btnAceptarCreacion);
        final EditText editTextNombreEvento = findViewById(R.id.editTextText3);
        final EditText editTextDescripcionEvento = findViewById(R.id.editTextText4);
        final CalendarView calendarView = findViewById(R.id.calendarView);

        btnAceptarCreacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener datos del evento
                String nombreEvento = editTextNombreEvento.getText().toString();
                String descripcionEvento = editTextDescripcionEvento.getText().toString();
                long fechaEvento = calendarView.getDate(); // Obtener la fecha en milisegundos

                // Crear un objeto Evento con los datos del evento
                Evento nuevoEvento = new Evento(nombreEvento, descripcionEvento, fechaEvento);

                // Guardar el evento en Firestore
                guardarEventoEnFirestore(nuevoEvento);
            }
        });
    }

    private void guardarEventoEnFirestore(Evento nuevoEvento) {
        // Generar un ID único para el evento
        String eventoId = UUID.randomUUID().toString();

        // Guardar el evento en Firestore
        db.collection("eventos").document(eventoId)
                .set(nuevoEvento)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CreacionEvento.this, "Evento guardado con éxito", Toast.LENGTH_SHORT).show();
                        finish(); // Cerrar la actividad después de guardar el evento
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreacionEvento.this, "Error al guardar el evento", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
