package com.example.eventvault;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
    private Button btnAceptarEditarEvento;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_evento);

        spinnerEventos = findViewById(R.id.spinnerEventos);
        edtNombreEvento = findViewById(R.id.edtTextNombreEvento);
        edtDescripcionEvento = findViewById(R.id.edtTextDescripcion);
        btnAceptarEditarEvento = findViewById(R.id.btnAceptarEditarEvento);

        db = FirebaseFirestore.getInstance();

        obtenerEventosUsuarioActual();

        Button btnAtrasEditarEventos = findViewById(R.id.btnAtrasEditarEventos);
        btnAtrasEditarEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
                                    evento.setId(document.getId()); // Establecer el id del evento
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
        List<String> nombresEventos = new ArrayList<>();
        for (Evento evento : listaEventos) {
            nombresEventos.add(evento.getNombre());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombresEventos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEventos.setAdapter(adapter);
    }

    private void editarEventoSeleccionado() {
        // Obtener el nombre del evento seleccionado del Spinner
        String nombreEventoSeleccionado = spinnerEventos.getSelectedItem().toString();

        // Obtener el nuevo nombre y descripción del evento editado desde los EditText
        String nuevoNombreEvento = edtNombreEvento.getText().toString().trim();
        String nuevaDescripcionEvento = edtDescripcionEvento.getText().toString().trim();

        // Buscar el evento seleccionado en la lista de eventos
        for (Evento evento : listaEventos) {
            if (evento.getNombre().equals(nombreEventoSeleccionado)) {
                // Actualizar los campos del evento con los nuevos valores
                evento.setNombre(nuevoNombreEvento);
                evento.setDescripcion(nuevaDescripcionEvento);

                // Obtener la referencia del documento del evento en Firestore
                DocumentReference eventoRef = db.collection("eventos").document(evento.getId()); // Utilizar el id del evento

                // Actualizar los datos del evento en Firestore
                eventoRef.update("nombre", nuevoNombreEvento, "descripcion", nuevaDescripcionEvento)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Mostrar un mensaje de éxito
                                Toast.makeText(EditarEvento.this, "Evento actualizado exitosamente", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Manejar errores en la actualización del evento
                                Toast.makeText(EditarEvento.this, "Error al actualizar el evento: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                // Salir del bucle una vez que se haya encontrado y actualizado el evento
                break;
            }
        }
    }
}
