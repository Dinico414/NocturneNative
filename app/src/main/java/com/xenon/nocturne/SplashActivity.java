package com.xenon.nocturne;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import android.view.View;
import android.os.Build;
import android.content.SharedPreferences;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "AppPrefs";
    private static final String IS_APP_RUNNING_KEY = "isAppRunning";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isAppRunning = prefs.getBoolean(IS_APP_RUNNING_KEY, false);

        // Check if the app is already running AND NOT the root task
        if (isAppRunning && !isTaskRoot()) {
            // If the app is running and this is NOT the root task, skip the splash screen
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            startActivity(intent);
            finish();
            return; // Important: Exit the method to prevent further execution
        }

        // Set the flag to true if the app is launched fresh and is the root task
        if (!isAppRunning && isTaskRoot()) {
            prefs.edit().putBoolean(IS_APP_RUNNING_KEY, true).apply();
        }

        // Set the flag to false if the app is not running and is not the root task
        if (!isAppRunning && !isTaskRoot()) {
            prefs.edit().putBoolean(IS_APP_RUNNING_KEY, false).apply();
        }

        setContentView(R.layout.activity_splash);

        // Hide the navigation bar and status bar
        hideSystemUI();

        GifImageView gifImageView = findViewById(R.id.gif_background);
        try {
            // Load your GIF from resources
            GifDrawable gifDrawable = new GifDrawable(getResources(), R.drawable.inshot_20250220_173925074);

            // Set the GIF to play only once.
            // If you need it to loop more than once, set the desired count.
            gifDrawable.setLoopCount(1);

            // Add an animation listener to trigger when the GIF completes a loop.
            gifDrawable.addAnimationListener(loopNumber -> {
                // When the GIF completes its single loop, start MainActivity.
                // Wrapping in runOnUiThread ensures we’re on the UI thread.
                runOnUiThread(() -> {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    startActivity(intent);
                    finish();
                });
            });

            // Set the GIF drawable to the ImageView
            gifImageView.setImageDrawable(gifDrawable);
        } catch (IOException e) {
            e.printStackTrace();
            // Fallback: If the GIF fails to load, transition after a fixed delay.
            gifImageView.postDelayed(() -> {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                startActivity(intent);
                finish();
            }, 5000);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Reset the flag when the app is closed
        if (!isAppRunning(this, getPackageName())) {
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            prefs.edit().putBoolean(IS_APP_RUNNING_KEY, false).apply();
        }
    }

    private boolean isAppRunning(Context context, String packageName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningProcesses = activityManager.getRunningAppProcesses();
        if (runningProcesses != null) {
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.processName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for sticky immersive mode, replace with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            );

        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }
}