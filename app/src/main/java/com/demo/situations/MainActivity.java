package com.demo.situations;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements HomeFragment.OnFragmentInteractionListener
                  ,UserAccountFragment.OnFragmentInteractionListener
                  ,BottomNavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        LoadFragment(new HomeFragment().newInstance("",""));
    }

    @Override
    public void onFragmentInteraction(Uri uri){

    }

    public boolean LoadFragment(android.support.v4.app.Fragment fragment){
        if(fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContent,fragment)
                    .commit();
            return(true);
        }
        return(false);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        android.support.v4.app.Fragment fragment = null;
        switch(item.getItemId()){
            case R.id.navigation_home:
                fragment = new HomeFragment().newInstance("","");
                break;
            case R.id.navigation_account:
                fragment = new UserAccountFragment().newInstance("","");
                break;
        }
        return(LoadFragment(fragment));
    }
}
