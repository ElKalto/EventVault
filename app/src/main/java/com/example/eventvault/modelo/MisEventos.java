package com.example.eventvault.modelo;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventvault.EventosAdapter;
import com.example.eventvault.R;
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
        recyclerView.setAdapter(eventosAdapter);

        obtenerEventosDelUsuarioActual();

        Button btnAtrasMisEventos = findViewById(R.id.btnAtrasMisEventos);

        SharedPreferences sharedPreferences = getSharedPreferences("ColorBotones", MODE_PRIVATE);
        int color = sharedPreferences.getInt("ColorBotones", Color.WHITE); // Color blanco por defecto

        // Aplicar el color al botón
        btnAtrasMisEventos.setBackgroundColor(color);

        btnAtrasMisEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cierra la actividad actual y vuelve a la actividad anterior
                finish();
            }
        });
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
                        } else {
                            // Manejar el error si la consulta falla
                        }
                    }
                });

    }
}
