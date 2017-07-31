package com.example.edelsteindo.androidproject.Model;

import android.media.Image;

/**
 * Created by saportane on 27/06/2017.
 */

public class Post {
    private  String postPicURL;
    private int numOfLikes;
    private boolean active;
    private String description;
    private String userName;

    public Post( String userName) {
        this.userName = userName;
    }
    public String getPostPic() {
        return postPicURL;
    }

    public void setPostPic(String postPic) {
        this.postPicURL = postPic;
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
        description = description;
    }

    public String getUser() {
        return userName;
    }

    public void setPoster(String userName) {
        this.userName = userName;
    }
}
