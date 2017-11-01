package com.example.ankurkhandelwal.datahungry;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.Gmail.Builder;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.EasyPermissions.PermissionCallbacks;

public class MailActivity extends AppCompatActivity implements PermissionCallbacks {
    private static final String BUTTON_TEXT = "Call Gmail API";
    private static final String PREF_ACCOUNT_NAME = "accountName";
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    private static final String[] SCOPES = new String[]{GmailScopes.GMAIL_LABELS};
    String TAG = "MailActivity";
    private Button mCallApiButton;
    GoogleAccountCredential mCredential;
    private TextView mOutputText;
    ProgressDialog mProgress;

    class C03161 implements OnClickListener {
        C03161() {
        }

        public void onClick(View v) {
            MailActivity.this.mCallApiButton.setEnabled(false);
            MailActivity.this.mOutputText.setText("");
            MailActivity.this.getResultsFromApi();
            MailActivity.this.mCallApiButton.setEnabled(true);
        }
    }

    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
        private Exception mLastError = null;
        private Gmail mService = null;

        MakeRequestTask(GoogleAccountCredential credential) {
            this.mService = new Builder(AndroidHttp.newCompatibleTransport(), JacksonFactory.getDefaultInstance(), credential).setApplicationName("DataHungry").build();
            Log.d("MailActivity", "MakingRequestTask =" + this.mService.getBaseUrl() + " & user =" + this.mService.users().messages().toString());
        }

        protected List<String> doInBackground(Void... params) {
            try {
                Log.d("MailActivity", "doInBackGround =" + params.toString());
                return getDataFromApi();
            } catch (Exception e) {
                this.mLastError = e;
                cancel(true);
                return null;
            }
        }

        private List<String> getDataFromApi() throws IOException {
            List<String> labels = new ArrayList();
            for (Message message : ((ListMessagesResponse) this.mService.users().messages().list("me").execute()).getMessages()) {
                labels.add(message.getPayload().getBody().getData());
                Log.d("MailActivity", "labels =" + message.getRaw());
            }
            return labels;
        }

        protected void onPreExecute() {
            MailActivity.this.mOutputText.setText("");
            Log.d("MailActivity", "Async preExecute");
            MailActivity.this.mProgress.show();
        }

        protected void onPostExecute(List<String> output) {
            MailActivity.this.mProgress.hide();
            if (output == null || output.size() == 0) {
                MailActivity.this.mOutputText.setText("No results returned.");
                return;
            }
            output.add(0, "Data retrieved using the Gmail API:");
            MailActivity.this.mOutputText.setText(TextUtils.join("\n", output));
        }

        protected void onCancelled() {
            MailActivity.this.mProgress.hide();
            if (this.mLastError != null) {
                Log.d("MailActivity", "Async OnCancelled");
                if (this.mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    Log.d("MailActivity", "GooglePlayServicesAvailabilityIOException");
                    MailActivity.this.showGooglePlayServicesAvailabilityErrorDialog(((GooglePlayServicesAvailabilityIOException) this.mLastError).getConnectionStatusCode());
                    return;
                } else if (this.mLastError instanceof UserRecoverableAuthIOException) {
                    Log.d("MailActivity", "UserRecoverableAuthIOException");
                    MailActivity.this.startActivityForResult(((UserRecoverableAuthIOException) this.mLastError).getIntent(), 1001);
                    return;
                } else {
                    MailActivity.this.mOutputText.setText("The following error occurred:\n" + this.mLastError.toString());
                    return;
                }
            }
            MailActivity.this.mOutputText.setText("Request cancelled.");
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout activityLayout = new LinearLayout(this);
        activityLayout.setLayoutParams(new LayoutParams(-1, -1));
        activityLayout.setOrientation(1);
        activityLayout.setPadding(16, 16, 16, 16);
        ViewGroup.LayoutParams tlp = new ViewGroup.LayoutParams(-2, -2);
        this.mCallApiButton = new Button(this);
        this.mCallApiButton.setText(BUTTON_TEXT);
        this.mCallApiButton.setOnClickListener(new C03161());
        activityLayout.addView(this.mCallApiButton);
        this.mOutputText = new TextView(this);
        this.mOutputText.setLayoutParams(tlp);
        this.mOutputText.setPadding(16, 16, 16, 16);
        this.mOutputText.setVerticalScrollBarEnabled(true);
        this.mOutputText.setMovementMethod(new ScrollingMovementMethod());
        this.mOutputText.setText("Click the 'Call Gmail API' button to test the API.");
        activityLayout.addView(this.mOutputText);
        this.mProgress = new ProgressDialog(this);
        this.mProgress.setMessage("Calling Gmail API ...");
        setContentView((View) activityLayout);
        this.mCredential = GoogleAccountCredential.usingOAuth2(getApplicationContext(), Arrays.asList(SCOPES)).setBackOff(new ExponentialBackOff());
    }

    private void getResultsFromApi() {
        Log.d(this.TAG, "getResultFromApi");
        if (!isGooglePlayServicesAvailable()) {
            Log.d(this.TAG, "Google api available");
            acquireGooglePlayServices();
        } else if (this.mCredential.getSelectedAccountName() == null) {
            Log.d(this.TAG, "Choose account");
            chooseAccount();
        } else if (isDeviceOnline()) {
            Log.d(this.TAG, "Making credential request");
            new MakeRequestTask(this.mCredential).execute(new Void[0]);
        } else {
            Log.d(this.TAG, "Device is online");
            this.mOutputText.setText("No network connection available.");
        }
    }

    @AfterPermissionGranted(1003)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(this, "android.permission.GET_ACCOUNTS")) {
            String accountName = getPreferences(0).getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                this.mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
                return;
            }
            startActivityForResult(this.mCredential.newChooseAccountIntent(), 1000);
            return;
        }
        EasyPermissions.requestPermissions((Activity) this, "This app needs to access your Google account (via Contacts).", 1003, "android.permission.GET_ACCOUNTS");
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1000:
                if (resultCode == -1 && data != null && data.getExtras() != null) {
                    String accountName = data.getStringExtra("authAccount");
                    if (accountName != null) {
                        Editor editor = getPreferences(0).edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        this.mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                        return;
                    }
                    return;
                }
                return;
            case 1001:
                if (resultCode == -1) {
                    getResultsFromApi();
                    return;
                }
                return;
            case 1002:
                if (resultCode != -1) {
                    this.mOutputText.setText("This app requires Google Play Services. Please install Google Play Services on your device and relaunch this app.");
                    return;
                } else {
                    getResultsFromApi();
                    return;
                }
            default:
                return;
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    public void onPermissionsGranted(int requestCode, List<String> list) {
    }

    public void onPermissionsDenied(int requestCode, List<String> list) {
    }

    private boolean isDeviceOnline() {
        NetworkInfo networkInfo = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private boolean isGooglePlayServicesAvailable() {
        return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) == 0;
    }

    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    void showGooglePlayServicesAvailabilityErrorDialog(int connectionStatusCode) {
        GoogleApiAvailability.getInstance().getErrorDialog(this, connectionStatusCode, 1002).show();
    }
}
