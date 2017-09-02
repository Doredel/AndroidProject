package com.example.edelsteindo.androidproject;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;


public class LoginActivity extends Activity {

    private Fragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //opening sign in fragment
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragment = SignInFregment.newInstance();
            fragmentTransaction.add(R.id.login_fragment_container, fragment);
            fragmentTransaction.commit();
        } else {
            fragment = getFragmentManager().findFragmentById(R.id.login_fragment_container);
        }

    }



}