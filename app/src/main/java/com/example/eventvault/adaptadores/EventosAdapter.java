package com.example.eventvault.adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.eventvault.R;
import com.example.eventvault.modelo.Evento;
import com.google.firebase.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class EventosAdapter extends RecyclerView.Adapter<EventosAdapter.ViewHolder> {
    private List<Evento> eventos;
    private Context context;
    private OnItemClickListener mListener;

    // Interfaz para el evento de clic
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // Establecer el listener de clic
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
        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Evento evento = eventos.get(position);
        holder.textViewTituloEvento.setText(evento.getNombre());
        holder.textViewFechaEvento.setText(obtenerFechaFormateada(evento.getFecha()));

        // Establecer evento de clic
        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClick(position);
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

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            textViewTituloEvento = itemView.findViewById(R.id.textViewTituloEvento);
            textViewFechaEvento = itemView.findViewById(R.id.textViewFechaEvento);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }

    private String obtenerFechaFormateada(Timestamp fecha) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        return dateFormat.format(fecha.toDate());
    }
}
