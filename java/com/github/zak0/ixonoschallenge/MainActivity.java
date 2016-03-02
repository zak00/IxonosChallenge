package com.github.zak0.ixonoschallenge;

import android.media.Image;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity { // AppCompatActivity {

    private final String TAG = "MainActivity";
    private final String greetingFragment = "com.github.zak0.ixonoschallenge.GreetingFragment";
    private final String[] fragments = {"com.github.zak0.ixonoschallenge.GreetingFragment",
                                        "com.github.zak0.ixonoschallenge.AboutFragment",
                                        "com.github.zak0.ixonoschallenge.LocationFragment"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initNavigationDrawer();

        // Show the signup (the greeting) fragment if no user is signed in.
        replaceFragment(greetingFragment);

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

    private void replaceFragment(String fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contentParent, Fragment.instantiate(this, fragment));
        transaction.commit();

        if (fragment == "com.github.zak0.ixonoschallenge.LocationFragment") {

            Log.d(TAG, "setting position on the map");
            /*SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentMap);
            GoogleMap map = mapFragment.getMap();
            LatLng testPos = new LatLng(21,57);
            map.moveCamera(CameraUpdateFactory.newLatLng(testPos));
            CameraUpdate camUpdate = CameraUpdateFactory.newLatLng(testPos);
            map.animateCamera(camUpdate);*/
        }
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
                Log.d(TAG, "Drawer menu item clicked at position: " + Integer.toString(position));
                replaceFragment(fragments[position]);
                DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
                drawerLayout.closeDrawer(Gravity.LEFT);
            }
        });
    }

}
