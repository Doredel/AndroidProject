package com.example.edelsteindo.androidproject.Model;

import android.media.Image;

/**
 * Created by saportane on 27/06/2017.
 */

public class Post {
    private String postPicUrl;
    private int numOfLikes;
    private boolean active;
    private String Description;
    private User poster;

    public Post(User poster) {
        this.poster = poster;
    }
    public String getPostPic() {
        return postPicUrl;
    }

    public void setPostPic(String postPic) {
        this.postPicUrl = postPic;
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
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public User getPoster() {
        return poster;
    }

    public void setPoster(User poster) {
        this.poster = poster;
    }
}
