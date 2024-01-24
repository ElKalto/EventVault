package com.example.eventvault;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "EventVaultPref";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_IS_CREATOR = "isCreator";

    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;
    private final Context _context;

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    // Método para cerrar la sesión del usuario
    public void cerrarSesion() {
        // Borra los datos de la sesión almacenados en SharedPreferences
        editor.clear();
        editor.apply();

        // Puedes agregar otras acciones necesarias para cerrar la sesión aquí
    }

    // Método para verificar si el usuario está autenticado
    public boolean estaAutenticado() {
        return pref.contains(KEY_USER_EMAIL);
    }

    // Método para obtener el correo electrónico del usuario
    public String obtenerEmailUsuario() {
        return pref.getString(KEY_USER_EMAIL, "");
    }

    // Método para verificar si el usuario es un creador
    public boolean esUsuarioCreador() {
        return pref.getBoolean(KEY_IS_CREATOR, false);
    }
}
