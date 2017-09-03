package com.example.edelsteindo.androidproject;

import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Fragment;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edelsteindo.androidproject.Model.Model;
import com.example.edelsteindo.androidproject.Model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;


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
    private ProgressBar progressBar;

    static final int REQUEST_WRITE_STORAGE = 11;

    private FirebaseUser currentUser ;
    private FirebaseAuth mAuth;

    private Fragment fragment;

    public PostsListFragment(){

    }

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View contextView =inflater.inflate(R.layout.fragment_posts_list, container, false);
        seacrh_text = (EditText)contextView.findViewById(R.id.search_text);
        seacrh_text.setVisibility(View.GONE);
        progressBar=(ProgressBar)contextView.findViewById(R.id.progress_bar);
        //for searching by userName
        seacrh_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count)
            {

                Model.instace.getAllPostsAndObserve(new Model.GetAllPostsAndObserveCallback() {
                    @Override
                    public void onComplete(List<Post> list) {
                        data.clear();
                        data.addAll(list);

                        List<Post> temp = new LinkedList<Post>();

                        for(int i =0; i<data.size();i++)
                        {
                            if(data.get(i).getUser().contains(s))
                                temp.add(data.get(i));

                        }
                        data.clear();
                        data.addAll(temp);
                        //ordering by uploading date
                        Collections.sort(data, new ListOrderComperator());
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancel() {
                    }
                });


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        list = (ListView)contextView.findViewById(R.id.post_list);

        Model.instace.getAllPostsAndObserve(new Model.GetAllPostsAndObserveCallback() {
            @Override
            public void onComplete(List<Post> list) {
                data.clear();
                data.addAll(list);
                //ordering by uploading date
                Collections.sort(data, new ListOrderComperator());
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancel() {
            }
        });

        adapter = new PostListAdapter();
        list.setAdapter(adapter);

        //functionality of likes
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
        // asking permission to write to external storage
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
    public void onStart() {
        super.onStart();
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
                //opening add post fragment
                fragment = AddPostFragment.newInstance();

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

                fragmentTransaction.replace(R.id.main_fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                return true;

            case R.id.search:
                if (seacrh_text.getVisibility() == View.VISIBLE)
                {
                    seacrh_text.setVisibility(View.GONE);
                    seacrh_text.setText("");
                    Model.instace.getAllPostsAndObserve(new Model.GetAllPostsAndObserveCallback() {
                        @Override
                        public void onComplete(List<Post> list) {
                            data.clear();
                            data.addAll(list);
                            //sorting by uploaded date
                            Collections.sort(data, new ListOrderComperator());
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
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
                    //sorting by uploaded date
                    Collections.sort(data, new ListOrderComperator());
                    adapter.notifyDataSetChanged();
                }
                return true;

            case R.id.log_out:
                mAuth.signOut();
                getActivity().finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //comperator that compares between two posts
    class ListOrderComperator implements Comparator<Post>{
        @Override
        public int compare(Post o1, Post o2) {
            return -1*(int)(o1.getTimeMs() - o2.getTimeMs());
        }
    }
    // the adapter of the main post list
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
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.post_list_row, null);
            }

            final ImageView postPic = (ImageView) convertView.findViewById(R.id.postPic);
            TextView userName = (TextView) convertView.findViewById(R.id.userName);
            TextView likesNum = (TextView) convertView.findViewById(R.id.likesNum);
            TextView postDate = (TextView) convertView.findViewById(R.id.post_date);
            TextView description = (TextView) convertView.findViewById(R.id.description);
            final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progress_bar_row);

            final ImageButton optionBtn = (ImageButton) convertView.findViewById(R.id.option_popup);

            final Post p = data.get(position);

            if(!p.getUser().equals(currentUser.getEmail())){
                optionBtn.setVisibility(View.GONE);

            }
            else{
                optionBtn.setVisibility(View.VISIBLE);
                optionBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Creating the instance of PopupMenu
                        PopupMenu popup = new PopupMenu(getActivity(), optionBtn);
                        //Inflating the Popup using xml file
                        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                        //registering popup with OnMenuItemClickListener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()){
                                    case R.id.edit_popup:
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable(EditPostFragment.POST_ARG,p);
                                        //opening edit post fragment
                                        fragment = EditPostFragment.newInstance();
                                        fragment.setArguments(bundle);

                                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                        transaction.replace(R.id.main_fragment_container, fragment);
                                        transaction.addToBackStack("List");
                                        transaction.commit();
                                        break;

                                    case R.id.delete_popup:
                                        Model.instace.removePost(p);
                                        Toast.makeText(getActivity(),"post delete",Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        break;
                                }
                                return true;
                            }
                        });

                        popup.show();//showing popup menu
                    }
                });
            }

            userName.setText(p.getUser());
            likesNum.setText(p.getNumOfLikes() + " liked this post");
            postDate.setText("uploaded "+DeltaTimeString(p.getDateTime()));

            description.setText(p.getDescription());

            postPic.setTag(p.getPostPicUrl());

            postPic.setImageDrawable(null);

            if (p.getPostPicUrl() != null && !p.getPostPicUrl().isEmpty() && !p.getPostPicUrl().equals("")) {
                progressBar.setVisibility(View.VISIBLE);
                Model.instace.getImage(p.getPostPicUrl(), new Model.GetImageListener() {
                    @Override
                    public void onSuccess(Bitmap image) {

                        String tagUrl = postPic.getTag().toString();
                        if (tagUrl.equals(p.getPostPicUrl())) {
                            postPic.setImageBitmap(image);

                        }
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFail() {
                    }
                });
            }

            return convertView;

        }
        private String DeltaTimeString(Calendar cal){
            Date now = Calendar.getInstance().getTime();
            long diff = now.getTime() - cal.getTime().getTime();
            if(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) >= 365){
                return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)%365+" years ago";
            }
            else if(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) > 0){
                return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)+" days ago";
            }
            else if (TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS) > 0){
                return TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS)+" hours ago";
            }
            else if(TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS) > 0){
                return TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS)+" minutes ago";
            }
            else{
                return "just now";
            }

        }

    }


}

