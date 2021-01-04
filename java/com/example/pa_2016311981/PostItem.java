package com.example.pa_2016311981;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class PostItem {
    String content;
    String tag;
    String username;
    String key;

    public PostItem() {}

//    public PostItem(String content, String tag, String username,String key) {
//        this.content = content;
//        this.tag = tag;
//        this.username = username;
//        this.key = key;
//    }

    public PostItem(String content, String tag, String username) {
        this.content = content;
        this.tag = tag;
        this.username = username;
    }

    public void setKey(String key) { this.key = key; }
    public String getKey() {
        return key;
    }

    public String getContent(){
        return content;
    }

    public String getTag(){
        return tag;
    }

    public String getUsername() {return username; }


}
