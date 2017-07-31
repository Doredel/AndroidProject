package com.example.edelsteindo.androidproject;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link SignInFregment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignInFregment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private Fragment fragment;
    public SignInFregment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SignInFregment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignInFregment newInstance() {
        SignInFregment fragment = new SignInFregment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("SignIn", "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_sign_in_fregment, container, false);
        
        //setting the buttons events
        Button sign_in_btn = (Button)contentView.findViewById(R.id.signInBtn);
        sign_in_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //// TODO: 27/06/2017  check if password is correct 
                Intent intent = new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
            }
        });
        
        Button sign_up_btn = (Button)contentView.findViewById(R.id.signUpBtn);
        sign_up_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //// TODO: 27/06/2017  saving data and stuff
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragment = SignUpFregment.newInstance();
                fragmentTransaction.replace(R.id.login_fragment_container,fragment);
                fragmentTransaction.commit();
            }
        });
        Log.d("SignIn", "onCreateView");
        return contentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
