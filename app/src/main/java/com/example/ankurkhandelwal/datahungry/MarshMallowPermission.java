package com.example.ankurkhandelwal.datahungry;

import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class MarshMallowPermission extends Activity {
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 2;
    public static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 1;
    public static final int READ_CALENDER_PERMISSION_REQUEST_CODE = 7;
    public static final int READ_CALL_LOG_PERMISSION_REQUEST_CODE = 6;
    public static final int READ_CONTACT_PERMISSION_REQUEST_CODE = 5;
    public static final int READ_PHONE_STATE_PERMISSION_REQUEST_CODE = 4;
    public static final int READ_SMS_PERMISSION_REQUEST_CODE = 3;
    public static final int RECEIVE_SMS_PERMISSION_REQUEST_CODE = 8;
    Activity activity;

    public MarshMallowPermission(Activity activity) {
        this.activity = activity;
    }

    public boolean checkPermissionForExternalStorage() {
        if (ContextCompat.checkSelfPermission(this.activity, "android.permission.READ_EXTERNAL_STORAGE") == 0) {
            return true;
        }
        return false;
    }

    public boolean checkPermissionForCamera() {
        if (ContextCompat.checkSelfPermission(this.activity, "android.permission.CAMERA") == 0) {
            return true;
        }
        return false;
    }

    public boolean checkPermissionForReadSMS() {
        if (ContextCompat.checkSelfPermission(this.activity, "android.permission.READ_SMS") == 0) {
            return true;
        }
        return false;
    }

    public boolean checkPermissionForReceiveSMS() {
        if (ContextCompat.checkSelfPermission(this.activity, "android.permission.RECEIVE_SMS") == 0) {
            return true;
        }
        return false;
    }

    public boolean checkPermissionForReadPhoneState() {
        if (ContextCompat.checkSelfPermission(this.activity, "android.permission.READ_PHONE_STATE") == 0) {
            return true;
        }
        return false;
    }

    public boolean checkPermissionForReadContact() {
        if (ContextCompat.checkSelfPermission(this.activity, "android.permission.READ_CONTACTS") == 0) {
            return true;
        }
        return false;
    }

    public boolean checkPermissionForReadPhoneLogs() {
        if (ContextCompat.checkSelfPermission(this.activity, "android.permission.READ_CALL_LOG") == 0) {
            return true;
        }
        return false;
    }

    public boolean checkPermissionForReadCalendar() {
        if (ContextCompat.checkSelfPermission(this.activity, "android.permission.READ_CALENDAR") == 0) {
            return true;
        }
        return false;
    }

    public void requestPermissionForExternalStorage() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this.activity, "android.permission.READ_EXTERNAL_STORAGE")) {
            ActivityCompat.requestPermissions(this.activity, new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 1);
        }
    }

    public void requestPermissionForCamera() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this.activity, "android.permission.CAMERA")) {
            ActivityCompat.requestPermissions(this.activity, new String[]{"android.permission.CAMERA"}, 2);
        }
    }

    public void requestPermissionForReadSMS() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this.activity, "android.permission.READ_SMS")) {
            ActivityCompat.requestPermissions(this.activity, new String[]{"android.permission.READ_SMS"}, 3);
        }
    }

    public void requestPermissionForReceiveSMS() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this.activity, "android.permission.RECEIVE_SMS")) {
            ActivityCompat.requestPermissions(this.activity, new String[]{"android.permission.RECEIVE_SMS"}, 8);
        }
    }

    public void requestPermissionForReadPhoneState() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this.activity, "android.permission.READ_PHONE_STATE")) {
            ActivityCompat.requestPermissions(this.activity, new String[]{"android.permission.READ_PHONE_STATE"}, 4);
        }
    }

    public void requestPermissionForReadContact() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this.activity, "android.permission.READ_CONTACTS")) {
            ActivityCompat.requestPermissions(this.activity, new String[]{"android.permission.READ_CONTACTS"}, 5);
        }
    }

    public void requestPermissionForReadPhoneLogs() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this.activity, "android.permission.READ_CALL_LOG")) {
            ActivityCompat.requestPermissions(this.activity, new String[]{"android.permission.READ_CALL_LOG"}, 6);
        }
    }

    public void requestPermissionForReadCalender() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this.activity, "android.permission.READ_CALENDAR")) {
            ActivityCompat.requestPermissions(this.activity, new String[]{"android.permission.READ_CALENDAR"}, 7);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length <= 0 || grantResults[0] != 0) {
                    Log.d("DataHungryApp", "External Storage Permission  Permission Denied");
                    return;
                } else {
                    Log.d("DataHungryApp", "External Storage Permission granted");
                    return;
                }
            case 2:
                if (grantResults.length <= 0 || grantResults[0] != 0) {
                    Log.d("DataHungryApp", "Camera Permission Denied");
                    return;
                } else {
                    Log.d("DataHungryApp", "Camera Permission granted");
                    return;
                }
            case 3:
                if (grantResults.length <= 0 || grantResults[0] != 0) {
                    Log.d("DataHungryApp", "Read Sms Permission Denied");
                    return;
                } else {
                    Log.d("DataHungryApp", "Read SmsPermission granted");
                    return;
                }
            case 4:
                if (grantResults.length <= 0 || grantResults[0] != 0) {
                    Log.d("DataHungryApp", "Phone State Permission Denied");
                    return;
                } else {
                    Log.d("DataHungryApp", "Phone State Permission granted");
                    return;
                }
            case 5:
                if (grantResults.length <= 0 || grantResults[0] != 0) {
                    Log.d("DataHungryApp", "Read Contact Permission Denied");
                    return;
                } else {
                    Log.d("DataHungryApp", "Read Contact Permission granted");
                    return;
                }
            case 6:
                if (grantResults.length <= 0 || grantResults[0] != 0) {
                    Log.d("DataHungryApp", "Read Phone Call logs Permission Denied");
                    return;
                } else {
                    Log.d("DataHungryApp", "Read Phone Call logs Permission granted");
                    return;
                }
            case 7:
                if (grantResults.length <= 0 || grantResults[0] != 0) {
                    Log.d("DataHungryApp", "Read CalendarData logs Permission Denied");
                    return;
                } else {
                    Log.d("DataHungryApp", "Read CalendarData Permission granted");
                    return;
                }
            case 8:
                if (grantResults.length <= 0 || grantResults[0] != 0) {
                    Log.d("DataHungryApp", "Receive  Sms Permission Denied");
                    return;
                } else {
                    Log.d("DataHungryApp", "Receive SmsPermission granted");
                    return;
                }
            default:
                return;
        }
    }
}
