package com.example.eventvault;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;
import android.view.View;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

public class CalendarioEventos extends AppCompatActivity {
    private CalendarView calendarView;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario_eventos);

        calendarView = findViewById(R.id.calendarView);
        db = FirebaseFirestore.getInstance();

        Button btnAtras = findViewById(R.id.btnAtras);

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cargarEventosCalendario();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String selectedDate = year + "/" + (month + 1) + "/" + dayOfMonth;
                Toast.makeText(CalendarioEventos.this, "Fecha seleccionada: " + selectedDate, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarEventosCalendario() {
        db.collection("eventos")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            Date fechaEvento = document.getDate("fecha");
                            marcarFechaEnCalendario(fechaEvento);
                        }
                    } else {
                    }
                });
    }

    private void marcarFechaEnCalendario(Date fecha) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        calendarView.setDate(calendar.getTimeInMillis(), true, true);
        calendarView.getChildAt(0).setBackgroundColor(Color.GREEN);
    }
}
