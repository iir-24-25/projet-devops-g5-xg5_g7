package com.example.pharmacie.models;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;

public class AuthUtils {
    private static final String PREFS_NAME = "auth_prefs";
    private static final String KEY_TOKEN = "jwt_token";
    public static void logout(Context context) {
        // Effacer le token localement
        clearToken(context);

        // Autres nettoyages si nécessaire (ex: cache, données utilisateur)
    }
    public static void saveToken(Context context, String token) {
        context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .edit()
                .putString(KEY_TOKEN, token)
                .apply();
    }

    public static String getToken(Context context) {
        return context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .getString(KEY_TOKEN, null);
    }

    public static void clearToken(Context context) {
        context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .edit()
                .remove(KEY_TOKEN)
                .apply();
    }

    public static boolean isLoggedIn(Context context) {
        return getToken(context) != null;
    }
}