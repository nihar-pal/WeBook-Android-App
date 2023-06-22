package com.spacester.webook.Pages;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.spacester.webook.R;

import java.util.HashMap;
import java.util.Objects;

public class CreateActivity extends AppCompatActivity {

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    Uri uri;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_create);

        findViewById(R.id.attach).setOnClickListener(view1 -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED){
                    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                    requestPermissions(permissions, PERMISSION_CODE);
                }
                else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("*/*");
                    startActivityForResult(intent, IMAGE_PICK_CODE);
                }
            }
            else {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("*/*");
                startActivityForResult(intent, IMAGE_PICK_CODE);
            }
        });

        FirebaseDatabase.getInstance().getReference().child("data").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            username = snapshot.child("username").getValue().toString();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        EditText title = findViewById(R.id.title);
        EditText desc = findViewById(R.id.desc);

        findViewById(R.id.upload).setOnClickListener(view -> {
            if (title.getText().toString().isEmpty()){
                Toast.makeText(this, "Enter title", Toast.LENGTH_SHORT).show();
            }else if(desc.getText().toString().isEmpty()){
                Toast.makeText(this, "Enter description", Toast.LENGTH_SHORT).show();
            }else {

                ProgressDialog progressdialog = new ProgressDialog(CreateActivity.this);
                progressdialog.setMessage("Please Wait....");
                progressdialog.show();
                progressdialog.setCancelable(false);

                StorageReference storageReference = FirebaseStorage.getInstance().getReference("post/" + ""+System.currentTimeMillis());
                storageReference.putFile(uri).addOnSuccessListener(taskSnapshot -> {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful()) ;
                    Uri downloadUri = uriTask.getResult();
                    if (uriTask.isSuccessful()){

                        String id = ""+System.currentTimeMillis();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("id", Long.valueOf(id));
                        hashMap.put("description", desc.getText().toString());
                        hashMap.put("link", downloadUri.toString());
                        hashMap.put("title", title.getText().toString());
                        hashMap.put("user", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        hashMap.put("username", username);

                        FirebaseDatabase.getInstance().getReference("post").child(id).setValue(hashMap);

                        progressdialog.dismiss();

                        Toast.makeText(this, "Uploaded", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Storage permission allowed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Storage permission is required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE && data != null){
            uri = Objects.requireNonNull(data).getData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}