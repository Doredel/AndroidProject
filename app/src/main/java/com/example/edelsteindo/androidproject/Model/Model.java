package com.example.edelsteindo.androidproject.Model;

import com.example.edelsteindo.androidproject.MyApplication;

import java.util.List;

/**
 * Created by Dor-New on 15/07/2017.
 */

public class Model {
    public final static Model instace = new Model();

    private MemModel modelMem;
    private SqlModel modelSql;

    public Model() {
        modelMem = new MemModel();
        modelSql = new SqlModel(MyApplication.getMyContext());
    }

    public List<User> getAllUsers() {
        return null;
    }

    public boolean addUser(User user) {
        return false;
    }

    public boolean removeUser(String userName) {
        return false;
    }

    public User getUser(String userName){
        return null;
    }
}
