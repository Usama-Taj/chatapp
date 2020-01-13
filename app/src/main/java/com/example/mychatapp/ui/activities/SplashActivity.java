package com.example.mychatapp.ui.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mychatapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_TIME_MS = 1000;
    private Handler mHandler;
    private Runnable mRunnable;
    private ImageView mLogo;
    private Animation mAnimation;
    private String email;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mLogo = findViewById(R.id.splash_logo);
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                preferences = getSharedPreferences("chat",MODE_PRIVATE);
                email = preferences.getString("email", null);
                if (FirebaseAuth.getInstance().getCurrentUser() != null){
                    UserListingActivity.startActivity(SplashActivity.this);
                }
                else {
                    LoginActivity.startIntent(SplashActivity.this);
                }
                //startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        };
        mHandler.postDelayed(mRunnable, SPLASH_TIME_MS);
        mAnimation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.splash_animation);
        mLogo.startAnimation(mAnimation);
    }
}
