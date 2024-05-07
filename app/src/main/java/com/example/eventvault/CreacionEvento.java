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
    private Calendar fechaSeleccionada; // Almacenar la fecha seleccionada

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

        // Inicializar la fecha seleccionada con la fecha actual del CalendarView
        fechaSeleccionada = Calendar.getInstance();
        fechaSeleccionada.setTimeInMillis(calendarView.getDate());

        // Configurar un listener para actualizar la fecha seleccionada cuando se cambia el CalendarView
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            fechaSeleccionada.set(Calendar.YEAR, year);
            fechaSeleccionada.set(Calendar.MONTH, month);
            fechaSeleccionada.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        });

        Button btnAceptarCreacion = findViewById(R.id.btnAceptarCreacion);
        btnAceptarCreacion.setOnClickListener((v) -> {
            obtenerAsociacionYCrearEvento(); // Acción para crear el evento
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

        userRef.get().addOnSuccessListener((documentSnapshot) -> {
            if (documentSnapshot.exists()) {
                String asociacion = documentSnapshot.getString("Asociacion");
                crearEvento(asociacion); // Crear el evento con la asociación
            } else {
                Toast.makeText(this, "No se encontró la asociación del usuario", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener((e) -> {
            Toast.makeText(this, "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show();
        });
    }

    private void crearEvento(String asociacion) {
        String nombreEvento = editTextNombreEvento.getText().toString();
        String descripcionEvento = editTextDescripcionEvento.getText().toString();
        String ubicacionEvento = editTextUbicacionEvento.getText().toString();

        int hora = timePicker.getCurrentHour();
        int minuto = timePicker.getCurrentMinute();

        // Establecer la hora y los minutos a la fecha seleccionada
        fechaSeleccionada.set(Calendar.HOUR_OF_DAY, hora);
        fechaSeleccionada.set(Calendar.MINUTE, minuto);

        // Crear un Timestamp a partir de la fecha seleccionada
        Timestamp timestamp = new Timestamp(fechaSeleccionada.getTimeInMillis() / 1000, 0);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        Evento nuevoEvento = new Evento(
                nombreEvento,
                descripcionEvento,
                timestamp, // Usar el timestamp correcto
                currentUser.getUid(),
                ubicacionEvento,
                asociacion // Incluir la asociación
        );

        guardarEventoEnFirestore(nuevoEvento); // Guardar el evento en Firestore
    }

    private void guardarEventoEnFirestore(Evento nuevoEvento) {
        String eventoId = UUID.randomUUID().toString();

        db.collection("eventos").document(eventoId)
                .set(nuevoEvento)
                .addOnSuccessListener((Void aVoid) -> {
                    Toast.makeText(CreacionEvento.this, "Evento guardado con éxito", Toast.LENGTH_SHORT).show();
                    finish(); // Cerrar la actividad al finalizar
                })
                .addOnFailureListener((e) -> {
                    Toast.makeText(CreacionEvento.this, "Error al guardar el evento", Toast.LENGTH_SHORT).show();
                });
    }
}
