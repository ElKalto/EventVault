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

    public void cerrarSesion() {
        editor.clear();
        editor.apply();

    }

    public boolean estaAutenticado() {
        return pref.contains(KEY_USER_EMAIL);
    }

    public String obtenerEmailUsuario() {
        return pref.getString(KEY_USER_EMAIL, "");
    }

    public boolean esUsuarioCreador() {
        return pref.getBoolean(KEY_IS_CREATOR, false);
    }
}
