package com.example.pa_2016311981;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class NewpostActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int PICK_IMAGE = 777;
    private static final int POST_IMAGE = 666;
    private StorageReference mStorageRef, mPostStorageRef;
    private DatabaseReference mPostReference;

    DrawerLayout drawerLayout;

    ImageView imageButton;
    EditText contentET, tagET;
    CheckBox checkBox;
    String content = "";
    String tag = "";

    ImageView image;
    Uri currentImageUri, postImageUri;
    TextView profileun;

    Button button;

    int tab_num;
    String username, password, fullname, birthday, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newpost);

        mStorageRef = FirebaseStorage.getInstance().getReference("Profiles");
        mPostStorageRef = FirebaseStorage.getInstance().getReference("Posts");
        mPostReference = FirebaseDatabase.getInstance().getReference("Posts");


        Intent intent = getIntent();

        username = intent.getStringExtra("ID");
        password = intent.getStringExtra("PW");
        fullname = intent.getStringExtra("FN");
        birthday = intent.getStringExtra("BD");
        email = intent.getStringExtra("EM");

        tab_num = intent.getIntExtra("Tab", 0);

        Toolbar tb = (Toolbar)findViewById(R.id.main_toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_newpost);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, tb, R.string.app_name, R.string.app_name);
        drawerToggle.syncState();



        //Profile Function
        View header = navigationView.getHeaderView(0);
        Menu menu = navigationView.getMenu();
        MenuItem fullnameItem = menu.findItem(R.id.item1);
        MenuItem birthdayItem = menu.findItem(R.id.item2);
        MenuItem emailItem = menu.findItem(R.id.item3);

        image = (ImageView) header.findViewById(R.id.profileimage);
        profileun = (TextView) header.findViewById(R.id.profileusername);

        //image.setImageBitmap(null);
        profileun.setText(username);
        fullnameItem.setTitle(fullname);
        birthdayItem.setTitle(birthday);
        emailItem.setTitle(email);

        try {
            Date date = new SimpleDateFormat("yyyyMMdd").parse(birthday);
            String formatBD = new SimpleDateFormat("yyyy/MM/dd").format(date);
            birthdayItem.setTitle(formatBD);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final long ONE_MEGABYTE = 20480 * 20480;

        mStorageRef.child(username + ".jpg").getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                image.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
            }
        });

        //profile function

        //post function
        imageButton = (ImageView)findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, POST_IMAGE);
            }
        });

        contentET = (EditText)findViewById(R.id.contentedit);
        tagET = (EditText)findViewById(R.id.tagedit);
        content = contentET.getText().toString();
        tag = tagET.getText().toString();
        checkBox = (CheckBox)findViewById(R.id.checkBox);


        button = (Button)findViewById(R.id.createbt);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if(contentET.getText().toString().length() == 0){
                    Toast.makeText(NewpostActivity.this, contentET.getText().toString(), Toast.LENGTH_SHORT).show();
                }else {
                    StorageReference postImageRef;
                    DatabaseReference postContentRef, newPostRef;
                    String postID;

                    //Map<String, Object> childUpdate = new HashMap<>();
                    Map<String, Object> postvalues = null;

                    if(checkBox.isChecked()){
                        postContentRef = mPostReference.child("public");
                        newPostRef = postContentRef.push();

                    } else{
                        postContentRef = mPostReference.child("personal/" + username);
                        newPostRef = postContentRef.push();
                    }

                    postImageRef = mPostStorageRef;

                    FirebaseContentInfo post = new FirebaseContentInfo(contentET.getText().toString(), tagET.getText().toString(), username);
                    postID = newPostRef.getKey();
                    postvalues = post.toMap();
                    //childUpdate.put(postID, postvalues);
                    newPostRef.updateChildren(postvalues);

                    StorageReference newImageRef = postImageRef.child(postID + ".jpg");
                    UploadTask uploadTask = newImageRef.putFile(postImageUri);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //Toast.makeText(MainpageActivity.this, "Profile Image Register", Toast.LENGTH_SHORT).show();
                        }
                    });


                    intentFunction(username, password, fullname, birthday, email, tab_num);

                }


            }
        });

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(tab_num==0){
            intentFunction(username, password, fullname, birthday, email, 0);
        }
        else if(tab_num==1){
            intentFunction(username, password, fullname, birthday, email, 1);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.item1:{
                break;
            }
            case R.id.item2:
                break;
            case R.id.item3:
                break;
        }
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE) {

            currentImageUri = data.getData();
            image.setImageURI(data.getData());

            //upload image
            StorageReference ref = mStorageRef.child(username + ".jpg");
            UploadTask uploadTask = ref.putFile(currentImageUri);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //Toast.makeText(MainpageActivity.this, "Profile Image Register", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if(requestCode == POST_IMAGE){
            postImageUri = data.getData();
            Bitmap postImageBm = null;
            try{
                postImageBm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), postImageUri);
            }catch (IOException e){
                e.printStackTrace();
            }
            imageButton.setImageBitmap(postImageBm);
        }
    }

    public void intentFunction(String un, String pw, String fn, String bd, String em, int tab){
        Intent intent = new Intent(NewpostActivity.this, MainpageActivity.class);
        intent.putExtra("ID", un);
        intent.putExtra("PW", pw);
        intent.putExtra("FN", fn);
        intent.putExtra("BD", bd);
        intent.putExtra("EM", em);
        intent.putExtra("Tab", tab);
        startActivity(intent);
    }

}
