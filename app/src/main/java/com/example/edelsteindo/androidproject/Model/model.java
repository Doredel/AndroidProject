package com.example.edelsteindo.androidproject.Model;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by edelsteindo on 26/06/2017.
 */

public class model {
    public final static model instace = new model();

    model(){
        this.data.add(new User("Dor Edelstein","dor_edel","123"));
        this.data.add(new User("Nevo Saporta","nevoSap","123"));
    }

    private List<User> data = new LinkedList<User>();

    public List<User> getAllUsers(){
        return data;
    }

    public boolean addUser(User user){
        if(getUser(user.getUserName()) == null ) {
            data.add(user);
            return true;
        }
        return false;
    }

    public boolean removeUser(String userName){
        return data.remove(getUser(userName));
    }

    public boolean EditUser(String userName, User u){
        int index = data.indexOf(getUser(userName));

        if(index != -1){
            this.data.get(index).setFullName(u.getFullName());
            this.data.get(index).setPassword(u.getPassword());
            this.data.get(index).setUserName(u.getUserName());
            this.data.get(index).setProfilePic(u.getProfilePic());
        }

        return (index != -1);
    }

    public User getUser(String userName) {
        for (User u : data){
            if (u.getUserName().equals(userName)){
                return u;
            }
        }
        return null;
    }

}
