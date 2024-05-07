package com.example.eventvault.modelo;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Evento {
    private String id;
    private String nombre;
    private String descripcion;
    private Timestamp fecha; // Cambiar de long a Timestamp
    private String idCreador;
    private String ubicacion;
    private String nombreAsociacion;

    public Evento() {}

    public Evento(String nombre, String descripcion, Timestamp fecha, String idCreador, String ubicacion) { // Usar Timestamp
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.idCreador = idCreador;
        this.ubicacion = ubicacion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Timestamp getFecha() { // Devuelve Timestamp
        return fecha;
    }

    public void setFecha(Timestamp fecha) { // Establece Timestamp
        this.fecha = fecha;
    }

    public String getIdCreador() {
        return idCreador;
    }

    public void setIdCreador(String idCreador) {
        this.idCreador = idCreador;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getFechaFormateada() { // Cambia para obtener la fecha formateada desde Timestamp
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(fecha.toDate()); // Usa toDate() para convertir Timestamp a Date
    }

    public String getHoraFormateada() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return timeFormat.format(fecha.toDate()); // Usa toDate() para convertir Timestamp a Date
    }

    public String getNombreAsociacion() {
        return nombreAsociacion;
    }

    public void setNombreAsociacion(String nombreAsociacion) {
        this.nombreAsociacion = nombreAsociacion;
    }
}
