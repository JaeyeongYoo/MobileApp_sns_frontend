package com.example.pa_2016311981;

import java.util.HashMap;
import java.util.Map;

public class FirebaseUserInfo {
    public String username;
    public String password;
    public String fullname;
    public String birthday;
    public String email;


    public FirebaseUserInfo () {
        //default constructor
    }

    public FirebaseUserInfo(String un, String pw, String fn, String bd, String em) {
        this.username = un;
        this.password = pw;
        this.fullname = fn;
        this.birthday = bd;
        this.email = em;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("username", username);
        result.put("password", password);
        result.put("fullname", fullname);
        result.put("birthday", birthday);
        result.put("email", email);

        return result;
    }

}
