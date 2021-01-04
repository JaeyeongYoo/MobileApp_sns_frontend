package com.example.pa_2016311981;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class PostRecycleAdapter extends RecyclerView.Adapter<PostRecycleAdapter.ViewHolder> {
    private Context context;
    private ArrayList<PostItem> posts;
    private StorageReference profileImageRef = FirebaseStorage.getInstance().getReference();


    final long ONE_MEGABYTE = 20480 * 20480;

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv1, tv2, username_tv;
        ImageView tv3, profileimg_tv;
        ViewHolder(View itemView){
            super(itemView);

            tv1 = itemView.findViewById(R.id.content_tv);
            tv2 = itemView.findViewById(R.id.tag_tv);
            tv3 = itemView.findViewById(R.id.image_tv);

            username_tv = itemView.findViewById(R.id.profileun_tv);
            profileimg_tv = itemView.findViewById(R.id.profileimg_tv);
        }
    }

    PostRecycleAdapter(Context context, ArrayList<PostItem> items){
        this.context = context;
        this.posts = new ArrayList<PostItem>();
        this.posts = items;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        PostRecycleAdapter.ViewHolder holder = new PostRecycleAdapter.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final PostRecycleAdapter.ViewHolder holder, int position) {
        PostItem post = posts.get(position);
        holder.tv1.setText(posts.get(position).getContent());
        holder.tv2.setText(posts.get(position).getTag());
        holder.username_tv.setText(posts.get(position).getUsername());

        profileImageRef.child("Posts/" + posts.get(position).getKey() + ".jpg").getBytes(ONE_MEGABYTE).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.tv3.setImageBitmap(bitmap);
            }
        });

        profileImageRef.child("Profiles/" + posts.get(position).getUsername() + ".jpg").getBytes(ONE_MEGABYTE).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.profileimg_tv.setImageBitmap(bitmap);
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


}
