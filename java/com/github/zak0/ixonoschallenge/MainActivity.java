package com.github.zak0.ixonoschallenge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity { // AppCompatActivity {

    public static final String USERDATAFILE = "userdata";
    public static final String EMAILKEY = "email";
    public static final String FIRSTNAMEKEY = "firstName";
    public static final String LASTNAMEKEY = "lastName";

    private static final int INVALIDEMAILBANNER = 0;
    private static final int NOINTERNETBANNER = 1;

    // Google Analytics
    private Tracker tracker;

    public static Typeface TfProximaNovaExtrabold;
    public static Typeface TfProximaNovaRegular;
    public static Typeface TfTungstenRndBook;
    public static Typeface TfTungstenRndMedium;

    private SharedPreferences sharedPreferences;
    private User user = null;
    private TextView textViewName;

    private final String TAG = "MainActivity";
    private final String greetingFragment = "com.github.zak0.ixonoschallenge.GreetingFragment";
    private final String aboutFragment = "com.github.zak0.ixonoschallenge.AboutFragment";
    private final String locationFragment = "com.github.zak0.ixonoschallenge.LocationFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        tracker = application.getDefaultTracker();

        initNavigationDrawer();
        initTypefaces();

        // Check (and load) if user exists in storage.
        sharedPreferences = getSharedPreferences(USERDATAFILE, 0);
        String email = sharedPreferences.getString(EMAILKEY, null);
        String firstName = sharedPreferences.getString(FIRSTNAMEKEY, null);
        String lastName = sharedPreferences.getString(LASTNAMEKEY, null);

        textViewName = (TextView) findViewById(R.id.textViewUserName);
        textViewName.setTypeface(TfTungstenRndMedium);
        textViewName.setText("");

        TextView textViewCloseMenu = (TextView) findViewById(R.id.textViewCloseMenu);
        textViewCloseMenu.setTypeface(TfTungstenRndMedium);
        // Close drawer on click.
        textViewCloseMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
                drawerLayout.closeDrawer(Gravity.LEFT);
            }
        });

        if(email != null) {
            user = new User(email, firstName, lastName);
            textViewName.setText(user.getFirstName() + " " + user.getLastName());
        }

        // Show the signup (the greeting) fragment if no user is signed in.
        if(user == null) {
            // Show greetingFragment and send an event to Analitycs.
            replaceFragment(greetingFragment);

            tracker.send(new HitBuilders.EventBuilder().setCategory("GreetingScreen").setAction("Show").build());
        }

        else
            replaceFragment(locationFragment);

        // Set the menu button to function as drawer toggle.
        ImageView imageViewMenu = (ImageView) findViewById(R.id.imageViewMenu);
        imageViewMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
    }

    // Handles a fragment transaction.
    private void replaceFragment(String fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contentParent, Fragment.instantiate(this, fragment));
        transaction.commit();
    }


    private void initTypefaces() {
        TfProximaNovaExtrabold = Typeface.createFromAsset(getAssets(), "fonts/ProximaNovaExtrabold.otf");
        TfProximaNovaRegular = Typeface.createFromAsset(getAssets(), "fonts/ProximaNovaRegular.otf");
        TfTungstenRndBook = Typeface.createFromAsset(getAssets(), "fonts/TungstenRndBook.otf");
        TfTungstenRndMedium = Typeface.createFromAsset(getAssets(), "fonts/TungstenRndMedium.otf");
    }


    private void initNavigationDrawer() {
        // Init navigation drawer
        ArrayList<String> drawerMenuItems = new ArrayList<>();
        drawerMenuItems.add("Home");
        drawerMenuItems.add("About");
        drawerMenuItems.add("Logout");

        ListView drawerList = (ListView) findViewById(R.id.naviDrawerList);
        drawerList.setAdapter(new DrawerListAdapter(this, R.layout.drawer_list_item, R.id.textViewListItem, drawerMenuItems));
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String fragment = "";
                switch (position) {
                    case 0: // Home
                        if (user == null)
                            fragment = greetingFragment;
                        else {
                            fragment = locationFragment;
                            textViewName.setText(user.getFirstName() + " " + user.getLastName());
                        }

                        break;
                    case 1: // About
                        textViewName.setText("");
                        fragment = aboutFragment;
                        break;
                    case 2: // Logout
                        user = null;
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear(); // Delete user data
                        editor.commit();
                        Intent intent = new Intent(MainActivity.this, SplashScreen.class);
                        startActivity(intent);
                        MainActivity.this.finish();
                        break;


                }

                if (fragment.equals(""))
                    return;

                replaceFragment(fragment);
                DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
                drawerLayout.closeDrawer(Gravity.LEFT);
            }
        });
    }

    // Saves user to SharedPreferences
    public void saveUser(User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EMAILKEY, user.getEmail());
        editor.putString(FIRSTNAMEKEY, user.getFirstName());
        editor.putString(LASTNAMEKEY, user.getLastName());
        editor.commit();

        this.user = user;
        textViewName.setText(user.getFirstName() + " " + user.getLastName());

        // This method is called from greeting fragment, so go to location fragment after login.
        replaceFragment(locationFragment);
    }

    // Shows a banner for three seconds or until clicked.
    //
    private void showBanner(int bannerToShow) {
        final ImageView banner;

        if(bannerToShow == INVALIDEMAILBANNER)
            banner = (ImageView) findViewById(R.id.imageViewInvalidEmailBanner);
        else
            banner = (ImageView) findViewById(R.id.imageViewNoInternetBanner);

        ViewCompat.setAlpha(banner, 1.0f);

        final ImageView imageViewMenu = (ImageView) findViewById(R.id.imageViewMenu);

        // Dismiss when clicked.
        banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewCompat.setAlpha(v, 0.0f);

                // Bring menu button back to front to re-enable pressing it.
                imageViewMenu.bringToFront();
            }
        });

        // Handler for dismissing the banner after 3 seconds
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                ViewCompat.setAlpha(banner, 0.0f);
                // Bring menu button back to front to re-enable pressing it.
                imageViewMenu.bringToFront();
            }
        };
        handler.postDelayed(runnable, 3000);
    }

    public void showInvalidEmailBanner() {
        Log.d(TAG, "ShowInvalidEmailBanner() called");
        showBanner(INVALIDEMAILBANNER);
    }

    public void showNoInternetBanner() {
        showBanner(NOINTERNETBANNER);
    }

    // Returns true/false based on whether phone is connected to internet or not
    public boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
