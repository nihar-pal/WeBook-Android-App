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
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spacester.webook.AdapterPost;
import com.spacester.webook.ModelPost;
import com.spacester.webook.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SearchFragment extends Fragment {

    AdapterPost adapterPost;
    List<ModelPost> modelPosts;
    RecyclerView post;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        post = view.findViewById(R.id.rv);
        post.setLayoutManager(new LinearLayoutManager(getContext()));
        modelPosts = new ArrayList<>();
        getAllPost();

        EditText editText = view.findViewById(R.id.search);
        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                filterPost(editText.getText().toString());
            }
            return false;
        });

        return view;
    }

    private void filterPost(String query) {
        FirebaseDatabase.getInstance().getReference("post")
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        modelPosts.clear();
                        for (DataSnapshot ds: snapshot.getChildren()){
                            ModelPost modelPost = ds.getValue(ModelPost.class);
                            if (Objects.requireNonNull(modelPost).getTitle().toLowerCase().contains(query.toLowerCase()) ||
                                    modelPost.getDescription().toLowerCase().contains(query.toLowerCase())){
                                modelPosts.add(modelPost);
                            }
                        }
                        Collections.reverse(modelPosts);
                        adapterPost = new AdapterPost(getActivity(), modelPosts);
                        post.setAdapter(adapterPost);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

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
                            modelPosts.add(modelPost);
                        }
                        Collections.reverse(modelPosts);
                        adapterPost = new AdapterPost(getActivity(), modelPosts);
                        post.setAdapter(adapterPost);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

}