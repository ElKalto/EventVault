package com.example.eventvault.vista;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.DividerItemDecoration;
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

public class ListaEventos extends AppCompatActivity {
    private List<Evento> listaEventos = new ArrayList<>();
    private EventosAdapter eventosAdapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_eventos);

        db = FirebaseFirestore.getInstance();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewEventos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventosAdapter = new EventosAdapter(this, listaEventos);
        recyclerView.setAdapter(eventosAdapter);

        // Agrega un decorador de división entre los elementos del RecyclerView
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        obtenerEventosDeFirestore();

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

        // Obtener el color de los botones desde SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("ColorBotones", MODE_PRIVATE);
        int color = sharedPreferences.getInt("ColorBotones", Color.BLACK);

        // Aplicar el color al TextView
        TextView textViewEventosDisponibles = findViewById(R.id.textView8);
        textViewEventosDisponibles.setTextColor(color);
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
