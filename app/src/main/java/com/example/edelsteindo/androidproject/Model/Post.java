package com.example.edelsteindo.androidproject.Model;

import java.util.Random;

/**
 * Created by saportane on 27/06/2017.
 */

public class Post {
    private String id;
    private String postPicUrl;
    private int numOfLikes;
    private boolean active;
    private String description;
    private String user;

    public Post() {
        genRandomId();
    }

    public Post(Post post) {
        this.setId(post.getId());
        this.setUser(post.getUser());
        this.setPostPicUrl(post.getPostPicUrl());
        this.setNumOfLikes(post.getNumOfLikes());
        this.setDescription(post.getDescription());
        this.setActive(post.isActive());
    }


    public Post(String user, String description, String postPicUrl, int numOfLikes, boolean active) {
        genRandomId();
        this.postPicUrl = postPicUrl;
        this.numOfLikes = numOfLikes;
        this.active = active;
        this.description = description;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostPicUrl() {
        return postPicUrl;
    }

    public void setPostPicUrl(String postPicUrl) {
        this.postPicUrl = postPicUrl;
    }

    public int getNumOfLikes() {
        return numOfLikes;
    }

    public void setNumOfLikes(int numOfLikes) {
        this.numOfLikes = numOfLikes;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    private void genRandomId(){
        Random rand = new Random();
        this.setId((new Integer(rand.nextInt())).toString());
    }
}
