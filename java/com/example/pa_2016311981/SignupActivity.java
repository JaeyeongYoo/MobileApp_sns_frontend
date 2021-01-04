package com.example.pa_2016311981;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    private DatabaseReference mPostReference;

    String username="", password="", fullname="", email="", birthday=""; //sign up information

    Button button;
    EditText usrET, pwET, fnET, bdET, emET;

    ArrayList<String> data;

    private ValueEventListener checkRegister = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
            while(child.hasNext()){
                if (username.equals(child.next().getKey())) {
                    Toast.makeText(SignupActivity.this, "Please use another username", Toast.LENGTH_SHORT).show();
                    mPostReference.removeEventListener(this);
                    return;
                }
            }
            Intent intent2 = new Intent(SignupActivity.this, LoginActivity.class);
            intent2.putExtra("ID", username); //id 정보 담아서 login page로 넘어가기
            postFirebaseDatabase(true); //회원가입 정보 firebase에 올리기
            startActivity(intent2);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Intent intent = getIntent();



        mPostReference = FirebaseDatabase.getInstance().getReference("user_list");
        data = new ArrayList<String>();

        usrET = (EditText)findViewById(R.id.id);
        pwET = (EditText)findViewById(R.id.pw);
        fnET = (EditText)findViewById(R.id.fn);
        bdET = (EditText)findViewById(R.id.bd);
        emET = (EditText)findViewById(R.id.em);

        button = (Button)findViewById(R.id.signupbt);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = usrET.getText().toString();
                password = pwET.getText().toString();
                fullname = fnET.getText().toString();
                birthday = bdET.getText().toString();
                email = emET.getText().toString();

                //check blank
                if((username.length() * password.length() * fullname.length() * birthday.length() * email.length()) == 0) {

                    Toast.makeText(SignupActivity.this, "Please fill all blanks", Toast.LENGTH_SHORT).show();

                }else {

                    mPostReference.addListenerForSingleValueEvent(checkRegister);

                }
            }
        });


    }

    public void postFirebaseDatabase(boolean add) {
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postvalues = null;
        if(add){
            FirebaseUserInfo post = new FirebaseUserInfo(username, password, fullname, birthday, email);
            postvalues = post.toMap();
        }
        childUpdates.put(username, postvalues);
        mPostReference.updateChildren(childUpdates);

        clearET();
    }

    public void clearET() {
        usrET.setText("");
        pwET.setText("");
        fnET.setText("");
        bdET.setText("");
        emET.setText("");
        username = "";
        password = "";
        fullname="";
        birthday="";
        email="";
    }

}
