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
        this.addPost(new Post("Nevo@gmail.com","Free food at... ","",69,true));
        this.addPost(new Post("Doredel98@gmail.com","Free food !!! yiay!!!!!!","",1,true));
        this.addPost(new Post("User1@gmail.com","1111111111111111111111111","",10,true));
        this.addPost(new Post("User2@gmail.com","2222222222222222222222222","",20,true));
        this.addPost(new Post("User3@gmail.com","3333333333333333333333333","",37,true));
        this.addPost(new Post("User4@gmail.com","4444444444444444444444444","",109,true));
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
