package com.example.pa_2016311981;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class PublicFrag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<PostItem> items = new ArrayList<PostItem>();

    String username;

    RecyclerView recyclerView;
    PostRecycleAdapter recycleAdapter;

    DatabaseReference postContentRef;
    StorageReference postImageRef;

    public PublicFrag() {
        // Required empty public constructor
    }

    public PublicFrag(ArrayList<PostItem> item, String username) {
        this.items = item;
        this.username = username;
    }

    // TODO: Rename and change types and number of parameters
    public static PublicFrag newInstance(String param1, String param2) {
        PublicFrag fragment = new PublicFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        postContentRef = FirebaseDatabase.getInstance().getReference("Posts/public/");
        postImageRef = FirebaseStorage.getInstance().getReference("Posts/public/");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_personal, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.scrollview_list_pe);
        //listView = (ListView) view.findViewById(R.id.scrollview_list_pe);


        getFirebaseDatabase();

        //recyclerView.setHasFixedSize(true);
        recycleAdapter = new PostRecycleAdapter(getActivity(), items);
        recycleAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(recycleAdapter);


        return view;
    }
    public void getFirebaseDatabase(){
        final long ONE_MEGABYTE = 10240 * 10240;

        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear();
                for(DataSnapshot postSnapShot : dataSnapshot.getChildren()){
                    String key = postSnapShot.getKey();

                    final PostItem getpost = postSnapShot.getValue(PostItem.class);
                    getpost.setKey(key);

                    items.add(getpost);

                }
                recycleAdapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        postContentRef.addValueEventListener(postListener);
    }

}
