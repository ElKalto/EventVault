package com.example.eventvault.vista;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.eventvault.R;
import com.example.eventvault.modelo.Evento;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EditarEvento extends AppCompatActivity {

    private List<Evento> listaEventos = new ArrayList<>();
    private Spinner spinnerEventos;
    private EditText edtNombreEvento, edtUbicacionEvento, edtDescripcionEvento;
    private TextView txtViewFecha, txtViewHoraEvento, textViewCreaEvent;
    private Button btnAceptarEditarEvento;
    private FirebaseFirestore db;
    private int colorTexto;

    private CalendarView calendarView;
    private TimePicker timePicker;

    // Variable para almacenar la fecha seleccionada en milisegundos
    private long fechaSeleccionadaMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_evento);

        textViewCreaEvent = findViewById(R.id.textViewCreaEvent);
        edtNombreEvento = findViewById(R.id.edtTextNombreEvento);
        edtDescripcionEvento = findViewById(R.id.edtTextDescripcion);
        edtUbicacionEvento = findViewById(R.id.edtTextUbicacion);
        txtViewFecha = findViewById(R.id.txtViewFecha);
        txtViewHoraEvento = findViewById(R.id.txtViewHoraEvento);
        btnAceptarEditarEvento = findViewById(R.id.btnAceptarEditarEvento);
        db = FirebaseFirestore.getInstance();

        calendarView = findViewById(R.id.calendarView);
        timePicker = findViewById(R.id.timePicker);

        // Recuperar el color del texto desde SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("ColorTextos", MODE_PRIVATE);
        colorTexto = sharedPreferences.getInt("ColorTextos", Color.BLACK); // Color negro por defecto

        // Aplicar color a los elementos de texto
        textViewCreaEvent.setTextColor(colorTexto);
        edtNombreEvento.setTextColor(colorTexto);
        edtNombreEvento.setHintTextColor(colorTexto);
        edtDescripcionEvento.setTextColor(colorTexto);
        edtDescripcionEvento.setHintTextColor(colorTexto);
        edtUbicacionEvento.setTextColor(colorTexto);
        edtUbicacionEvento.setHintTextColor(colorTexto);
        txtViewFecha.setTextColor(colorTexto);
        txtViewHoraEvento.setTextColor(colorTexto);

        // Obtener eventos del usuario
        obtenerEventosUsuarioActual();

        // Configurar el botón de aceptar edición de evento
        btnAceptarEditarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarEventoSeleccionado();
                // Devolver a la actividad anterior
                finish();
            }
        });

        // Configurar el Listener para el cambio de fecha del CalendarView
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Obtener la fecha seleccionada en milisegundos
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                fechaSeleccionadaMillis = calendar.getTimeInMillis();
            }
        });
    }

    private void obtenerEventosUsuarioActual() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String idUsuarioActual = currentUser.getUid();

            db.collection("eventos")
                    .whereEqualTo("idCreador", idUsuarioActual)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                listaEventos.clear();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Evento evento = document.toObject(Evento.class);
                                    evento.setId(document.getId());
                                    listaEventos.add(evento);
                                }
                                configurarSpinner();
                            } else {
                                Toast.makeText(EditarEvento.this, "Error al obtener eventos", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void configurarSpinner() {
        spinnerEventos = findViewById(R.id.spinnerEventos); // Inicialización del spinner
        List<String> nombresEventos = new ArrayList<>();
        for (Evento evento : listaEventos) {
            nombresEventos.add(evento.getNombre());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombresEventos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEventos.setAdapter(adapter);
    }

    private void editarEventoSeleccionado() {
        String nombreEventoSeleccionado = spinnerEventos.getSelectedItem().toString();

        String nuevoNombreEvento = edtNombreEvento.getText().toString().trim();
        String nuevaDescripcionEvento = edtDescripcionEvento.getText().toString().trim();
        String nuevaUbicacionEvento = edtUbicacionEvento.getText().toString().trim(); // Obtener la nueva ubicación del evento

        // Obtener la hora seleccionada del TimePicker
        int horaSeleccionada = timePicker.getCurrentHour();
        int minutoSeleccionado = timePicker.getCurrentMinute();

        // Combinar la fecha y la hora en un objeto Timestamp
        Timestamp nuevaFecha = new Timestamp(new Date(fechaSeleccionadaMillis));
        Calendar cal = Calendar.getInstance();
        cal.setTime(nuevaFecha.toDate());
        cal.set(Calendar.HOUR_OF_DAY, horaSeleccionada);
        cal.set(Calendar.MINUTE, minutoSeleccionado);
        nuevaFecha = new Timestamp(cal.getTime());

        for (Evento evento : listaEventos) {
            if (evento.getNombre().equals(nombreEventoSeleccionado)) {
                evento.setNombre(nuevoNombreEvento);
                evento.setDescripcion(nuevaDescripcionEvento);
                evento.setUbicacion(nuevaUbicacionEvento);
                evento.setFecha(nuevaFecha); // Establecer la nueva fecha del evento

                DocumentReference eventoRef = db.collection("eventos").document(evento.getId()); // Utilizar el id del evento

                eventoRef.update("nombre", nuevoNombreEvento, "descripcion", nuevaDescripcionEvento, "ubicacion", nuevaUbicacionEvento, "fecha", nuevaFecha) // Actualizar también la fecha
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EditarEvento.this, "Evento actualizado exitosamente", Toast.LENGTH_SHORT).show();
                                // Refrescar la lista de eventos después de la actualización
                                obtenerEventosUsuarioActual();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditarEvento.this, "Error al actualizar el evento: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
            }
        }
    }
}
