package com.spacester.webook;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Objects;

public class AdapterPost extends RecyclerView.Adapter<AdapterPost.MyHolder>{

    final Context context;
    final List<ModelPost> userList;

    public AdapterPost(Context context, List<ModelPost> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.post_layout, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        holder.name.setText(userList.get(position).getTitle());
        holder.username.setText("By "+userList.get(position).getUsername());
        holder.desc.setText(userList.get(position).getDescription());

        holder.attach.setOnClickListener(view -> {
            StorageReference picRef = FirebaseStorage.getInstance().getReferenceFromUrl(userList.get(position).getLink());
            picRef.getDownloadUrl().addOnSuccessListener(uri -> {
                picRef.getMetadata().addOnSuccessListener(storageMetadata -> {
                    String extension = storageMetadata.getContentType();
                    String url = uri.toString();
                    downloadDoc(context, DIRECTORY_DOWNLOADS, url, extension);
                });
            });
        });

    }

    private void downloadDoc(Context context, String directoryDownloads, String url, String extension) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri1 = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri1);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, directoryDownloads, extension);
        Objects.requireNonNull(downloadManager).enqueue(request);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder{

        TextView name, username, desc;
        Button attach;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            username = itemView.findViewById(R.id.username);
            desc = itemView.findViewById(R.id.desc);
            attach = itemView.findViewById(R.id.attach);

        }

    }
}
