package com.github.zak0.ixonoschallenge;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class SplashScreen extends AppCompatActivity {

    // Google Analytics
    private Tracker tracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        tracker = application.getDefaultTracker();

        Intent launchIntent = getIntent();

        // Send event to Analytics if app was launched from a link
        if(launchIntent.getAction() != null) {
            if (launchIntent.getAction().equals("android.intent.action.VIEW"))
                tracker.send(new HitBuilders.EventBuilder().setCategory("LaunchSource").setAction("Link").build());
        }

        // Set font for "welcome" text.
        Typeface TfProximaNovaRegular = Typeface.createFromAsset(getAssets(), "fonts/ProximaNovaRegular.otf");
        ((TextView) findViewById(R.id.textViewAndroidChallenge)).setTypeface(TfProximaNovaRegular);

        // Show splash for 3000 ms, then launch MainActivity.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                SplashScreen.this.startActivity(intent);
                SplashScreen.this.finish();
            }
        }, 3000); // show splash for 3 seconds
    }



}
