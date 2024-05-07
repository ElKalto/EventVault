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

import java.util.Calendar;
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

        final EditText editTextNombreEvento = findViewById(R.id.edtTextNombreEvento);
        final EditText editTextDescripcionEvento = findViewById(R.id.edtTextDescripcion);
        final EditText editTextUbicacionEvento = findViewById(R.id.edtTextUbicacion);
        final CalendarView calendarView = findViewById(R.id.calendarView);
        final TimePicker timePicker = findViewById(R.id.timePicker);

        btnAceptarCreacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                // Convertir a Timestamp
                Timestamp timestamp = new Timestamp(calendar.getTimeInMillis() / 1000, 0);

                FirebaseUser currentUser = mAuth.getCurrentUser();
                String idCreador = currentUser.getUid();

                // Usar Timestamp para la fecha
                Evento nuevoEvento = new Evento(nombreEvento, descripcionEvento, timestamp, idCreador, ubicacionEvento);

                guardarEventoEnFirestore(nuevoEvento);
            }
        });
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
