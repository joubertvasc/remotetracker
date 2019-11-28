// Google MAPS API KEY: 0mZ7YqNK8747evEyxpTHA9SGRREeq0hDMnFp61g
package jv.android.remotetracker.activity;

import jv.android.remotetracker.utils.Permissions;
import jv.android.remotetracker.utils.Preferences;
import jv.android.remotetracker.R;
import jv.android.remotetracker.data.Config;
import jv.android.remotetracker.interfaces.IGetClass;
import jv.android.remotetracker.receiver.ListeningAdminReceiver;
import jv.android.remotetracker.receiver.ListeningSms;
import jv.android.utils.Logs;
import jv.android.utils.Message;
import jv.android.utils.PhoneUtils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;

import com.google.gson.Gson;

public class RemoteTrackerActivity extends AppCompatActivity implements IGetClass {

    private static final int TOS = 1;
    private static final int PASSWORD = 2;
    private static final int PREFERENCES = 3;

    private boolean isATMVersion = false;
    private int atmIcon = -1;
    private BroadcastReceiver receiver;

    private Preferences preferences;

    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Device Admin
        DevicePolicyManager mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName mDeviceAdmin = new ComponentName(RemoteTrackerActivity.this, ListeningAdminReceiver.class);

        // For IceCreamSandwich
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");

        //Extends BroadcastReceiver
        receiver = new ListeningSms();
        registerReceiver(receiver, filter);

        preferences = new Preferences(getApplicationContext());
        preferences.setDeviceAdmin(false);

        ImageView ivWizard = findViewById(R.id.ivWizard);

        // ATM version does not uses ToS and does not ask to buy pro version
        if (isATMVersion) {
            preferences.setTosAccepted(true);

            Bitmap myImg = BitmapFactory.decodeResource(getResources(), atmIcon);
            ivWizard.setImageBitmap(myImg);
        }

        preferences.setProVersion(true);
        preferences.setDeviceAdmin(mDPM.isAdminActive(mDeviceAdmin));

        askPermissions();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_help) {
            doHelp();
        } else if (id == R.id.action_about) {
            doAbout();
        } else if (id == R.id.action_cards) {
            doCards();
        } else if (id == R.id.action_contacts) {
            doContacts();
        } else if (id == R.id.action_history) {
            doHistory();
        } else if (id == R.id.action_config) {
            doPreferences();
        }

        return super.onOptionsItemSelected(item);
    }


    private void askPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1 &&
                (!Permissions.canAccessFineLocation(this) ||
                        !Permissions.canAccessCoarseLocation(this) ||
                        !Permissions.canReadExternalStorage(this) ||
                        !Permissions.canWriteExternalStorage(this) ||
                        !Permissions.canAccessCamera(this) ||
                        !Permissions.canReadPhoneState(this) ||
                        !Permissions.canReadContacts(this) ||
                        !Permissions.canWriteContacts(this) ||
                        !Permissions.canReadSMS(this) ||
                        !Permissions.canReceiveSMS(this) ||
                        !Permissions.canSendSMS(this))) {
            requestPermissions(Permissions.INITIAL_PERMS, Permissions.INITIAL_REQUEST);
        } else {
            permissionsOk();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Permissions.INITIAL_REQUEST) {
            if (grantResults.length != permissions.length) {
                Message.showMessage(RemoteTrackerActivity.this, getString(R.string.warning), getString(R.string.avPermissionsMissing));
                finish();
            } else {
                // Used to write hidden options
                SharedPreferences.Editor prefEditor = preferences.getSharedPrefs().edit();
                prefEditor.putBoolean("tos_accepted", true);

                if (preferences.getImsi1().equals("")) {
                    PhoneUtils pu = new PhoneUtils(getApplicationContext());

                    preferences.setImsi1(pu.getIMSI());
                    preferences.setImsiAlias1(getString(R.string.msgOwnerSIMCard));
                    prefEditor.putString("imsi1", preferences.getImsi1());
                    prefEditor.putString("imsiAlias1", preferences.getImsiAlias1());
                }

                prefEditor.apply();

                permissionsOk();
            }
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);

        // closing Entire Application
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!preferences.getDefaultEMailAddress().trim().equals("")) {
                finish();
            } else {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setIcon(R.drawable.exclamation);
                alert.setTitle(getString(R.string.warning));
                alert.setMessage(getString(R.string.avNoEMail));
                alert.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        doPreferences();
                    }
                });

                alert.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                        dialog.dismiss();
                    }
                });

                alert.create();
                alert.show();

            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == TOS) {
                int resultado = data.getIntExtra("result", -1);

                if (resultado == 1) {
                    finish();
                }
            } else if (requestCode == PASSWORD) {
                int resultado = data.getIntExtra("result", -1);

                switch (resultado) {
                    case 0:
                        // Nothing to do...
                        return;
                    case 1:
                        finish();
                }
            } else if (requestCode == PREFERENCES) {
                preferences.readPreferences();
            }
        }
    }

    private void doPreferences() {
        Intent pref = new Intent(RemoteTrackerActivity.this, PreferencesActivity.class);
        startActivityForResult(pref, PREFERENCES);
    }

    private void doCards() {
        Intent cards = new Intent(RemoteTrackerActivity.this, Cards.class);
        startActivity(cards);
    }

    private void doContacts() {
        Intent emergency = new Intent(RemoteTrackerActivity.this, Emergency.class);
        startActivity(emergency);
    }

    private void doHistory() {
        Intent history = new Intent(RemoteTrackerActivity.this, ListHistory.class);
        startActivity(history);
    }

    private void doHelp() {
        Intent help = new Intent(RemoteTrackerActivity.this, HelpActivity.class);
        startActivity(help);
    }

    private void doAbout() {
        Intent about = new Intent(RemoteTrackerActivity.this, AboutActivity.class);
        about.putExtra("ATM", (isATMVersion ? "true" : "false"));
        about.putExtra("ATMIcon", atmIcon);
        startActivity(about);
    }

    public void permissionsOk() {
        if (preferences.backupExists()) {
            preferences.importPreferences();
            verifyTosAcceptedOrNot();
        }
    }

    private void verifyTosAcceptedOrNot() {
        if (preferences.getDebug())
            Logs.startLog(this,"RemoteTracker", (preferences.isProVersion() ? "remotetrackerpro.txt" : "remotetracker.txt"));

        if (!preferences.getTosAccepted()) {
            if (isATMVersion) {
                preferences.setTosAccepted(true);
            } else {
                Intent intent = new Intent(RemoteTrackerActivity.this, ToS.class);
                startActivityForResult(intent, TOS);
            }
        } else if (!preferences.getPassword().equals("")) {
            Intent intent = new Intent(RemoteTrackerActivity.this, Password.class);
            intent.putExtra("password", preferences.getPassword());
            startActivityForResult(intent, PASSWORD);
        }
    }

    @Override
    public void onGetConfig(String json) {
        if (json != null && !json.trim().equals("")) {
            Gson g = new Gson();
            Config config = g.fromJson(json, Config.class);
            preferences.setParamsByConfig(config);
            verifyTosAcceptedOrNot();
        }
    }
}