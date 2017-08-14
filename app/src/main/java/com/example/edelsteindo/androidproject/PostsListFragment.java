package com.example.edelsteindo.androidproject;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edelsteindo.androidproject.Model.Model;
import com.example.edelsteindo.androidproject.Model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Comparator;
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


    private List<Post> data = new LinkedList<Post>();
    private ListView list;
    private EditText seacrh_text;
    private PostListAdapter adapter;

    private ImageView postPic;

    static final int REQUEST_WRITE_STORAGE = 11;

    private FirebaseUser currentUser ;
    private FirebaseAuth mAuth;

    private Fragment fragment;

    public static PostsListFragment newInstance() {
        PostsListFragment fragment = new PostsListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        setHasOptionsMenu(true);

        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View contextView =inflater.inflate(R.layout.fragment_posts_list, container, false);

        seacrh_text = (EditText)contextView.findViewById(R.id.search_text);
        seacrh_text.setVisibility(View.GONE);
        seacrh_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count)
            {

                Model.instace.getAllPostsAndObserve(new Model.GetAllPostsAndObserveCallback() {
                    @Override
                    public void onComplete(List<Post> list) {
                        data.clear();
                        data.addAll(list);
                        adapter.notifyDataSetChanged();

                        List<Post> temp = new LinkedList<Post>();

                        for(int i =0; i<data.size();i++)
                        {
                            if(data.get(i).getUser().contains(s))
                                temp.add(data.get(i));

                        }
                        data.clear();
                        data.addAll(temp);
                        String st =data.size()+"";
                        Log.d("total posts",st);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancel() {
                        data.clear();
                        Log.d("error","blblblblblbl");
                    }
                });


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Log.d("f", "onCreateView: ");
        list = (ListView)contextView.findViewById(R.id.post_list);
        //getallposts isn't implemented yet

        Model.instace.getAllPostsAndObserve(new Model.GetAllPostsAndObserveCallback() {
            @Override
            public void onComplete(List<Post> list) {
                data.clear();
                data.addAll(list);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancel() {
            }
        });

        adapter = new PostListAdapter();
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Post post = data.get(position);
                String username = currentUser.getEmail();

                if(!post.getLikedUsers().contains(username)){
                    post.addLikedUser(username);
                    post.incNumOfLikes();
                    Model.instace.updatePost(post);
                }
                else{
                    post.removeLikedUser(username);
                    post.decNumOfLikes();
                    Model.instace.updatePost(post);
                }
            }
        });

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

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.addPost:
                item.setEnabled(false);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragment = AddPostFragment.newInstance();
                fragmentTransaction.replace(R.id.main_fragment_container, fragment);
                fragmentTransaction.commit();
            case R.id.search:
                if (seacrh_text.getVisibility() == View.VISIBLE)
                {
                    seacrh_text.setVisibility(View.GONE);
                } else
                {
                    seacrh_text.setVisibility(View.VISIBLE);
                    data.clear();
                    Model.instace.getAllPostsAndObserve(new Model.GetAllPostsAndObserveCallback() {
                        @Override
                        public void onComplete(List<Post> list) {
                            data.addAll(list);
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                    adapter.notifyDataSetChanged();
                }

        }
        return super.onOptionsItemSelected(item);
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
            final ProgressBar progressBar =(ProgressBar) convertView.findViewById(R.id.progress_bar);

            final Post p = data.get(position);
            userName.setText(p.getUser());
            likesNum.setText(p.getNumOfLikes()+"");
            isActive.setText(Boolean.toString(p.isActive()));
            description.setText(p.getDescription());

            postPic.setTag(p.getPostPicUrl());

            postPic.setImageResource(R.drawable.default_pic);
            progressBar.setVisibility(View.VISIBLE);
            if (p.getPostPicUrl() != null && !p.getPostPicUrl().isEmpty() && !p.getPostPicUrl().equals("")){
                Model.instace.getImage(p.getPostPicUrl(), new Model.GetImageListener() {
                    @Override
                    public void onSuccess(Bitmap image) {
                        String tagUrl = postPic.getTag().toString();
                        if (tagUrl.equals(p.getPostPicUrl())) {
                            postPic.setImageBitmap(image);
                            progressBar.setVisibility(View.GONE);
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

