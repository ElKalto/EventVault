package com.example.eventvault.vista;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventvault.adaptadores.EventosAdapter;
import com.example.eventvault.R;
import com.example.eventvault.modelo.DetallesEvento;
import com.example.eventvault.modelo.Evento;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EventosSemana extends AppCompatActivity {
    private List<Evento> listaEventos = new ArrayList<>();
    private EventosAdapter eventosAdapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos_semana);

        db = FirebaseFirestore.getInstance();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewEventos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventosAdapter = new EventosAdapter(this, listaEventos);
        recyclerView.setAdapter(eventosAdapter);

        obtenerEventosDeFirestore();

        eventosAdapter.setOnItemClickListener((position) -> {
            Evento evento = listaEventos.get(position);
            Intent intent = new Intent(EventosSemana.this, DetallesEvento.class);

            // Log para verificar los datos antes de enviar
            Log.d("EventosSemana", "Evento seleccionado: " + evento);

            intent.putExtra("nombre", evento.getNombre());
            intent.putExtra("descripcion", evento.getDescripcion());
            intent.putExtra("fecha", evento.getFecha().getSeconds() * 1000); // Tiempo en milisegundos
            intent.putExtra("ubicacion", evento.getUbicacion());
            intent.putExtra("nombreAsociacion", evento.getNombreAsociacion());

            startActivity(intent);
        });

        Button btnAtrasEventos = findViewById(R.id.btnAtrasEventos);
        SharedPreferences sharedPreferences = getSharedPreferences("ColorBotones", MODE_PRIVATE);
        int color = sharedPreferences.getInt("ColorBotones", Color.BLACK);
        btnAtrasEventos.setBackgroundColor(color);

        btnAtrasEventos.setOnClickListener((v) -> {
            finish();
        });
    }

    private void obtenerEventosDeFirestore() {
        db.collection("eventos")
                .get()
                .addOnCompleteListener((task) -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                Evento evento = document.toObject(Evento.class);
                                listaEventos.add(evento);
                            }
                            eventosAdapter.notifyDataSetChanged();
                        } else {
                            Log.e("Firestore", "QuerySnapshot es null");
                        }
                    } else {
                        Log.e("Firestore", "Error al obtener eventos", task.getException());
                        Toast.makeText(getApplicationContext(), "Error al obtener eventos", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
