package com.example.ankurkhandelwal.datahungry;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.ContactsContract.PhoneLookup;
import com.example.ankurkhandelwal.datahungry.Models.Contact;
import com.example.ankurkhandelwal.datahungry.Models.SMS;
import java.util.ArrayList;

public class DataHungryApplication extends Application {
    public static ArrayList<Contact> contactList = new ArrayList();
    public static Editor editor;
    public static SharedPreferences prefs;
    public static Contact selected_contact = null;
    public static ArrayList<SMS> smsArrayList = new ArrayList();
    public static ArrayList<SMS> smsArrayListFull = new ArrayList();

    public void onCreate() {
        super.onCreate();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
    }

    public static String getContactName(Context context, String phoneNumber) {
        Cursor cursor = context.getContentResolver().query(Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber)), new String[]{"display_name"}, null, null, null);
        if (cursor == null) {
            return null;
        }
        String contactName = null;
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex("display_name"));
        }
        if (!(cursor == null || cursor.isClosed())) {
            cursor.close();
        }
        return contactName;
    }
}
