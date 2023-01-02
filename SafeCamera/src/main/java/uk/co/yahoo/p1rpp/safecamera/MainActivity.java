/*
 * Copyright © 2022. Richard P. Parkins, M. A.
 * Released under GPL V3 or later
 *
 * This is a little utility app to get around the problem of the latest
 * (December 2022) Samsung Galaxy update breaking the camera app so that it
 * won't run even to take still pictures without Bluetooth permission,
 * which it only needs to connect to an external Bluetooth microphone
 * (if you have one, which I don't) when taking movies.
 *
 * This app checks to see if Bluetooth is enabled and saves the state.
 * If Bluetooth was enabled, it disables it.
 * It then launches the camera app.
 * When the camera app exits, it restores the enabled state of Bluetooth.
 *
 * If you have your side key programmed to open the camera app, you need
 * to reprogam it in Settings to open this app instead.
 *
 * Note that for this to work properly, you have to exit the camera app
 * via the Back button, and not via the Home button or the Recents button
 * since these won't force the camera app to finish() and come back to
 * this app to restore the Bluetooth state.
 */
package uk.co.yahoo.p1rpp.safecamera;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final String BUNDLE_WASENABLED = "key_was_enabled";
    private Bundle mSavedInstanceState;
    boolean mWasEnabled;

    @SuppressLint("MissingPermission")
    private void runCamera() {
        BluetoothManager bm =
            (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (bm != null) {
            BluetoothAdapter ba = bm.getAdapter();
            if (ba != null) {
                boolean isEnabled = ba.isEnabled();
                if (mSavedInstanceState == null) {
                    mWasEnabled = isEnabled;
                } else {
                    mWasEnabled = mSavedInstanceState.getBoolean(
                        BUNDLE_WASENABLED, isEnabled);
                }
                if (isEnabled) {
                    ba.disable();
                }
                Intent intent = Intent.makeMainActivity(
                    new ComponentName(
                        "com.sec.android.app.camera",
                        "com.sec.android.app.camera.Camera"));
                startActivityForResult(intent, 1);
            } else {
                Toast.makeText(this,
                    "SafeCamera failed to access Bluetooth adapter",
                    Toast.LENGTH_SHORT).show();
                finishAndRemoveTask();
            }
        } else {
            Toast.makeText(this,
                "SafeCamera failed to get a Bluetooth Manager",
                Toast.LENGTH_SHORT).show();
            finishAndRemoveTask();
        }
    }

    public void onRequestPermissionsResult(
        int requestCode, String[] permissions, int[] grantResults)
    {
        for (int i = 0; i < permissions.length; ++i) {
            if (   (permissions[i].equals("android.permission.BLUETOOTH_CONNECT"))
                && (grantResults[i] == PackageManager.PERMISSION_GRANTED)) {
                runCamera();
                return;
            }
        }
        Toast.makeText(this,
            "Sorry, SafeCamera can't run without BLUETOOTH_CONNECT permission",
            Toast.LENGTH_LONG).show();
        finishAndRemoveTask();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSavedInstanceState = savedInstanceState;
        if (checkSelfPermission("android.permission.BLUETOOTH_CONNECT")
            == PackageManager.PERMISSION_GRANTED)
        {
            runCamera();
        } else {
            requestPermissions(
                new String[] {"android.permission.BLUETOOTH_CONNECT"},
                1);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(BUNDLE_WASENABLED, mWasEnabled);
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BluetoothManager bm =
            (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (bm != null) {
            BluetoothAdapter ba = bm.getAdapter();
            if (ba != null) {
                if (mWasEnabled) {
                    ba.enable();
                } else {
                    ba.disable();
                }
            } else {
                Toast.makeText(this,
                    "SafeCamera failed to access Bluetooth adapter",
                    Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this,
                "SafeCamera failed to get a Bluetooth Manager",
                Toast.LENGTH_SHORT).show();
        }
        finishAndRemoveTask();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // we don't display anything, so no action required
    }
}
