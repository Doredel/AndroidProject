package com.example.edelsteindo.androidproject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends Activity {
  
    private Fragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragment = PostsListFragment.newInstance();
            fragmentTransaction.add(R.id.main_fragment_container, fragment);
            fragmentTransaction.commit();
        } else {
            fragment = getFragmentManager().findFragmentById(R.id.post_list);
        }
    }



}

