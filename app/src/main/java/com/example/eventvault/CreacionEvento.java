package com.example.eventvault;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventvault.modelo.Evento;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Calendar;
import java.util.UUID;

public class CreacionEvento extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private EditText editTextNombreEvento;
    private EditText editTextDescripcionEvento;
    private EditText editTextUbicacionEvento;
    private CalendarView calendarView;
    private TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creacion_evento);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        editTextNombreEvento = findViewById(R.id.edtTextNombreEvento);
        editTextDescripcionEvento = findViewById(R.id.edtTextDescripcion);
        editTextUbicacionEvento = findViewById(R.id.edtTextUbicacion);
        calendarView = findViewById(R.id.calendarView);
        timePicker = findViewById(R.id.timePicker);

        Button btnAceptarCreacion = findViewById(R.id.btnAceptarCreacion);
        btnAceptarCreacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerAsociacionYCrearEvento();
            }
        });
    }

    private void obtenerAsociacionYCrearEvento() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();
        DocumentReference userRef = db.collection("usuarios").document(userId);

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String asociacion = documentSnapshot.getString("Asociacion");
                    crearEvento(asociacion);  // Crear el evento con la asociación
                } else {
                    Toast.makeText(CreacionEvento.this, "No se encontró la asociación del usuario", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreacionEvento.this, "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void crearEvento(String asociacion) {
        String nombreEvento = editTextNombreEvento.getText().toString();
        String descripcionEvento = editTextDescripcionEvento.getText().toString();
        String ubicacionEvento = editTextUbicacionEvento.getText().toString();

        long fechaEventoMillis = calendarView.getDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(fechaEventoMillis);

        int hora = timePicker.getCurrentHour();
        int minuto = timePicker.getCurrentMinute();

        calendar.set(Calendar.HOUR_OF_DAY, hora);
        calendar.set(Calendar.MINUTE, minuto);

        Timestamp timestamp = new Timestamp(calendar.getTimeInMillis() / 1000, 0);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Crear el evento con la asociación
        Evento nuevoEvento = new Evento(
                nombreEvento,
                descripcionEvento,
                timestamp,
                currentUser.getUid(),
                ubicacionEvento,
                asociacion  // Incluir la asociación
        );

        guardarEventoEnFirestore(nuevoEvento);
    }

    private void guardarEventoEnFirestore(Evento nuevoEvento) {
        String eventoId = UUID.randomUUID().toString();

        db.collection("eventos").document(eventoId)
                .set(nuevoEvento)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CreacionEvento.this, "Evento guardado con éxito", Toast.LENGTH_SHORT).show();
                        finish();
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
