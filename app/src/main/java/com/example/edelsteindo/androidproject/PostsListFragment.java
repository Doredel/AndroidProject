package com.example.edelsteindo.androidproject;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.edelsteindo.androidproject.Model.Model;
import com.example.edelsteindo.androidproject.Model.Post;

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

    private List<Post> data;
    private ListView list;
    private PostListAdapter adapter;

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
        View contextView =inflater.inflate(R.layout.fragment_posts_list, container, false);

        Log.d("f", "onCreateView: ");
        list = (ListView)contextView.findViewById(R.id.post_list);
        //getallposts isn't implemented yet
        data = Model.instace.getAllPost();



        adapter = new PostListAdapter();
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return contextView;
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//    }

    @Override
    public void onAttach(Context context) {
        Log.d("f", "onAttach: ");
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        Log.d("f", "onDetach: ");
        super.onDetach();
    }

//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }

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


            ImageView postPic = (ImageView) convertView.findViewById(R.id.postPic);
            TextView userName = (TextView) convertView.findViewById(R.id.userName);
            TextView likesNum = (TextView) convertView.findViewById(R.id.likesNum);
            TextView isActive = (TextView) convertView.findViewById(R.id.isActive);
            TextView description = (TextView) convertView.findViewById(R.id.description);

            Post p = data.get(position);
            postPic.setImageResource(R.drawable.default_pic);//p.getPostPicUrl());
            userName.setText(p.getUser());
            likesNum.setText(p.getNumOfLikes()+"");
            isActive.setText(Boolean.toString(p.isActive()));
            description.setText(p.getDescription());
            return convertView;

        }
    }

}

