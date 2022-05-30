package com.example.clubdictionary.Home;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clubdictionary.FilterActivity;
import com.example.clubdictionary.MainActivity;
import com.example.clubdictionary.R;
import com.example.clubdictionary.WritePost.WriteContentsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    HomeRecyclerViewAdapter homeRecyclerViewAdapter;
    Context mContext;

    // 필터링 관련 변수들
    private FirebaseUser user;
    DocumentReference docRef = null;
    ArrayList<String> filtering = new ArrayList<>();
    String filteringBinary = null;
    ArrayList<Boolean> checked = new ArrayList<>();
    CheckBox onlyFavorite, onlyRecruit;
    Map<String, List<String>> bookMark = null;
    FloatingActionButton writePostButton;
    ArrayList<String> allMinor = new ArrayList<String>(Arrays.asList(
                    "idea",
                    "it", "liberalArts", "nature", "idea",
                    "band", "instrument", "song", "play", "art", "cook", "dance", "picture", "handicraft",
                    "ball", "racket", "martialArts", "extreme", "archery", "game",
                    "christian", "buddhism", "catholic", "transpiration"
    ));

    public HomeFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mContext = container.getContext();

        view.findViewById(R.id.filter).setOnClickListener(onClickListener);

        onlyFavorite = view.findViewById(R.id.onlyFavorite);
        onlyFavorite.setOnClickListener(onClickListener);

        onlyRecruit = view.findViewById(R.id.onlyRecruit);
        onlyRecruit.setOnClickListener(onClickListener);

        writePostButton = view.findViewById(R.id.writePostButton);
        writePostButton.setOnClickListener(onClickListener);

        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (!documentSnapshot.exists()) {
                        Log.e("###", "클럽입니다");
                        docRef = db.collection("clubs").document(user.getUid());
                        writePostButton.setVisibility(View.VISIBLE);
                        getFiltering();
                    } else {
                        Log.e("###", "유저입니다");
                        //writePostButton.setVisibility(View.VISIBLE);
                        getFiltering();
                    }
                }
            }
        });

        //ArrayList<HomeItem> testDataSet = new ArrayList<>();

        //테스트 용
        ArrayList<String> testDataSet = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            testDataSet.add(i + "번째 아이템");
        }

        recyclerView = view.findViewById(R.id.fragmrnt_home_recyclerview);
        homeRecyclerViewAdapter = new HomeRecyclerViewAdapter(testDataSet, mContext);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(homeRecyclerViewAdapter);

        return view;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.filter:
                    Intent intent = new Intent(getActivity(), FilterActivity.class);
                    intent.putExtra("filteringBinary", filteringBinary);
                    startActivityForResult(intent, 1);
                    break;

                case R.id.onlyFavorite:
                case R.id.onlyRecruit:
                    checked.set(0, onlyRecruit.isChecked());
                    checked.set(1, onlyFavorite.isChecked());

                    if (onlyFavorite.isChecked())

                        docRef.update("checked", checked)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });

                    query();
                    break;

                case R.id.writePostButton:
                    intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent, 2);
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case 1:
                    filtering = data.getStringArrayListExtra("newFiltering");
                    filteringBinary = data.getStringExtra("newFilteringBinary");
                    query();
                    break;
                case 2:
                    if (resultCode == Activity.RESULT_OK) {
                        ArrayList<Uri> imageList = new ArrayList<>();

                        if (data.getClipData() == null) {
                            imageList.add(Uri.parse(data.getData().getPath()));
                        } else {
                            ClipData clipData = data.getClipData();
                            if (clipData.getItemCount() > 10) {
                                Toast.makeText(getActivity(), "사진은 10개까지 선택가능 합니다.", Toast.LENGTH_SHORT).show();
                                return;
                            } else if (clipData.getItemCount() == 1) {
                                imageList.add(clipData.getItemAt(0).getUri());
                            } else if (clipData.getItemCount() > 1 && clipData.getItemCount() < 10) {
                                for (int i = 0; i < clipData.getItemCount(); i++) {
                                    imageList.add((clipData.getItemAt(i).getUri()));
                                }
                            }
                        }

                        Intent intent = new Intent(getActivity(), WriteContentsActivity.class);
                        intent.putExtra("imageList", imageList);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(), "사진 선택을 취소하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

    private void getFiltering() {
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    filtering = (ArrayList<String>) documentSnapshot.get("filtering");
                    filteringBinary = (String) documentSnapshot.get("filteringBinary");
                    checked = (ArrayList<Boolean>) documentSnapshot.get("checked");
                    bookMark = (Map<String, List<String>>) documentSnapshot.get("bookMark");
                            /*Map<String, List<String>> bookMark = new HashMap<>();
                            bookMark = (Map<String, List<String>>) documentSnapshot.get("bookMark");
                            List<String> newBookMark = new ArrayList<>();

                            if(bookMark.containsKey("ball")){
                                newBookMark = bookMark.get("ball");
                            }
                            newBookMark.add("스트라이크");
                            bookMark.put("ball", newBookMark);

                            docRef.update("bookMark", bookMark);*/
                    if (checked.get(0)) onlyRecruit.setChecked(true);
                    else onlyRecruit.setChecked(false);
                    if (checked.get(1)) onlyFavorite.setChecked(true);
                    else onlyFavorite.setChecked(false);

                    query();
                }
            }
        });
    }

    private void query() {
        boolean onlyRecruitChecked = onlyRecruit.isChecked();
        boolean onlyFavoriteChecked = onlyFavorite.isChecked();
        Log.d("filter", " \n" + filtering + "\n" + filteringBinary + "\n" + onlyRecruitChecked +
                ", " + onlyFavoriteChecked + "\n" + bookMark);

        ArrayList<String> intersection = new ArrayList<>();
        if(onlyFavoriteChecked){
            for(String minor : bookMark.keySet()){
                if(filtering.contains(minor)){
                    List<String> clubs = bookMark.get(minor);
                    for(int i = 0; i<clubs.size(); i++){
                        intersection.add(clubs.get(i));
                    }
                }
            }
        }

        if (filtering.size() == 0) {
            // 맨 처음 회원가입한 후의 상태 - 이 때는 모든 동아리의 문서를 쿼리
            if(filteringBinary == null){
                if(onlyFavoriteChecked){

                }
                else{

                }
            }
            // 유저가 펄터링에서 0개의 소분류를 선택했을 때
            else {

            }
        } else {
            if (filtering.size() <= allMinor.size()/2){

            }
            else if(filtering.size() == allMinor.size()){

            }
            else{

            }
        }
        /*if(onlyFavoriteChecked){
            for(){

            }
        }
        else{
            intersection = filtering;
        }*/
        /*ArrayList<Task> tasks = null;
        for(int i = 0; i <= filtering.size()/10; i++) {
            List<String> now = null;
            if(filtering.size() >= 10 * i) {
                now = filtering.subList(10 * i, 10 * i + 9);
            }
            else{
                now = filtering.subList(10 * i, filtering.size() - 1);
            }
            Query query = clubsRef.whereIn("minor", now);
            tasks.set(i, query.get());
        }*/
    }
}
