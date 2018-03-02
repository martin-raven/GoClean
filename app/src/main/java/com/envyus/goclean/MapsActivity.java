package com.envyus.goclean;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

public class MapsActivity extends Fragment implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener,
        View.OnClickListener {

    //Our Map
    private GoogleMap mMap;

    //To store longitude and latitude from map
    private double longitude;
    private double latitude;
    ArrayList<markermodel> jobs;
    //Buttons
    private ImageButton buttonSave;
    private ImageButton buttonCurrent;
    private ImageButton buttonView;

    //Google ApiClient
    private GoogleApiClient googleApiClient;

    //
    private SupportMapFragment fragment;
    private  MapView mMapView;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_mapview, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.map);

        mMapView.onCreate(savedInstanceState);
        jobs=new ArrayList<markermodel>();
        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);
//        });
        //Initializing googleapi client
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        //Initializing views and adding onclick listeners

//        FragmentManager manager = getFragmentManager();
//        fragment = (SupportMapFragment) manager.findFragmentById(R.id.map);
////           if (fragment == null) {
//        FragmentTransaction transaction = manager.beginTransaction();
//        fragment = new SupportMapFragment();
//        transaction.replace(R.id.map, fragment, "added from seeker");
//        transaction.commit();
        httpcall();
        return rootView;
    }

    private void httpcall() {
        new JsonTask().execute("https://fxurj2bm0m.execute-api.ap-south-1.amazonaws.com/DEV/getdumppoints");
    }

    @Override
    public void onStart() {
        googleApiClient.connect();
        Log.d("GoogleAPI","Connected");
        super.onStart();
    }

    @Override
    public void onStop() {
        Log.e("GoogleAPI","Disconnected");
        googleApiClient.disconnect();
        super.onStop();
    }

    //Getting current location
    private void getCurrentLocation() {
        if(mMap!=null)
            mMap.clear();
        //Creating a location object
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            //Getting longitude and latitude
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat("LONG",(float) longitude);
            editor.putFloat("LAT",(float) latitude);
            editor.commit();

            //moving the map to location
            moveMap();
        }
    }

    //Function to move the map
    private void moveMap() {
        //String to display current latitude and longitude
        String msg = latitude + ", "+longitude;

        //Creating a LatLng Object to store Coordinates
        LatLng latLng = new LatLng(latitude, longitude);

        //Adding marker to map
        mMap.addMarker(new MarkerOptions()
                .position(latLng) //setting position
                .draggable(true) //Making the marker draggable
                .title("Current Location"))//Adding a title
                .setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker));

        //Moving the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        //Animating the camera
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //Displaying current coordinates in toast
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onConnected(Bundle bundle) {
        getCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        //Adding a new marker to the current pressed position
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .draggable(true));
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        //Getting the coordinates
        latitude = marker.getPosition().latitude;
        longitude = marker.getPosition().longitude;

        //Moving the map
        moveMap();
    }
    public void addmarker()
    {
        if(jobs!=null){
        ArrayList<markermodel> jobsAvailable = jobs;
        double lat,lang;
        try {
            for (int i = 0; i < jobs.size(); i++) {
                lat = Double.valueOf(jobsAvailable.get(i).getLatt());
                lang = Double.valueOf(jobsAvailable.get(i).getLong());
                mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lang))
                        .title(jobsAvailable.get(i).getDumbID()))
                        .setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_waste));

            }


        }catch (Exception e){e.printStackTrace();}
        }
    }
    @Override
    public void onClick(View v) {
        if(v == buttonCurrent){
            getCurrentLocation();
            moveMap();
        }
    }
    public class JsonTask extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {
            JSONObject parentObject = GetJsonObject.getJsonObjectFromUrl(params[0]);

            JSONArray childObject = null;
            try {
                childObject = parentObject.getJSONArray("Items");
                String res = childObject.toString();
                com.google.gson.Gson gson = new com.google.gson.Gson();

                Type collectionType = new TypeToken<Collection<markermodel>>() {}.getType();

                jobs = gson.fromJson(res,collectionType);
            } catch (Exception e) {
                Log.e("Exception ",String.valueOf(e));
            }
            Log.d("Landing",jobs.toString());
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            addmarker();
            super.onPostExecute(s);

        }
    }
}