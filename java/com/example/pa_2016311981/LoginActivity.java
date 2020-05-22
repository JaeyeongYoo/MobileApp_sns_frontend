package com.example.pa_2016311981;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    EditText username, password;
    TextView signup;
    Button loginbt;
    String ID, PW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = getIntent();
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
                Intent intent = new Intent(LoginActivity.this, MainpageActivity.class);
                intent.putExtra("ID", username.getText().toString());
                intent.putExtra("PW", password.getText().toString());
                startActivity(intent);
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
