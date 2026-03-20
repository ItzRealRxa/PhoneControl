package com.example.phonecontrol;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

public class CommandExecutor {
    private static final String TAG = "CommandExecutor";

    public static void execute(Context context, String command) {
        Log.d(TAG, "Executing: " + command);
        
        switch (command.toLowerCase()) {
            case "flash_on":
                toggleFlash(context, true);
                break;
            case "flash_off":
                toggleFlash(context, false);
                break;
            case "play_sound":
                playSound(context);
                break;
            case "get_battery":
                showBattery(context);
                break;
            case "none":
                break;
            default:
                Log.w(TAG, "Unknown command: " + command);
        }
    }

    private static void toggleFlash(Context context, boolean state) {
        CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, state);
            showToast(context, "Flash " + (state ? "ON" : "OFF"));
        } catch (CameraAccessException | ArrayIndexOutOfBoundsException e) {
            Log.e(TAG, "Flash Error", e);
        }
    }

    private static void playSound(Context context) {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        MediaPlayer mp = MediaPlayer.create(context, notification);
        mp.start();
        showToast(context, "Playing Sound...");
    }

    private static void showBattery(Context context) {
        BatteryManager bm = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
        int level = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        showToast(context, "Battery: " + level + "%");
    }

    private static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
