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

    public List<Post> getAllPost() {
        return PostSql.getAllPosts(modelSql.getReadableDatabase());
        //return this.modelMem.getAllPost();
    }

    public boolean addPost(Post post) {
        return PostSql.addPost(modelSql.getWritableDatabase(),post);
        //return this.modelMem.addPost(post);
    }

    public void removePost(String id) {
        PostSql.removePost(modelSql.getWritableDatabase(),id);
        //return this.modelMem.removePost(id);
    }

    public Post getPost(String id){
        return PostSql.getPost(modelSql.getReadableDatabase(),id);
        //return this.modelMem.getPost(id);
    }
}
