package com.example.edelsteindo.androidproject;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edelsteindo.androidproject.Model.Model;
import com.example.edelsteindo.androidproject.Model.Post;

import java.util.LinkedList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link .} interface
 * to handle interaction events.
 * Use the {@link PostsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostsListFragment extends android.app.Fragment {

    private List<Post> data =  new LinkedList<Post>();
    private ListView list;
    private PostListAdapter adapter;

    private ImageView postPic;

    static final int REQUEST_WRITE_STORAGE = 11;

    public static PostsListFragment newInstance() {
        PostsListFragment fragment = new PostsListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        Log.d("f", "onCreate: ");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View contextView =inflater.inflate(R.layout.fragment_posts_list, container, false);

        Log.d("f", "onCreateView: ");
        list = (ListView)contextView.findViewById(R.id.post_list);
        //getallposts isn't implemented yet

        Model.instace.getAllPostsAndObserve(new Model.GetAllPostsAndObserveCallback() {
            @Override
            public void onComplete(List<Post> list) {
                data = list;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancel() {
            }
        });

        adapter = new PostListAdapter();
        list.setAdapter(adapter);

        boolean hasPermission = (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(getActivity(),new String[]{
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
        }


        return contextView;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.addPost).setEnabled(true);
        super.onPrepareOptionsMenu(menu);
    }

    class PostListAdapter extends BaseAdapter {

        LayoutInflater inflater = getActivity().getLayoutInflater();

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = inflater.inflate(R.layout.post_list_row,null);
            }


            postPic = (ImageView) convertView.findViewById(R.id.postPic);
            TextView userName = (TextView) convertView.findViewById(R.id.userName);
            TextView likesNum = (TextView) convertView.findViewById(R.id.likesNum);
            TextView isActive = (TextView) convertView.findViewById(R.id.isActive);
            TextView description = (TextView) convertView.findViewById(R.id.description);

            final Post p = data.get(position);
            userName.setText(p.getUser());
            likesNum.setText(p.getNumOfLikes()+"");
            isActive.setText(Boolean.toString(p.isActive()));
            description.setText(p.getDescription());

            postPic.setTag(p.getPostPicUrl());
            postPic.setImageResource(R.drawable.default_pic);

            if (p.getPostPicUrl() != null && !p.getPostPicUrl().isEmpty() && !p.getPostPicUrl().equals("")){
                Model.instace.getImage(p.getPostPicUrl(), new Model.GetImageListener() {
                    @Override
                    public void onSuccess(Bitmap image) {
                        String tagUrl = postPic.getTag().toString();
                        if (tagUrl.equals(p.getPostPicUrl())) {
                            postPic.setImageBitmap(image);
                        }
                    }

                    @Override
                    public void onFail() {
                    }
                });
            }


            return convertView;

        }
    }

}

