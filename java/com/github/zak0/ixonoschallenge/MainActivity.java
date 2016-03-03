package com.github.zak0.ixonoschallenge;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity { // AppCompatActivity {

    public static final String USERDATAFILE = "userdata";
    public static final String EMAILKEY = "email";
    public static final String FIRSTNAMEKEY = "firstName";
    public static final String LASTNAMEKEY = "lastName";

    // Typefaces to use in text
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

        if(email != null) {
            user = new User(email, firstName, lastName);
            textViewName.setText(user.getFirstName() + " " + user.getLastName());
        }

        // Show the signup (the greeting) fragment if no user is signed in.
        if(user == null)
            replaceFragment(greetingFragment);
        else
            replaceFragment(locationFragment);

        // TODO: Go directly to user info and location if already logged in.

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

    private void initTypefaces() {
        TfProximaNovaExtrabold = Typeface.createFromAsset(getAssets(), "fonts/ProximaNovaExtrabold.otf");
        TfProximaNovaRegular = Typeface.createFromAsset(getAssets(), "fonts/ProximaNovaRegular.otf");
        TfTungstenRndBook = Typeface.createFromAsset(getAssets(), "fonts/TungstenRndBook.otf");
        TfTungstenRndMedium = Typeface.createFromAsset(getAssets(), "fonts/TungstenRndMedium.otf");
    }

    // Handles a fragment transaction.
    private void replaceFragment(String fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contentParent, Fragment.instantiate(this, fragment));
        transaction.commit();
    }

    private void initNavigationDrawer() {
        // Init navigation drawer
        ArrayList<String> drawerMenuItems = new ArrayList<>();
        drawerMenuItems.add("Home");
        drawerMenuItems.add("About");
        drawerMenuItems.add("Logout");

        ListView drawerList = (ListView) findViewById(R.id.naviDrawer);
        drawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, R.id.textViewListItem, drawerMenuItems));
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

                if(fragment.equals(""))
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

}
