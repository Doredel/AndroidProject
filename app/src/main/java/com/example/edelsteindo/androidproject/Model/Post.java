package com.example.edelsteindo.androidproject.Model;

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
}
