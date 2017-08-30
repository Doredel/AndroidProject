package com.example.edelsteindo.androidproject.Model;

import java.io.Serializable;
import java.security.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by saportane on 27/06/2017.
 */

public class Post implements Serializable {
    private String id;
    private String postPicUrl;
    private int numOfLikes;
    private boolean active;
    private String description;
    private String user;
    private double lastUpdateDate;
    private List<String> likedUsers;
    private long timeMs;


    public Post() {
        genRandomId();
        this.likedUsers = new LinkedList<String>();
        this.setTimeMs(GregorianCalendar.getInstance().getTimeInMillis());

    }

    public Post(Post post) {
        this.setId(post.getId());
        this.setUser(post.getUser());
        this.setPostPicUrl(post.getPostPicUrl());
        this.setNumOfLikes(post.getNumOfLikes());
        this.setDescription(post.getDescription());
        this.setActive(post.isActive());
        this.setLastUpdateDate(post.getLastUpdateDate());
        this.setLikedUsers(post.getLikedUsers());
        this.setTimeMs(post.getTimeMs());
    }


    public Post(String user, String description, String postPicUrl, int numOfLikes, boolean active) {
        genRandomId();
        this.likedUsers = new LinkedList<String>();
        this.postPicUrl = postPicUrl;
        this.numOfLikes = numOfLikes;
        this.active = active;
        this.description = description;
        this.user = user;
        this.setLastUpdateDate(0);
        this.setTimeMs(GregorianCalendar.getInstance().getTimeInMillis());
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

    public long getTimeMs() {
        return timeMs;
    }

    public void setTimeMs(long timeMs) {
        this.timeMs = timeMs;
    }

    public Calendar getDateTime(){
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(this.getTimeMs());
        return calendar;
    }

    public void setDateTime(Calendar calendar){
        this.setTimeMs(calendar.getTimeInMillis());
    }

    private void genRandomId(){
        Random rand = new Random();
        this.setId((new Integer(rand.nextInt(Integer.MAX_VALUE))).toString());
    }


}
