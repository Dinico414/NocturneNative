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


        if (isAppRunning && !isTaskRoot()) {

            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            startActivity(intent);
            finish();
            return;
        }


        if (!isAppRunning && isTaskRoot()) {
            prefs.edit().putBoolean(IS_APP_RUNNING_KEY, true).apply();
        }


        if (!isAppRunning && !isTaskRoot()) {
            prefs.edit().putBoolean(IS_APP_RUNNING_KEY, false).apply();
        }

        setContentView(R.layout.activity_splash);


        hideSystemUI();

        GifImageView gifImageView = findViewById(R.id.gif_background);
        try {
            GifDrawable gifDrawable = new GifDrawable(getResources(), R.drawable.inshot_20250220_173925074);


            gifDrawable.setLoopCount(1);


            gifDrawable.addAnimationListener(loopNumber -> runOnUiThread(() -> {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                startActivity(intent);
                finish();
            }));


            gifImageView.setImageDrawable(gifDrawable);
        } catch (IOException e) {

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


        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }
}