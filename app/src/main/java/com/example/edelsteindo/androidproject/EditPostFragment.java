package com.example.edelsteindo.androidproject;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.edelsteindo.androidproject.Model.Model;
import com.example.edelsteindo.androidproject.Model.Post;


/**
 * Created by Dor-New on 24/08/2017.
 */

public class EditPostFragment extends AddPostFragment {

    public static final String POST_ARG = "POST_ARG";

    public static EditPostFragment newInstance() {
        EditPostFragment fragment = new EditPostFragment();
        //getting the details of the post
        Bundle args = new Bundle();
        args.putSerializable(POST_ARG, post);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            post = (Post)getArguments().getSerializable(POST_ARG);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = super.onCreateView(inflater,container,savedInstanceState);

        description.setText(post.getDescription());

        imageView.setTag(post.getPostPicUrl());
        if (post.getPostPicUrl() != null && !post.getPostPicUrl().isEmpty() && !post.getPostPicUrl().equals("")) {

            Model.instace.getImage(post.getPostPicUrl(), new Model.GetImageListener() {
                @Override
                public void onSuccess(Bitmap image) {
                    String tagUrl = imageView.getTag().toString();
                    if (tagUrl.equals(post.getPostPicUrl())) {
                        imageView.setImageBitmap(image);
                        imageBitmap = image;
                    }
                }

                @Override
                public void onFail() {
                }
            });
        }
        return contentView;
    }

    @Override
    public void onClick(View v) {
        //saving the changes
        if (imageBitmap != null) {
            post.setDescription(description.getText().toString());

            if(isChanged) {
                Model.instace.saveImage(imageBitmap, post.getId() + ".jpg", new Model.SaveImageListener() {
                    @Override
                    public void complete(String url) {
                        post.setPostPicUrl(url);
                        Model.instace.updatePost(post);
                    }

                    @Override
                    public void fail() {

                    }
                });
            }
            else{
                Model.instace.updatePost(post);
            }


            getFragmentManager().popBackStack();

        } else {
            Toast toast = Toast.makeText(MyApplication.getMyContext(), "Please choose your picture first", Toast.LENGTH_SHORT);
            toast.show();
        }

    }
}
