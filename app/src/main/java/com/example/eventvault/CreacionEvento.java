package com.example.eventvault;

import android.content.SharedPreferences;
import android.graphics.Color;
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

        SharedPreferences sharedPreferences = getSharedPreferences("ColorBotones", MODE_PRIVATE);
        int color = sharedPreferences.getInt("ColorBotones", Color.WHITE); // Color blanco por defecto

        btnAceptarCreacion.setBackgroundColor(color);

        final EditText editTextNombreEvento = findViewById(R.id.edtTextNombreEvento);
        final EditText editTextDescripcionEvento = findViewById(R.id.edtTextDescripcion);
        final CalendarView calendarView = findViewById(R.id.calendarView);
        final TimePicker timePicker = findViewById(R.id.timePicker); // Agregar TimePicker

        btnAceptarCreacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreEvento = editTextNombreEvento.getText().toString();
                String descripcionEvento = editTextDescripcionEvento.getText().toString();

                long fechaEventoMillis = calendarView.getDate();
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(fechaEventoMillis);

                int hora = timePicker.getCurrentHour();
                int minuto = timePicker.getCurrentMinute();

                calendar.set(Calendar.HOUR_OF_DAY, hora);
                calendar.set(Calendar.MINUTE, minuto);

                long fechaYHoraEvento = calendar.getTimeInMillis();

                FirebaseUser currentUser = mAuth.getCurrentUser();
                String idCreador = currentUser.getUid();

                Evento nuevoEvento = new Evento(nombreEvento, descripcionEvento, fechaYHoraEvento, idCreador);

                guardarEventoEnFirestore(nuevoEvento);
            }
        });

        Button btnAtrasEditarEventos = findViewById(R.id.btnAtrasEditarEventos);
        btnAtrasEditarEventos.setBackgroundColor(color);
        btnAtrasEditarEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
