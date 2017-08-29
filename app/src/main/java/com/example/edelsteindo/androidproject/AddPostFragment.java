package com.example.edelsteindo.androidproject;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import static android.app.Activity.RESULT_OK;
import com.example.edelsteindo.androidproject.Model.Model;
import com.example.edelsteindo.androidproject.Model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class AddPostFragment extends Fragment implements View.OnClickListener {
    protected Fragment fragment;

    protected Bitmap imageBitmap;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private FirebaseUser currentUser ;
    private FirebaseAuth mAuth;

    protected boolean isChanged = false;

    protected static Post post;

    protected ImageView imageView;
    protected Button upload_btn;
    protected Button cancel_btn;
    protected Button upload_pic;
    protected TextView description;

    public AddPostFragment() {
    }

    public static AddPostFragment newInstance() {
        AddPostFragment fragment = new AddPostFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mAuth=FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            setHasOptionsMenu(true);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View contentView = inflater.inflate(R.layout.fragment_add_post, container, false);
        upload_btn = (Button) contentView.findViewById(R.id.uploadBtn);
        upload_pic = (Button) contentView.findViewById(R.id.choosePicBtn);
        cancel_btn = (Button) contentView.findViewById(R.id.cancelBtn);
        imageView = (ImageView) contentView.findViewById(R.id.chosenPic);
        description = (TextView) contentView.findViewById(R.id.newPostDescription);
        upload_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("tag","upload_pic OnClickListener");
                dispatchTakePictureIntent();
            }
        });
        //saving the post
        upload_btn.setOnClickListener(this);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        // Inflate the layout for this fragment
        return contentView;
    }

    @Override
    public void onClick(View v) {

        if (imageBitmap != null) {
            post = new Post(currentUser.getEmail(), description.getText().toString(), "", 0, true);

            Model.instace.saveImage(imageBitmap, post.getId() + ".jpg", new Model.SaveImageListener() {
                @Override
                public void complete(String url) {
                    post.setPostPicUrl(url);
                    Model.instace.addPost(post);
                }

                @Override
                public void fail() {
                    Log.d("Fail","image error");
                }
            });
            getFragmentManager().popBackStack();
            Log.d("TAG", "onClick: aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

        } else {
            Toast toast = Toast.makeText(MyApplication.getMyContext(), "Please choose your picture first", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    protected void dispatchTakePictureIntent() {
        Log.d("tag","dispatchTakePictureIntent");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("tag","onActivityResult");
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK ) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
            isChanged = true;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
