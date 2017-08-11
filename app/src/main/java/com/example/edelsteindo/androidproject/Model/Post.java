package com.example.edelsteindo.androidproject.Model;

import java.util.LinkedList;
import java.util.List;
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
    public double lastUpdateDate;
    List<String> likedUsers;

    public Post() {
        this.likedUsers = new LinkedList<String>();
        genRandomId();
    }

    public Post(Post post) {
        this.likedUsers = new LinkedList<String>();
        this.setId(post.getId());
        this.setUser(post.getUser());
        this.setPostPicUrl(post.getPostPicUrl());
        this.setNumOfLikes(post.getNumOfLikes());
        this.setDescription(post.getDescription());
        this.setActive(post.isActive());
        this.setLastUpdateDate(post.getLastUpdateDate());
    }


    public Post(String user, String description, String postPicUrl, int numOfLikes, boolean active) {
        this.likedUsers = new LinkedList<String>();
        genRandomId();
        this.postPicUrl = postPicUrl;
        this.numOfLikes = numOfLikes;
        this.active = active;
        this.description = description;
        this.user = user;
        this.setLastUpdateDate(0);
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

    public void incNumOfLikes(){
        this.numOfLikes++;
    }

    public void decNumOfLikes(){
        this.numOfLikes--;
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

    public double getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(double lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public List<String> getLikedUsers() {
        return likedUsers;
    }

    public void setLikedUsers(List<String> likedUsers) {
        this.likedUsers.clear();
        this.likedUsers.addAll(likedUsers);
    }

    public void addLikedUser(String username) {
        this.likedUsers.add(username);
    }

    public void removeLikedUser(String username) {
        this.likedUsers.remove(username);
    }

    private void genRandomId(){
        Random rand = new Random();
        this.setId((new Integer(rand.nextInt(Integer.MAX_VALUE))).toString());
    }
}
