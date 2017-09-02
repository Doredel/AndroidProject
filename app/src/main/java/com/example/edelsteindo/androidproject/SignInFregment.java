package com.example.edelsteindo.androidproject;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link SignInFregment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignInFregment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private Fragment fragment;
    private EditText password;
    private EditText userName;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    public SignInFregment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SignInFregment.
     */
    public static SignInFregment newInstance() {
        SignInFregment fragment = new SignInFregment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mAuth =FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        userName.setText("");
        password.setText("");
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_sign_in_fregment, container, false);
        progressBar = (ProgressBar) contentView.findViewById(R.id.progress_bar_login);
        progressBar.setVisibility(View.GONE);
        password = (EditText) contentView.findViewById(R.id.signInPass);
        userName = (EditText) contentView.findViewById(R.id.signInUserName);


        final Button sign_up_btn = (Button)contentView.findViewById(R.id.signUpBtn);
        //setting the buttons events
        final Button sign_in_btn = (Button)contentView.findViewById(R.id.signInBtn);
        sign_in_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!(password.getText().toString().matches("")||userName.getText().toString().matches("")))
                {
                    progressBar.setVisibility(View.VISIBLE);
                    sign_in_btn.setEnabled(false);
                    sign_up_btn.setEnabled(false);
                    //firebase authentication
                    mAuth.signInWithEmailAndPassword(userName.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        progressBar.setVisibility(View.GONE);
                                        sign_in_btn.setEnabled(true);
                                        sign_up_btn.setEnabled(true);
                                        Intent intent = new Intent(getActivity(),MainActivity.class);
                                        startActivity(intent);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        progressBar.setVisibility(View.GONE);
                                        sign_in_btn.setEnabled(true);
                                        sign_up_btn.setEnabled(true);
                                        Toast.makeText(getActivity(), task.getException().getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
                else
                {
                    Toast.makeText(getActivity(), "Please fill all of the fields",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        

        sign_up_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //opening sign up fragment
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragment = SignUpFregment.newInstance();
                fragmentTransaction.replace(R.id.login_fragment_container,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
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
