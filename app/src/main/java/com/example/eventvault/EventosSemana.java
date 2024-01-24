package com.example.eventvault;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventvault.EventosAdapter;

import java.util.ArrayList;
import java.util.List;

public class EventosSemana extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos_semana);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewEventos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Evento> eventos = obtenerListaEventos(); // Reemplaza con tu lógica para obtener la lista de eventos
        EventosAdapter eventosAdapter = new EventosAdapter(this, eventos);
        recyclerView.setAdapter(eventosAdapter);
    }

    private List<Evento> obtenerListaEventos() {
        // Aquí deberías implementar la lógica para obtener la lista de eventos de la semana
        // Puedes cargar datos de una base de datos, una API, etc.
        List<Evento> eventos = new ArrayList<>();
        eventos.add(new Evento("Evento 1"));
        eventos.add(new Evento("Evento 2"));
        // Agrega más eventos según tu lógica
        return eventos;
    }
}
