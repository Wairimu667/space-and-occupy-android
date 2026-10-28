package com.example.spaceandoccupy.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.spaceandoccupy.model.User;
import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Set;

public class SessionManager {
    private static final String PREF_NAME = "space_occupy_pref";
    private static final String KEY_USER = "current_user";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_FAVORITES = "favorite_property_ids";

    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;
    private final Gson gson;

    public SessionManager(Context context) {
        pref = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
        gson = new Gson();
    }

    public void saveUserSession(User user) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_USER, gson.toJson(user));
        editor.apply();
    }

    public User getCurrentUser() {
        String json = pref.getString(KEY_USER, null);
        if (json != null) {
            return gson.fromJson(json, User.class);
        }
        return null;
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void logout() {
        editor.remove(KEY_IS_LOGGED_IN);
        editor.remove(KEY_USER);
        editor.apply();
    }

    public void toggleFavorite(String propertyId) {
        Set<String> favorites = getFavoriteIds();
        if (favorites.contains(propertyId)) {
            favorites.remove(propertyId);
        } else {
            favorites.add(propertyId);
        }
        editor.putStringSet(KEY_FAVORITES, favorites);
        editor.apply();
    }

    public boolean isFavorite(String propertyId) {
        return getFavoriteIds().contains(propertyId);
    }

    public Set<String> getFavoriteIds() {
        Set<String> set = pref.getStringSet(KEY_FAVORITES, new HashSet<>());
        return new HashSet<>(set);
    }
}
