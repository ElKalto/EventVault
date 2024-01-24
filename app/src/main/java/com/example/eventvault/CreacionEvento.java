package com.example.eventvault;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlSerializer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class CreacionEvento extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creacion_evento);

        Button btnAceptarCreacion = findViewById(R.id.btnAceptarCreacion);
        final EditText editTextNombreEvento = findViewById(R.id.editTextText3);
        final EditText editTextDescripcionEvento = findViewById(R.id.editTextText4);
        final CalendarView calendarView = findViewById(R.id.calendarView);

        btnAceptarCreacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener datos del evento
                String nombreEvento = editTextNombreEvento.getText().toString();
                String descripcionEvento = editTextDescripcionEvento.getText().toString();
                long fechaEvento = calendarView.getDate(); // Obtener la fecha en milisegundos

                // Crear un objeto Evento con los datos del evento
                Evento nuevoEvento = new Evento(nombreEvento, descripcionEvento, fechaEvento);

                // Guardar el evento en un archivo XML
                guardarEventoEnXML(nuevoEvento);

                // Mostrar un mensaje de éxito (puedes cambiar esto según tus necesidades)
                Toast.makeText(CreacionEvento.this, "Evento guardado con éxito", Toast.LENGTH_SHORT).show();

                // Notificar a EventosSemana que se han agregado nuevos eventos
                notificarCambios();

                // Volver a la pantalla activity_perfil_creador
                finish();
            }
        });
    }

    private void guardarEventoEnXML(Evento nuevoEvento) {
        try {
            // Leer eventos existentes desde el archivo XML
            List<Evento> eventosExistente = new EventosSemana().leerEventosDesdeXML(this);

            // Agregar el nuevo evento a la lista
            eventosExistente.add(nuevoEvento);

            FileOutputStream fos = openFileOutput("Eventos.xml", Context.MODE_PRIVATE);

            XmlSerializer xmlSerializer = Xml.newSerializer();
            xmlSerializer.setOutput(fos, "UTF-8");
            xmlSerializer.startDocument(null, true);
            xmlSerializer.startTag(null, "eventos");

            for (Evento evento : eventosExistente) {
                xmlSerializer.startTag(null, "evento");

                xmlSerializer.startTag(null, "nombre");
                xmlSerializer.text(evento.getNombre());
                xmlSerializer.endTag(null, "nombre");

                xmlSerializer.startTag(null, "descripcion");
                xmlSerializer.text(evento.getDescripcion());
                xmlSerializer.endTag(null, "descripcion");

                xmlSerializer.startTag(null, "fecha");
                xmlSerializer.text(String.valueOf(evento.getFecha()));
                xmlSerializer.endTag(null, "fecha");

                xmlSerializer.endTag(null, "evento");
            }

            xmlSerializer.endTag(null, "eventos");
            xmlSerializer.endDocument();

            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para notificar cambios a EventosSemana
    private void notificarCambios() {
        Intent intent = new Intent("ACTUALIZAR_EVENTOS");
        sendBroadcast(intent);
    }
}
