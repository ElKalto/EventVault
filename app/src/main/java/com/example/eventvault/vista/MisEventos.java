package com.example.eventvault.vista;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventvault.adaptadores.EventosAdapter;
import com.example.eventvault.R;
import com.example.eventvault.modelo.DetallesEvento;
import com.example.eventvault.modelo.Evento;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MisEventos extends AppCompatActivity {

    private List<Evento> listaEventos = new ArrayList<>();
    private EventosAdapter eventosAdapter;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_eventos);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewMisEventos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventosAdapter = new EventosAdapter(this, listaEventos);

        // Configurar evento de clic para abrir detalles
        eventosAdapter.setOnItemClickListener(position -> {
            Evento evento = listaEventos.get(position);
            Intent intent = new Intent(MisEventos.this, DetallesEvento.class);

            // Pasar los detalles del evento
            intent.putExtra("nombre", evento.getNombre());
            intent.putExtra("descripcion", evento.getDescripcion());
            intent.putExtra("fecha", evento.getFecha().toDate().getTime());
            intent.putExtra("hora", evento.getHoraFormateada());
            intent.putExtra("ubicacion", evento.getUbicacion());
            intent.putExtra("creador", evento.getIdCreador());
            intent.putExtra("nombreAsociacion", evento.getNombreAsociacion());

            startActivity(intent);
        });

        recyclerView.setAdapter(eventosAdapter);

        obtenerEventosDelUsuarioActual();

        Button btnAtrasMisEventos = findViewById(R.id.btnAtrasMisEventos);

        SharedPreferences sharedPreferences = getSharedPreferences("ColorBotones", MODE_PRIVATE);
        int color = sharedPreferences.getInt("ColorBotones", Color.WHITE);

        btnAtrasMisEventos.setBackgroundColor(color);

        btnAtrasMisEventos.setOnClickListener(v -> finish());
    }

    private void obtenerEventosDelUsuarioActual() {
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("eventos")
                .whereEqualTo("idCreador", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Evento evento = document.toObject(Evento.class);
                                listaEventos.add(evento);
                            }
                            eventosAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}
