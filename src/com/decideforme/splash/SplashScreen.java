package com.decideforme.splash;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.decideforme.R;
import com.decideforme.dashboard.HomeActivity;

public class SplashScreen extends Activity {
    
    private long totalTimeForSplash = 5000;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        
        // thread for displaying the SplashScreen
        TimerTask task = new TimerTask() {
			@Override
			public void run() {
				finish();
				Intent mainIntent = new Intent().setClass(SplashScreen.this, HomeActivity.class);
				startActivity(mainIntent);				
			}
        };
        Timer timer = new Timer();
        timer.schedule(task, totalTimeForSplash);
    }
    
}