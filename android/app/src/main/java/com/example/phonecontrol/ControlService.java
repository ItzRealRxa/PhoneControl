package com.example.phonecontrol;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ControlService extends Service {
    private static final String TAG = "ControlService";
    private static final String API_URL = "http://prxacontrol.xo.je/api.php?key=1234"; 
    private static final String VERSION_URL = "http://prxacontrol.xo.je/version.php";
    private static final String CURRENT_VERSION = "1.0";
    private Handler handler;
    private Runnable runnable;
    private static final int INTERVAL = 5000; // 5 seconds

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        startForegroundService();
        checkUpdate(); // Check for newer version on start
        
        runnable = new Runnable() {
            @Override
            public void run() {
                fetchCommand();
                handler.postDelayed(this, INTERVAL);
            }
        };
        handler.post(runnable);
    }

    private void startForegroundService() {
        String channelId = "phone_control_channel";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Phone Control Service", NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setContentTitle("Phone Control Active")
                .setContentText("Listening for commands...")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .build();

        startForeground(1, notification);
    }

    private void fetchCommand() {
        new Thread(() -> {
            try {
                URL url = new URL(API_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();

                JSONObject json = new JSONObject(result.toString());
                String command = json.getString("command");

                if (!command.equals("none")) {
                    Log.d(TAG, "Command Received: " + command);
                    // Switch to main thread to show Toast/Execute
                    new Handler(getMainLooper()).post(() -> {
                        CommandExecutor.execute(getApplicationContext(), command);
                        // Send broadcast to update UI in MainActivity
                        Intent intent = new Intent("COMMAND_RECEIVED");
                        intent.putExtra("command", command);
                        sendBroadcast(intent);
                    });
                }
            } catch (Exception e) {
                Log.e(TAG, "Fetch Error: " + e.getMessage());
            }
        }).start();
    }

    private void checkUpdate() {
        new Thread(() -> {
            try {
                URL url = new URL(VERSION_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) result.append(line);
                reader.close();

                JSONObject json = new JSONObject(result.toString());
                String remoteVersion = json.getString("version");
                String downloadUrl = json.getString("url");

                if (!remoteVersion.equals(CURRENT_VERSION)) {
                    Log.d(TAG, "Update available: " + remoteVersion);
                    showUpdateNotification(remoteVersion, downloadUrl);
                }
            } catch (Exception e) {
                Log.e(TAG, "Update Check Failed: " + e.getMessage());
            }
        }).start();
    }

    private void showUpdateNotification(String version, String url) {
        // Send broadcast to UI or show simple notification
        Intent intent = new Intent("COMMAND_RECEIVED");
        intent.putExtra("command", "UPDATE_AVAILABLE: " + version);
        sendBroadcast(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        Log.d(TAG, "Service Stopped");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
