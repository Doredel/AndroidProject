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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddPostFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddPostFragment extends Fragment {
    private boolean picChosen =false;
    private Fragment fragment;
    private ImageView imageView;
    private Bitmap imageBitmap;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private Button upload_btn ;
    private Button upload_pic;
    FirebaseUser currentUser ;
    private FirebaseAuth mAuth;
     public AddPostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment AddPostFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        imageView = (ImageView) contentView.findViewById(R.id.chosenPic);
        upload_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("tag","upload_pic OnClickListener");
                dispatchTakePictureIntent();
                picChosen=false;
            }
        });
        //saving the post
        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //// TODO: 02/08/2017   user reconition & actual photo
                TextView description = (TextView) contentView.findViewById(R.id.newPostDescription);
                if (imageBitmap != null) {
                    final Post post = new Post(currentUser.getEmail(), description.getText().toString(), "", 0, true);
                    //Model.instace.addPost(post);
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

                    //returnig to the main fregmant
                    //.....

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragment = PostsListFragment.newInstance();
                    fragmentTransaction.replace(R.id.main_fragment_container, fragment);
                    fragmentTransaction.commit();
                } else {
                    Toast toast = Toast.makeText(MyApplication.getMyContext(), "Please choose your picture first", Toast.LENGTH_SHORT);
                    toast.show();
                }

                }
        });
        // Inflate the layout for this fragment
        return contentView;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
    }

    private void dispatchTakePictureIntent() {
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
        }
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("tag","onActivityResult2");
    }

}
