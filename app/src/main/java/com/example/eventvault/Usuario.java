package com.example.eventvault;

public class Usuario {
    private String email;
    private String password;
    private boolean esCreador;

    public Usuario(String email, String password, boolean esCreador) {
        this.email = email;
        this.password = password;
        this.esCreador = esCreador;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isCreador() {
        return esCreador;
    }
}
