package com.envyus.goclean;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
//suhail
    private TextView mTextMessage;
    private FirebaseAuth mAuth;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    public static final int requestCode = 1;
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        while(!checkAndRequestPermissions()){}
        if(currentUser==null)
        {
            Intent GotoLogin=new Intent(this,LoginActivity.class);
            startActivity(GotoLogin);
        }
        if (ContextCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CAMERA}, requestCode);

        }
        if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);

        }
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.container, new cameraview()).commit();
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        if (bottomNavigationView != null) {
            // Set action to perform when any menu-item is selected.
            bottomNavigationView.setOnNavigationItemSelectedListener(
                    new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.navigation_home:
                                    switchToFragment1();
                                    return true;

                                case R.id.navigation_dashboard:
                                    switchToFragment2();
                                    return true;

                                case R.id.navigation_notifications:
                                    switchToFragment3();
                                    return true;
                            }

                            return false;
                        }
                    });
        }

    }
    public void switchToFragment1() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.container, new cameraview()).commit();
    }
    public void switchToFragment2() {
        FragmentManager manager2 = getSupportFragmentManager();
        manager2.beginTransaction().replace(R.id.container, new MapsActivity()).commit();
    }
    public void switchToFragment3() {
        FragmentManager manager3 = getSupportFragmentManager();
        manager3.beginTransaction().replace(R.id.container, new profileview()).commit();
    }
    private  boolean checkAndRequestPermissions() {

        int internet=ContextCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET);
        int camera=ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int loc = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int loc2 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (internet != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (loc2 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (loc != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(this,listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

}
