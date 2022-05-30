package com.example.clubdictionary.WritePost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clubdictionary.MainActivity;
import com.example.clubdictionary.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.UUID;

public class WriteContentsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Uri> imageList = new ArrayList<Uri>();
    ImageListAdapter imageListAdapter;
    TextView hashTag, contents;
    Button exit, upload;
    int idx = 0;

    DocumentSnapshot clubDoc = null;
    DocumentReference clubRef = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_contents);
        imageList = (ArrayList<Uri>) getIntent().getSerializableExtra("imageList");

        recyclerView = findViewById(R.id.imageListRecyclerView);
        imageListAdapter = new ImageListAdapter(imageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(imageListAdapter);

        exit = findViewById(R.id.exit);
        exit.setOnClickListener(onClickListener);
        upload = findViewById(R.id.upload);
        upload.setOnClickListener(onClickListener);

        hashTag = findViewById(R.id.hashTag);
        contents = findViewById(R.id.contents);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.upload:
                    uploadPost();
                    break;

                case R.id.exit:
                    finish();
                    break;
            }
        }
    };

    private void uploadPost() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String upTime = String.valueOf(System.currentTimeMillis());
        String userUid = user.getUid();
        ArrayList<String> imageUrlList = new ArrayList<>();

        clubRef = db.collection("clubs").document(userUid);
        db.collection("clubs").document(user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            clubDoc = task.getResult();
                            FirebaseStorage storage = FirebaseStorage.getInstance();
                            idx = 0;
                            for (Uri image : imageList) {

                                StorageReference postRef = storage.getReference().child("clubs/" + userUid
                                        + "/" + upTime + "/" + idx + ".jpg");
                                UploadTask uploadTask = postRef.putFile(image);

                                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            //imageUrlList.add(postRef.getDownloadUrl().);
                                            idx++;
                                            if (idx == imageList.size()) {
                                                PostInfo postInfo = new PostInfo((String) clubDoc.get("name"), (String) clubDoc.get("icon"),
                                                        (String) clubDoc.get("major"), (String) clubDoc.get("minor"),
                                                        upTime, hashTag.getText().toString(), contents.getText().toString(), imageUrlList);
                                                clubRef.collection("posts").document(upTime).set(postInfo)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(WriteContentsActivity.this, "업로드에 성공했습니다", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    Toast.makeText(WriteContentsActivity.this, "업로드에 실패했습니다", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                            }
                                        } else {
                                            Toast.makeText(WriteContentsActivity.this, "업로드에 실패했습니다", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
    }
}