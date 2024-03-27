package com.example.eventvault;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventvault.modelo.DetallesEvento;
import com.example.eventvault.modelo.Evento;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

        // Configurar clics en el RecyclerView
        eventosAdapter.setOnItemClickListener(new EventosAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Evento evento = listaEventos.get(position);
                Intent intent = new Intent(EventosSemana.this, DetallesEvento.class);
                // Pasar los detalles del evento a la actividad DetallesEvento
                intent.putExtra("nombre", evento.getNombre());
                intent.putExtra("descripcion", evento.getDescripcion());
                intent.putExtra("fecha", evento.getFecha());
                intent.putExtra("hora", evento.getHoraFormateada());
                intent.putExtra("ubicacion", evento.getUbicacion()); // Agrega este extra
                intent.putExtra("creador", evento.getIdCreador());
                intent.putExtra("nombreAsociacion", evento.getNombreAsociacion()); // Agrega este extra

                startActivity(intent);
            }
        });

        Button btnAtrasEventos = findViewById(R.id.btnAtrasEventos);

        SharedPreferences sharedPreferences = getSharedPreferences("ColorBotones", MODE_PRIVATE);
        int color = sharedPreferences.getInt("ColorBotones", Color.BLACK);

        btnAtrasEventos.setBackgroundColor(color);

        btnAtrasEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void obtenerEventosDeFirestore() {
        db.collection("eventos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Evento evento = document.toObject(Evento.class);
                                listaEventos.add(evento);
                            }
                            eventosAdapter.notifyDataSetChanged();
                        } else {
                            // Manejar error
                            Exception e = task.getException();
                            if (e != null) {
                                Log.e("Firestore", "Error al obtener eventos", e);

                                Toast.makeText(getApplicationContext(), "Error al obtener eventos", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

}