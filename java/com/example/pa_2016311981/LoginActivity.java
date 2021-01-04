package com.example.pa_2016311981;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private DatabaseReference mPostReference;

    EditText username, password;
    TextView signup;
    Button loginbt;
    String ID = "";
    String PW = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = getIntent();
        mPostReference = FirebaseDatabase.getInstance().getReference("user_list");

        username = (EditText) findViewById(R.id.id);
        password = (EditText) findViewById(R.id.pw);
        loginbt = (Button)findViewById(R.id.button);
        signup = (TextView)findViewById(R.id.signup);

        ID = intent.getStringExtra("ID");
        PW = intent.getStringExtra("PW");
        username.setText(ID);
        password.setText(PW);

        loginbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ID = username.getText().toString();
                        PW = password.getText().toString();

                        for(DataSnapshot checksnapshot : dataSnapshot.getChildren()){
                            String idCheck = checksnapshot.getKey();
                            FirebaseUserInfo get = checksnapshot.getValue(FirebaseUserInfo.class);

                            String pwCheck = get.password;

                            if (idCheck.equals(ID) && pwCheck.equals(PW)){
                                String fnCheck = get.fullname;
                                String bdCheck = get.birthday;
                                String emCheck = get.email;

                                Intent intent = new Intent(LoginActivity.this, MainpageActivity.class);
                                intent.putExtra("ID", idCheck);
                                intent.putExtra("PW", pwCheck);
                                intent.putExtra("FN", fnCheck);
                                intent.putExtra("BD", bdCheck);
                                intent.putExtra("EM", emCheck);

                                startActivity(intent);
                                return;

                            } else if (idCheck.equals(ID)){
                                ID = "";
                                PW = "";
                                Toast.makeText(LoginActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        ID = "";
                        PW = "";
                        Toast.makeText(LoginActivity.this, "Wrong Username", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent_signup = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent_signup);
            }
        });

    }
}
