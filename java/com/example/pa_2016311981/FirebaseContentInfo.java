package com.example.pa_2016311981;

import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class FirebaseContentInfo {
    String content;
    String tag;
    String username;

    FirebaseContentInfo() {}
    FirebaseContentInfo(String ct, String tg, String username){
        this.content = ct;
        this.tag = tg;
        this.username = username;
    }


    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("content", content);
        result.put("tag", tag);
        result.put("username", username);

        return result;
    }

}
