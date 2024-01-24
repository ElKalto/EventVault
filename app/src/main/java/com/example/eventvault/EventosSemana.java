package com.example.eventvault;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EventosSemana extends AppCompatActivity {

    private List<Evento> listaEventos = new ArrayList<>();
    private EventosAdapter eventosAdapter;

    // Receptor de transmisiones para actualizar la lista de eventos
    private BroadcastReceiver actualizarEventosReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Leer eventos del archivo XML
            leerEventosDesdeXML(context);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos_semana);

        // Inicializar RecyclerView y su adaptador
        RecyclerView recyclerView = findViewById(R.id.recyclerViewEventos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventosAdapter = new EventosAdapter(this, listaEventos);
        recyclerView.setAdapter(eventosAdapter);

        // Registrar el receptor de transmisiones para actualizar la lista cuando cambie
        registerReceiver(actualizarEventosReceiver, new IntentFilter("ACTUALIZAR_EVENTOS"));

        // Leer eventos del archivo XML
        leerEventosDesdeXML(this);

        Button btnAtrasEventos = findViewById(R.id.btnAtrasEventos);

        btnAtrasEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent para iniciar la actividad PerfilCreador
                Intent intent = new Intent(EventosSemana.this, PerfilCreador.class);
                startActivity(intent);
            }
        });
    }

    public List<Evento> leerEventosDesdeXML(Context context) {
        List<Evento> eventos = new ArrayList<>();

        try {
            FileInputStream fis = context.openFileInput("Eventos.xml");
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser parser = xmlFactoryObject.newPullParser();
            parser.setInput(fis, null);

            int eventType = parser.getEventType();
            Evento eventoActual = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("eventos".equals(tagName)) {
                            // Puedes agregar lógica específica si lo deseas
                        } else if ("evento".equals(tagName)) {
                            eventoActual = new Evento("", "", 0);
                        } else if ("nombre".equals(tagName)) {
                            if (eventoActual != null) {
                                eventoActual.setNombre(parser.nextText());
                            }
                        } else if ("descripcion".equals(tagName)) {
                            if (eventoActual != null) {
                                eventoActual.setDescripcion(parser.nextText());
                            }
                        } else if ("fecha".equals(tagName)) {
                            if (eventoActual != null) {
                                eventoActual.setFecha(Long.parseLong(parser.nextText()));
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if ("evento".equals(tagName) && eventoActual != null) {
                            eventos.add(eventoActual); // Agregar el evento a la lista
                        }
                        break;
                }
                eventType = parser.next();
            }

            fis.close();
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }

        // Actualizar el conjunto de datos del adaptador
        listaEventos.clear();
        listaEventos.addAll(eventos);
        eventosAdapter.notifyDataSetChanged();

        return eventos; // Devolver la lista de eventos leída
    }

    @Override
    protected void onDestroy() {
        // Desregistrar el receptor de transmisiones al salir de la actividad
        unregisterReceiver(actualizarEventosReceiver);
        super.onDestroy();
    }
}
