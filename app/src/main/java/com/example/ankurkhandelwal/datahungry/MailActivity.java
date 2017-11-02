package com.example.ankurkhandelwal.datahungry;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.LoginFilter;
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

import com.example.ankurkhandelwal.datahungry.Models.Email;
import com.example.ankurkhandelwal.datahungry.Utils.PreferencesManager;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Base64;
import com.google.api.client.util.Data;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.Gmail.Builder;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartHeader;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.EasyPermissions.PermissionCallbacks;

public class MailActivity extends AppCompatActivity implements PermissionCallbacks {
    private static final String BUTTON_TEXT = "Get Gmails mail here";
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
    Activity activity;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout activityLayout = new LinearLayout(this);
        activityLayout.setLayoutParams(new LayoutParams(-1, -1));
        activityLayout.setOrientation(LinearLayout.VERTICAL);
        activityLayout.setPadding(16, 16, 16, 16);
        ViewGroup.LayoutParams tlp = new ViewGroup.LayoutParams(-2, -2);

        this.mCallApiButton = new Button(this);
        this.mCallApiButton.setText(BUTTON_TEXT);
        this.mCallApiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallApiButton.setEnabled(false);
                mOutputText.setText("");
                getResultsFromApi();
                mCallApiButton.setEnabled(true);
            }
        });

        activityLayout.addView(this.mCallApiButton);
        mOutputText = new TextView(this);
        mOutputText.setLayoutParams(tlp);
        mOutputText.setPadding(16, 16, 16, 16);
        mOutputText.setVerticalScrollBarEnabled(true);
        mOutputText.setMovementMethod(new ScrollingMovementMethod());
        mOutputText.setText("Click here to get Emails.");
        activityLayout.addView(this.mOutputText);
        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Calling Gmail API ...");
        setContentView((View) activityLayout);
        activity = this;
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
            new MakeRequestTask(this.mCredential, MailActivity.this).execute(new Void[0]);
        } else {
            Log.d(this.TAG, "Device is online");
            this.mOutputText.setText("No network connection available.");
        }
    }

    @Override public void onBackPressed() {
        super.onBackPressed();

    }

    private static class MakeRequestTask extends AsyncTask<Void, Void, ArrayList<Email>> {
        private Exception mLastError = null;
        private Gmail mService = null;
        private WeakReference<MailActivity> activityWeakReference;

        MakeRequestTask(GoogleAccountCredential credential, MailActivity context) {
            this.mService = new Builder(AndroidHttp.newCompatibleTransport(), JacksonFactory.getDefaultInstance(), credential).setApplicationName("DataHungry").build();
            Log.d("MailActivity", "MakingRequestTask =" + this.mService.getBaseUrl() + " & user =" + this.mService.users().messages().toString());
            activityWeakReference = new WeakReference<MailActivity>(context);
        }

        protected ArrayList<Email> doInBackground(Void... params) {
            try {
                Log.d("MailActivity", "doInBackGround =" + params.toString());
                return getDataFromApi();
            } catch (Exception e) {
                this.mLastError = e;
                cancel(true);
                return null;
            }
        }

        private ArrayList<Email> getDataFromApi() throws IOException {
            String user = "me";
            ArrayList<Email> emailList = new ArrayList<Email>();
            String query = "in:inbox";
            ListMessagesResponse listResponse = this.mService.users().messages().list(user).setMaxResults((long) 5).setQ(query).execute();
            //int i=0;
            for (Message label : listResponse.getMessages()) {
                Message m =  this.mService.users().messages().get(user, label.getId()).execute();
                String a ="";
                try{
                    String sender_name = "";
                    String email_snippet = m.getSnippet();
                    List<MessagePart> parts = m.getPayload().getParts();
                    List<MessagePartHeader> headers = m.getPayload().getHeaders();
                    Long date = m.getInternalDate();
                    for(MessagePartHeader header:headers){
                        String name = header.getName();
                        if(name.equals("From")||name.equals("from")){
                            sender_name = header.getValue();
                            break;
                        }
                    }

                    byte[] bodyBytes = Base64.decodeBase64(m.getPayload().getParts().get(0).getBody().getData().trim().toString());
                    String email_body = new String(bodyBytes, "UTF-8");
                    Log.d("bb", email_body );

                    String email_subject = "";
                    Log.d(activityWeakReference.get().TAG, "email_sender = "+sender_name);
                    Log.d(activityWeakReference.get().TAG, "email_snippet = "+email_snippet);
                    Log.d(activityWeakReference.get().TAG, "date = "+date);
                    Email email = new Email(sender_name,email_snippet,email_body,date);
                    emailList.add(email);
                }catch(Exception e){
                    e.getMessage();
                }

            }
            return emailList;
        }

        protected void onPreExecute() {
            MailActivity activity = activityWeakReference.get();
            if (activity == null) return;

            activityWeakReference.get().mOutputText.setText("");
            Log.d("MailActivity", "Async preExecute");
            activityWeakReference.get().mProgress.show();
        }

        protected void onPostExecute(ArrayList<Email> output) {
            MailActivity activity = activityWeakReference.get();
            if (activity == null) return;

            if(activityWeakReference.get().mProgress != null && activityWeakReference.get().mProgress.isShowing())
            {
                activityWeakReference.get().mProgress.hide();
            }
            if (output == null || output.size() == 0) {
                activityWeakReference.get().mOutputText.setText("No results returned.");
                return;
            }
            DataHungryApplication.emailArrayList = output;
            DataHungryApplication.prefs.edit().putString("email_account",String.valueOf(activityWeakReference.get().mCredential.getSelectedAccountName())).apply();
            activityWeakReference.get().onBackPressed();
//            activityWeakReference.get().mOutputText.setText(TextUtils.join("\n", output));
        }

        protected void onCancelled() {
            MailActivity activity = activityWeakReference.get();
            if (activity == null) return;

            if(activityWeakReference.get().mProgress != null && activityWeakReference.get().mProgress.isShowing())
            {
                activityWeakReference.get().mProgress.hide();
            }
            if (this.mLastError != null) {
                Log.d("MailActivity", "Async OnCancelled");
                if (this.mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    Log.d("MailActivity", "GooglePlayServicesAvailabilityIOException");
                    activityWeakReference.get().showGooglePlayServicesAvailabilityErrorDialog(((GooglePlayServicesAvailabilityIOException) this.mLastError).getConnectionStatusCode());
                    return;
                } else if (this.mLastError instanceof UserRecoverableAuthIOException) {
                    Log.d("MailActivity", "UserRecoverableAuthIOException");
                    activityWeakReference.get().startActivityForResult(((UserRecoverableAuthIOException) this.mLastError).getIntent(), 1001);
                    return;
                } else {
                    activityWeakReference.get().mOutputText.setText("The following error occurred:\n" + this.mLastError.toString());
                    return;
                }
            }
            activityWeakReference.get().mOutputText.setText("Request cancelled.");
        }
    }

    @AfterPermissionGranted(1003)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(this, "android.permission.GET_ACCOUNTS")) {
            String accountName = getPreferences(0).getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                this.mCredential.setSelectedAccountName(accountName);
                Log.d(TAG,"selected_account ="+accountName);
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
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
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
