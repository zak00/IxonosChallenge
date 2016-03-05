package com.github.zak0.ixonoschallenge;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class LocationFragment extends Fragment {

    private static final String TAG = "LocationFragment";
    private static View view;
    private MapView mapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return (ViewGroup) inflater.inflate(R.layout.fragment_location, null);

        // Inflate only if no root view exists. Maps Fragment does not like to exists in multiple instances...
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }

        try {
            view = inflater.inflate(R.layout.fragment_location, container, false);
        } catch (InflateException e) {
        }

        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        // Check for connection and show banner if not.
        if(!((MainActivity) getActivity()).isInternetAvailable())
            ((MainActivity) getActivity()).showNoInternetBanner();

        else
            initMap();

        return view;

    }

    // Inits the map and location related things.
    private void initMap() {

        // Define location listener
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {

            // Animate the map to zoom into current location if/when available.
            @Override
            public void onLocationChanged(Location location) {
                final Location loc = location;
                mapView.getMapAsync(new OnMapReadyCallback() {

                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
                        CameraUpdate camUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);

                        String streetAddress = "";
                        String fullAddress = "";
                        Geocoder geocoder = new Geocoder(getContext());
                        try {
                            List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                            streetAddress += addressList.get(0).getAddressLine(0);

                            fullAddress = streetAddress;
                            fullAddress += " " + addressList.get(0).getSubAdminArea();
                            fullAddress += " " + addressList.get(0).getCountryCode();

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (IndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }

                        TextView textViewAddress = (TextView) view.findViewById(R.id.textViewAddress);
                        textViewAddress.setTypeface(MainActivity.TfTungstenRndMedium);
                        textViewAddress.setText(fullAddress);

                        Log.d(TAG, "LatLng: "+latLng.toString());
                        // Zoom into current location
                        googleMap.animateCamera(camUpdate);

                        // And place a nice marker as well
                        googleMap.clear();
                        googleMap.addMarker(new MarkerOptions().position(latLng).title("Some Street 123").snippet(streetAddress));
                    }
                });

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        // Check if app has GPS permissions
        if (getContext().getPackageManager().checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, getContext().getPackageName()) == PackageManager.PERMISSION_GRANTED) {
            // Subscribe for location updates.
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            //Log.d(TAG, "app has GPS permission");
        }
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mapView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        mapView.onLowMemory();
        super.onLowMemory();
    }

}