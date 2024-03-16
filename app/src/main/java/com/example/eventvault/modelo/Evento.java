package com.example.eventvault.modelo;

public class Evento {
    private String id;
    private String nombre;
    private String descripcion;
    private long fecha;
    private String idCreador;

    public Evento() {
    }

    public Evento(String nombre, String descripcion, long fecha, String idCreador) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.idCreador = idCreador;
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

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public String getIdCreador() {
        return idCreador;
    }

    public void setIdCreador(String idCreador) {
        this.idCreador = idCreador;
    }
}
