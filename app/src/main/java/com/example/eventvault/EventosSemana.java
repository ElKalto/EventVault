package com.example.eventvault;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance();

        // Inicializar RecyclerView y su adaptador
        RecyclerView recyclerView = findViewById(R.id.recyclerViewEventos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventosAdapter = new EventosAdapter(this, listaEventos);
        recyclerView.setAdapter(eventosAdapter);

        // Obtener eventos de Firestore
        obtenerEventosDeFirestore();

        Button btnAtrasEventos = findViewById(R.id.btnAtrasEventos);


        SharedPreferences sharedPreferences = getSharedPreferences("ColorBotones", MODE_PRIVATE);
        int color = sharedPreferences.getInt("ColorBotones", Color.BLACK); // Obtener el color, si no se encuentra, se asigna el color negro


        btnAtrasEventos.setBackgroundColor(color);


        btnAtrasEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent para iniciar la actividad PerfilCreador
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
                            // Actualizar el RecyclerView después de obtener los eventos
                            eventosAdapter.notifyDataSetChanged();
                        } else {
                            // Si falla la obtención de eventos, puedes manejarlo aquí
                        }
                    }
                });
    }
}
