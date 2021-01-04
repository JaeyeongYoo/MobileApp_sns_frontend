package com.example.pa_2016311981;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
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

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainpageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static final int PICK_IMAGE = 777;
    final long ONE_MEGABYTE = 20480 * 20480;
    private StorageReference mStorageRef, postImageRef;
    private DatabaseReference postContentRef;

    DrawerLayout drawerLayout;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    ImageButton imageButton;

    ArrayList<PostItem> posts;

    ImageView image;
    Uri currentImageUri;
    Bitmap currentImageBm;
    byte[] curImageBmBytes;
    TextView profileun;

    int tab_num = 0;
    String username, password, fullname, birthday, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);

        mStorageRef = FirebaseStorage.getInstance().getReference("Profiles");

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
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
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

        //image.setImageBitmap(0);
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

        mStorageRef.child(username + ".jpg").getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                curImageBmBytes = bytes;
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


        posts = new ArrayList<PostItem>();
        PostItem posttest = new PostItem("testfromMainActivity", "testtag", username);
        PostItem posttest2 = new PostItem("testfromMainActivity22", "testtag", username);

        posts.add(posttest);
        posts.add(posttest2);

        adapter = new ViewPagerAdapter(getSupportFragmentManager(), 2, posts, username);

        viewPager = (ViewPager)findViewById(R.id.pager);
        viewPager.setAdapter(adapter);

        viewPager.setCurrentItem(tab_num);
        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);


        //post


        imageButton = (ImageButton)findViewById(R.id.plusbutton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tab_num = viewPager.getCurrentItem();
                Intent intent = new Intent(MainpageActivity.this, NewpostActivity.class);

                intent.putExtra("ID", username);
                intent.putExtra("PW", password);
                intent.putExtra("FN", fullname);
                intent.putExtra("BD", birthday);
                intent.putExtra("EM", email);

                intent.putExtra("Tab", tab_num);
                startActivity(intent);
            }
        });


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
    }


}
