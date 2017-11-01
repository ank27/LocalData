package com.example.ankurkhandelwal.datahungry.services;

import android.util.Log;
import com.example.ankurkhandelwal.datahungry.Utils.PreferencesManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class DataHungryFireBaseInstanceIdService extends FirebaseInstanceIdService {
    private static final String TAG = DataHungryFireBaseInstanceIdService.class.getSimpleName();

    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if (refreshedToken != null) {
            Log.d(TAG, "Token: " + refreshedToken);
            saveTokenInPrefs(refreshedToken);
        }
    }

    private void saveTokenInPrefs(String token) {
        if (token == null) {
            token = "";
        }
        try {
            PreferencesManager.putString("fcm_token", token);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            PreferencesManager.putString("fcm_token", token);
        }
    }
}
