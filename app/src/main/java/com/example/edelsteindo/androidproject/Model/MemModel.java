package com.example.edelsteindo.androidproject.Model;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by edelsteindo on 26/06/2017.
 */

public class MemModel {
    public final static MemModel instace = new MemModel();

    private List<Post> data;

    public MemModel(){
        this.data = new LinkedList<Post>();
    }

    public List<Post> getAllPost() {
        return this.data;
    }

    public boolean addPost(Post post) {
        if(this.getPost(post.getId()) != null) {
            return false;
        }
        return this.data.add(post);
    }

    public boolean removePost(String id) {
        Post post = this.getPost(id);
        if(post != null) {
            return this.data.remove(post);
        }
        return false;
    }

    public Post getPost(String id){
        for (Post p:this.data) {
            if(p.getId().equals(id)){
                return p;
            }
        }
        return null;
    }

}
