package com.example.eventvault.vista;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventvault.R;
import com.example.eventvault.adaptadores.EventosAdapter;
import com.example.eventvault.modelo.DetallesEvento;
import com.example.eventvault.modelo.Evento;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListaEventos extends AppCompatActivity {
    private List<Evento> listaEventos = new ArrayList<>();
    private EventosAdapter eventosAdapter;
    private FirebaseFirestore db;
    private Spinner spinnerOrden;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_eventos);

        db = FirebaseFirestore.getInstance();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewEventos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventosAdapter = new EventosAdapter(this, listaEventos);
        recyclerView.setAdapter(eventosAdapter);

        // Agregar un decorador de división entre los elementos del RecyclerView
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        // Configurar el Spinner de orden
        spinnerOrden = findViewById(R.id.spinnerOrden);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.orden_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrden.setAdapter(adapter);
        spinnerOrden.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: // De más reciente a más antiguo
                        obtenerEventosDeFirestore(Query.Direction.DESCENDING);
                        break;
                    case 1: // De más antiguo a más reciente
                        obtenerEventosDeFirestore(Query.Direction.ASCENDING);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada si no se selecciona nada
            }
        });

        // Llamar a obtenerEventosDeFirestore con la dirección por defecto
        obtenerEventosDeFirestore(Query.Direction.DESCENDING);

        eventosAdapter.setOnItemClickListener((position) -> {
            Evento evento = listaEventos.get(position);
            Intent intent = new Intent(ListaEventos.this, DetallesEvento.class);

            // Log para verificar los datos antes de enviar
            Log.d("ListaEventos", "Evento seleccionado: " + evento);

            intent.putExtra("nombre", evento.getNombre());
            intent.putExtra("descripcion", evento.getDescripcion());
            intent.putExtra("fecha", evento.getFecha().getSeconds() * 1000); // Tiempo en milisegundos
            intent.putExtra("ubicacion", evento.getUbicacion());
            intent.putExtra("nombreAsociacion", evento.getNombreAsociacion());

            startActivity(intent);
        });

        // Recuperar el color del texto desde SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("ColorTextos", MODE_PRIVATE);
        int color = sharedPreferences.getInt("ColorTextos", Color.BLACK); // Color negro por defecto

        // Aplicar el color al TextView
        TextView textViewEventosDisponibles = findViewById(R.id.txtviewTituloLista);
        textViewEventosDisponibles.setTextColor(color);
    }

    private void obtenerEventosDeFirestore(Query.Direction direction) {
        db.collection("eventos")
                .orderBy("fecha", direction) // Ordenar por fecha según la dirección proporcionada
                .get()
                .addOnCompleteListener((task) -> {
                    if (task.isSuccessful()) {
                        listaEventos.clear(); // Limpiar la lista actual
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Evento evento = document.toObject(Evento.class);
                            listaEventos.add(evento);
                        }
                        eventosAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("Firestore", "Error al obtener eventos", task.getException());
                        Toast.makeText(getApplicationContext(), "Error al obtener eventos", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
