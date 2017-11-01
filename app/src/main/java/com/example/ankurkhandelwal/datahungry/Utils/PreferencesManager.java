package com.example.ankurkhandelwal.datahungry.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferencesManager {
    private static SharedPreferences prefs;

    public static void initialize(Context context) {
        prefs = context.getSharedPreferences("settings", 0);
    }

    private static Editor getEditor() {
        return prefs.edit();
    }

    public static String getString(String key, String defValue) {
        return prefs.getString(key, defValue);
    }

    public static int getInt(String key, int defValue) {
        return prefs.getInt(key, defValue);
    }

    public static boolean getBoolean(String key, boolean defVal) {
        return prefs.getBoolean(key, defVal);
    }

    public static void putInt(String key, int value) {
        getEditor().putInt(key, value).commit();
    }

    public static void putString(String key, String value) {
        getEditor().putString(key, value).commit();
    }

    public static void putBoolean(String key, boolean value) {
        getEditor().putBoolean(key, value).commit();
    }
}
