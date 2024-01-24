package com.example.eventvault;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventosAdapter extends RecyclerView.Adapter<EventosAdapter.ViewHolder> {
    private List<Evento> eventos;
    private Context context;

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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Evento evento = eventos.get(position);
        holder.textViewTituloEvento.setText(evento.getNombre());
        // Agrega más configuraciones según las propiedades del evento
    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTituloEvento;
        // Agrega más vistas según las propiedades del evento

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTituloEvento = itemView.findViewById(R.id.textViewTituloEvento);
            // Inicializa otras vistas según las propiedades del evento
        }
    }
}
