package com.xenon.nocturne;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.os.Build;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                startActivity(intent);
                finish();
            }, 5000);
        }
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