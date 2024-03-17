package com.example.eventvault;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventvault.modelo.Evento;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventosAdapter extends RecyclerView.Adapter<EventosAdapter.ViewHolder> {
    private List<Evento> eventos;
    private Context context;
    private OnItemClickListener mListener; // Agregar interfaz para manejar clics

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public EventosAdapter(Context context, List<Evento> eventos) {
        this.context = context;
        this.eventos = eventos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_evento, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Evento evento = eventos.get(position);
        holder.textViewTituloEvento.setText(evento.getNombre());
        holder.textViewFechaEvento.setText(obtenerFechaFormateada(evento.getFecha()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(position); // Notificar al listener cuando se hace clic en un elemento
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTituloEvento;
        TextView textViewFechaEvento;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTituloEvento = itemView.findViewById(R.id.textViewTituloEvento);
            textViewFechaEvento = itemView.findViewById(R.id.textViewFechaEvento);
        }
    }

    private String obtenerFechaFormateada(long fecha) {
        Date date = new Date(fecha);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        return dateFormat.format(date);
    }
}