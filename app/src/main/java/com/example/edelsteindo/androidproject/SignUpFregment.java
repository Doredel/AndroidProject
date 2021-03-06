package com.example.edelsteindo.androidproject;

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
 * Use the {@link SignUpFregment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFregment extends Fragment {

    private FirebaseAuth mAuth;
    private  EditText email;
    private  EditText password;
    private  EditText confrimPassword;
    private ProgressBar progressBar;
    private  Button sign_up_btn;
    private  Button cancel_btn;
    public SignUpFregment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SignUpFregment.
     */
    public static SignUpFregment newInstance() {
        SignUpFregment fragment = new SignUpFregment();
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mAuth= FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_sign_up_fregment, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar_signUp);
        progressBar.setVisibility(View.GONE);
        email =(EditText)view.findViewById(R.id.newUserName);
        password = (EditText)view.findViewById(R.id.newPassword);
        confrimPassword = (EditText)view.findViewById(R.id.newConfrimPassword);
        sign_up_btn =(Button) view.findViewById(R.id.new_user);
        cancel_btn = (Button) view.findViewById(R.id.cancel_new_user);
        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check if all the parameters aren't empty
                if(!(email.getText().toString().matches("")||password.getText().toString().matches("")||confrimPassword.getText().toString().matches("")))
                {
                    //Check if the password and the confirm password are equal
                    if(password.getText().toString().matches(confrimPassword.getText().toString()))
                    {
                        //Check if the email is valid
                        if(isValidEmail(email.getText()))
                        {
                            progressBar.setVisibility(View.VISIBLE);
                            sign_up_btn.setEnabled(false);
                            mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(Task<AuthResult> task) {

                                            if (task.isSuccessful()) {
                                                // Sign in success, update UI with the signed-in user's information
                                                progressBar.setVisibility(View.GONE);
                                                sign_up_btn.setEnabled(true);
                                                //open the post list activity
                                                Intent intent = new Intent(getActivity(),MainActivity.class);
                                                startActivity(intent);

                                            } else {
                                                // If sign in fails, display a message to the user.
                                                progressBar.setVisibility(View.GONE);
                                                sign_up_btn.setEnabled(true);
                                                Toast.makeText(getActivity(), "Authentication failed." + task.getException().getMessage(),
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                        else
                        {
                            Toast toast = Toast.makeText(MyApplication.getMyContext(), "Please enter valid email", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                    else
                    {
                        Toast toast = Toast.makeText(MyApplication.getMyContext(), "The passwords are not identical", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                else
                {
                    Toast toast = Toast.makeText(MyApplication.getMyContext(), "Please fill all of the fields", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getFragmentManager().popBackStack();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }


    public static boolean isValidEmail(CharSequence target) {
        if(target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }



}
