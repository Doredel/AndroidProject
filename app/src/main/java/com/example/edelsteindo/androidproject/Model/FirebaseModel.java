package com.example.edelsteindo.androidproject.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by edelsteindo on 07/08/2017.
 */

public class FirebaseModel {

    private Map<String, Object> createValues(Post post){
        Map<String, Object> value = new HashMap<>();
        value.put("id", post.getId());
        value.put("postPicUrl", post.getPostPicUrl());
        value.put("numOfLikes", post.getNumOfLikes());
        value.put("active", post.isActive());
        value.put("description", post.getDescription());
        value.put("user", post.getUser());
        value.put("lastUpdateDate", ServerValue.TIMESTAMP);
        value.put("likedUsers",post.getLikedUsers());
        value.put("timeMs", post.getTimeMs());

        return value;
    }

    public void addPost(Post post){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("posts");
        myRef.child(post.getId()).setValue(createValues(post));

    }

    public void removePost(String id){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("posts");
        myRef.child(id).removeValue();
    }

    public void updatePost(Post post){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("posts");
        myRef.child(post.getId()).setValue(createValues(post));
    }

    interface GetPostCallback {
        void onComplete(Post post);

        void onCancel();
    }

    public void getPost(String stId, final GetPostCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("posts");
        myRef.child(stId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                callback.onComplete(post);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onCancel();
            }
        });
    }

    interface GetAllPostsAndObserveCallback {
        void onComplete(List<Post> list);
        void onCancel();
    }
    public void getAllPostsAndObserve(final GetAllPostsAndObserveCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("posts");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Post> list = new LinkedList<Post>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Post post = snap.getValue(Post.class);
                    list.add(post);
                }
                callback.onComplete(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onCancel();
            }
        });
    }

    //-------------------------------------------------------------------------------------

    public void saveImage(Bitmap imageBmp, String name, final Model.SaveImageListener listener){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imagesRef = storage.getReference().child("images").child(name);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                listener.fail();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                listener.complete(downloadUrl.toString());
            }
        });
    }


    public void getImage(String url, final Model.GetImageListener listener){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference httpsReference = storage.getReferenceFromUrl(url);
        final long ONE_MEGABYTE = 1024 * 1024;
        httpsReference.getBytes(3* ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap image = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                listener.onSuccess(image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                listener.onFail();
            }
        });
    }



}
