package com.example.phonecontrol;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView logText;
    private Button btnToggle;
    private boolean isServiceRunning = true;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String command = intent.getStringExtra("command");
            addLog("Command: " + command);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logText = findViewById(R.id.logText);
        btnToggle = findViewById(R.id.btnToggleService);

        // Start the service by default
        startControlService();

        btnToggle.setOnClickListener(v -> {
            if (isServiceRunning) {
                stopService(new Intent(this, ControlService.class));
                btnToggle.setText("Start Service");
                addLog("Service Stopped manually.");
            } else {
                startControlService();
                btnToggle.setText("Stop Service");
                addLog("Service Started manually.");
            }
            isServiceRunning = !isServiceRunning;
        });

        registerReceiver(receiver, new IntentFilter("COMMAND_RECEIVED"));
    }

    private void startControlService() {
        Intent serviceIntent = new Intent(this, ControlService.class);
        startService(serviceIntent);
        addLog("Service Started...");
    }

    private void addLog(String message) {
        String timestamp = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        logText.append("[" + timestamp + "] " + message + "\n");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
