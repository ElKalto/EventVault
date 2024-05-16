package com.example.eventvault.vista;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.eventvault.R;

import static android.content.Context.MODE_PRIVATE;

public class EventoFragment extends Fragment {

    public EventoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_evento, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Recuperar el color del texto desde SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("ColorTextos", MODE_PRIVATE);
        int colorTexto = sharedPreferences.getInt("ColorTextos", Color.BLACK); // Color negro por defecto

        // Obtener referencias a los elementos de texto
        TextView textViewTituloEvento = view.findViewById(R.id.textViewTituloEvento);
        TextView textViewFechaEvento = view.findViewById(R.id.textViewFechaEvento);

        // Aplicar el color del texto a los elementos de texto
        textViewTituloEvento.setTextColor(colorTexto);
        textViewFechaEvento.setTextColor(colorTexto);
    }
}
