package com.example.eventvault.vista;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.eventvault.R;
import com.example.eventvault.modelo.Evento;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class EditarEvento extends AppCompatActivity {

    private List<Evento> listaEventos = new ArrayList<>();
    private Spinner spinnerEventos;
    private EditText edtNombreEvento;
    private EditText edtDescripcionEvento;
    private EditText edtUbicacionEvento; // Agregar EditText para la ubicación
    private Button btnAceptarEditarEvento;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_evento);

        edtNombreEvento = findViewById(R.id.edtTextNombreEvento);
        edtDescripcionEvento = findViewById(R.id.edtTextDescripcion);
        edtUbicacionEvento = findViewById(R.id.edtTextUbicacion); // Inicialización del EditText para la ubicación
        btnAceptarEditarEvento = findViewById(R.id.btnAceptarEditarEvento); // Inicialización del botón

        SharedPreferences sharedPreferences = getSharedPreferences("ColorBotones", MODE_PRIVATE);
        int color = sharedPreferences.getInt("ColorBotones", Color.WHITE); // Color blanco por defecto

        // Aplicar el color al botón
        btnAceptarEditarEvento.setBackgroundColor(color);

        db = FirebaseFirestore.getInstance();

        obtenerEventosUsuarioActual();

        btnAceptarEditarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarEventoSeleccionado();
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

        for (Evento evento : listaEventos) {
            if (evento.getNombre().equals(nombreEventoSeleccionado)) {
                evento.setNombre(nuevoNombreEvento);
                evento.setDescripcion(nuevaDescripcionEvento);
                evento.setUbicacion(nuevaUbicacionEvento); // Establecer la nueva ubicación del evento

                DocumentReference eventoRef = db.collection("eventos").document(evento.getId()); // Utilizar el id del evento

                eventoRef.update("nombre", nuevoNombreEvento, "descripcion", nuevaDescripcionEvento, "ubicacion", nuevaUbicacionEvento) // Agregar "ubicacion" al método update()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EditarEvento.this, "Evento actualizado exitosamente", Toast.LENGTH_SHORT).show();
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
