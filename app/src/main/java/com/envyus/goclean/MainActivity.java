package com.envyus.goclean;
//raven in

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;




//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        manager2.beginTransaction().replace(R.id.container, new mapview()).commit();
    }
    public void switchToFragment3() {
        FragmentManager manager3 = getSupportFragmentManager();
        manager3.beginTransaction().replace(R.id.container, new profileview()).commit();
    }

}
