package com.spacester.webook.Pages;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spacester.webook.AdapterPost;
import com.spacester.webook.ModelPost;
import com.spacester.webook.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProfileFragment extends Fragment {

    AdapterPost adapterPost;
    List<ModelPost> modelPosts;
    RecyclerView post;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView name = v.findViewById(R.id.name);
        TextView username = v.findViewById(R.id.username);
        TextView email = v.findViewById(R.id.email);

        FirebaseDatabase.getInstance().getReference().child("data").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    name.setText(snapshot.child("name").getValue().toString());

                    username.setText(snapshot.child("username").getValue().toString());

                    email.setText(snapshot.child("email").getValue().toString());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        post = v.findViewById(R.id.rv);
        post.setLayoutManager(new LinearLayoutManager(getContext()));
        modelPosts = new ArrayList<>();
        getAllPost();

       return v;
    }

    private void getAllPost() {
        FirebaseDatabase.getInstance().getReference("post")
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        modelPosts.clear();
                        for (DataSnapshot ds: snapshot.getChildren()){
                            ModelPost modelPost = ds.getValue(ModelPost.class);
                            assert modelPost != null;
                            if (modelPost.getUser().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                modelPosts.add(modelPost);
                            }
                        }
                        adapterPost = new AdapterPost(getActivity(), modelPosts);
                        post.setAdapter(adapterPost);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

}