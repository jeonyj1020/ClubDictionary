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

import com.example.clubdictionary.R;
import com.example.clubdictionary.WritePost.PostInfo;
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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    HomeRecyclerViewAdapter homeRecyclerViewAdapter;
    Context mContext;

    // 필터링 관련 변수들
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DocumentReference docRef = null;
    ArrayList<String> filtering = new ArrayList<>();
    String filteringBinary = null;
    ArrayList<Boolean> checked = new ArrayList<>();
    CheckBox onlyFavorite, onlyRecruit;
    Map<String, List<String>> bookMark = null;
    FloatingActionButton writePostButton;
    ArrayList<String> allMinor = new ArrayList<String>(Arrays.asList(
            "volunteer",
            "liberalArts", "idea", "it", "nature",
            "band", "instrument", "song", "art", "picture", "play", "dance", "handicraft", "cook",
            "ball", "racket", "martialArts", "extreme", "archery", "game",
            "christian", "buddhism", "catholic", "transpiration"
    ));
    ArrayList<PostInfo> postList = new ArrayList<>();
    ArrayList<String> intersectionClubs = new ArrayList<>();
    ArrayList<String> exceptFiltering = new ArrayList<>();
    ArrayList<String> scrapList = new ArrayList<>();
    int clubCnt = 0, cmp = 0, postCnt = 0;

    public HomeFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        mContext = container.getContext();

        view.findViewById(R.id.filter).setOnClickListener(onClickListener);

        onlyFavorite = view.findViewById(R.id.onlyFavorite);
        onlyFavorite.setOnClickListener(onClickListener);

        onlyRecruit = view.findViewById(R.id.onlyRecruit);
        onlyRecruit.setOnClickListener(onClickListener);

        writePostButton = view.findViewById(R.id.writePostButton);
        writePostButton.setOnClickListener(onClickListener);

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
                        getData();
                    } else {
                        Log.e("###", "유저입니다");
                        //writePostButton.setVisibility(View.VISIBLE);
                        getData();
                    }
                }
            }
        });

        //ArrayList<HomeItem> testDataSet = new ArrayList<>();

        //테스트 용
        /*ArrayList<String> testDataSet = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            testDataSet.add(i + "번째 아이템");
        }*/



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
                            } else {
                                for (int i = 0; i < clipData.getItemCount(); i++) {
                                    imageList.add((clipData.getItemAt(i).getUri()));
                                }
                            }
                        }

                        for(Uri now : imageList){
                            Log.e("uri : ", now.toString());
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

    private void getData() {
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    filtering = (ArrayList<String>) documentSnapshot.get("filtering");
                    filteringBinary = (String) documentSnapshot.get("filteringBinary");
                    checked = (ArrayList<Boolean>) documentSnapshot.get("checked");
                    bookMark = (Map<String, List<String>>) documentSnapshot.get("bookMark");
                    scrapList = (ArrayList<String>) documentSnapshot.get("scrap");
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
        Log.e("filter", " \n" + filtering + "\n" + filteringBinary + "\n" + onlyRecruitChecked +
                ", " + onlyFavoriteChecked + "\n" + bookMark);

        postList.clear();
        intersectionClubs.clear();
        exceptFiltering.clear();

        String is = "";
        if (onlyFavoriteChecked) {
            for (String minor : bookMark.keySet()) {
                if (filtering.contains(minor)) {
                    List<String> clubs = bookMark.get(minor);
                    for (int i = 0; i < clubs.size(); i++) {
                        intersectionClubs.add(clubs.get(i));
                        is += clubs.get(i);
                    }
                }
            }
        }

        String exceptFilteringTest = "";
        for (String minor : allMinor) {
            if (!filtering.contains(minor)) {
                exceptFiltering.add(minor);
                exceptFilteringTest += minor + " ";
            }
        }

        Log.e("intersection", is);

        // 찜한 동아리만 보기가 선택되면 intersectionClubs의 동아리 이름들로 쿼리
        if (onlyFavoriteChecked) {
            if(filtering.size() > 0) {
                if (intersectionClubs.size() == 0) {
                   // Toast.makeText(getContext(), "필터링과 찜한 동아리 둘 다에 해당하는 동아리가 한 개도 없습니다!", Toast.LENGTH_SHORT).show();
                } else {
                    int ten = intersectionClubs.size() / 10;
                    if (intersectionClubs.size() % 10 == 0) ten--;

                    if (onlyRecruitChecked) {
                        for (int i = 0; i <= ten; i++) {
                            if (i != ten) {
                                List<String> clubNames = intersectionClubs.subList(i * 10, (i + 1) * 10);
                                queryByClubNameAndOnlyRecruit(clubNames, false);
                            } else {
                                List<String> clubNames = intersectionClubs.subList(i * 10, intersectionClubs.size());
                                queryByClubNameAndOnlyRecruit(clubNames, true);
                            }
                        }
                    } else {
                        for (int i = 0; i <= ten; i++) {
                            if (i != ten) {
                                List<String> clubNames = intersectionClubs.subList(i * 10, (i + 1) * 10);
                                queryByClubName(clubNames, false);
                            } else {
                                List<String> clubNames = intersectionClubs.subList(i * 10, intersectionClubs.size());
                                queryByClubName(clubNames, true);
                            }
                        }
                    }
                }
            }
            else{
                Toast.makeText(getContext(), "소분류를 한 개도 선택하지 않아 게시물을 불러오지 않습니다!", Toast.LENGTH_SHORT).show();
            }
        }
        // 찜한 동아리만 보기가 선택되지 않았으면, filtering의 소분류들로만 쿼리
        else {
            if (filtering == null || filtering.size() == allMinor.size()) {
                if (onlyRecruitChecked) {
                    db.collectionGroup("posts")
                            .whereEqualTo("recruit", true).get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    QuerySnapshot queryDocumentSnapshots = task.getResult();
                                    postCnt = 0;
                                    for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                        postCnt++;
                                    }

                                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                        postList.add(documentSnapshot.toObject(PostInfo.class));
                                    }

                                    if(postList.size() == postCnt){
                                        sortPostList();
                                    }
                                }
                            });
                } else {
                    db.collectionGroup("posts").get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    QuerySnapshot queryDocumentSnapshots = task.getResult();
                                    postCnt = 0;
                                    for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                        postCnt++;
                                    }

                                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                        postList.add(documentSnapshot.toObject(PostInfo.class));
                                    }
                                    if(postList.size() == postCnt){
                                        sortPostList();
                                    }
                                }
                            });
                }
            } else if (filtering.size() == 0) {
                Toast.makeText(getContext(), "소분류를 한 개도 선택하지 않아 게시물을 불러오지 않습니다!", Toast.LENGTH_SHORT).show();
            } else if (filtering.size() <= allMinor.size() / 2) {
                int ten = filtering.size() / 10;
                if(filtering.size() % 10 == 0) ten--;

                if (onlyRecruitChecked) {
                    for (int i = 0; i <= ten; i++) {
                        if (i != ten) {
                            List<String> minors = filtering.subList(i * 10, (i + 1) * 10);
                            queryByMinorAndOnlyRecruit(minors, false);
                        } else {
                            List<String> minors = filtering.subList(i * 10, filtering.size());
                            queryByMinorAndOnlyRecruit(minors,true);
                        }
                    }
                } else {
                    for (int i = 0; i <= ten; i++) {
                        if (i != ten) {
                            List<String> minors = filtering.subList(i * 10, (i + 1) * 10);
                            queryByMinor(minors, false);
                        } else {
                            List<String> minors = filtering.subList(i * 10, filtering.size());
                            queryByMinor(minors, true);
                        }
                    }
                }
            } else if (filtering.size() < allMinor.size()) {
                int ten = exceptFiltering.size() / 10;
                if(exceptFiltering.size() % 10 == 0) ten--;

                if (onlyRecruitChecked) {
                    for (int i = 0; i <= ten; i++) {
                        if (i != ten) {
                            List<String> minors = exceptFiltering.subList(i * 10, (i + 1) * 10);
                            queryByMinorNotInAndOnlyRecruit(minors, false);
                        } else {
                            List<String> minors = exceptFiltering.subList(i * 10, exceptFiltering.size());
                            queryByMinorNotInAndOnlyRecruit(minors, true);
                        }
                    }
                } else {
                    for (int i = 0; i <= ten; i++) {
                        if (i != ten) {
                            List<String> minors = exceptFiltering.subList(i * 10, (i + 1) * 10);
                            queryByMinorNotIn(minors, false);
                        } else {
                            List<String> minors = exceptFiltering.subList(i * 10, exceptFiltering.size());
                            queryByMinorNotIn(minors, true);
                        }
                    }
                }
            }
        }
    }

    public void queryByClubName(List<String> clubNames, boolean last) {
        db.collection("clubs").whereIn("name", clubNames).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot queryDocumentSnapshots = task.getResult();
                        clubCnt = 0;
                        cmp = 0;
                        for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            clubCnt++;
                        }

                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            documentSnapshot.getReference().collection("posts").get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            cmp++;
                                            for (DocumentSnapshot document : task.getResult()) {
                                                postList.add(document.toObject(PostInfo.class));
                                            }
                                            if(last && cmp == clubCnt){
                                                sortPostList();
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    public void queryByClubNameAndOnlyRecruit(List<String> clubNames, boolean last) {
        db.collection("clubs").whereIn("name", clubNames).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot queryDocumentSnapshots = task.getResult();
                        clubCnt = 0;
                        cmp = 0;
                        for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            clubCnt++;
                        }

                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            documentSnapshot.getReference().collection("posts")
                                    .whereEqualTo("recruit", true).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            cmp++;
                                            for (DocumentSnapshot document : task.getResult()) {
                                                postList.add(document.toObject(PostInfo.class));
                                            }
                                            if(last && cmp == clubCnt){
                                                sortPostList();
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    public void queryByMinor(List<String> Minors, boolean last) {
        db.collection("clubs").whereIn("minor", Minors).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) { clubCnt = 0;
                        QuerySnapshot queryDocumentSnapshots = task.getResult();
                        clubCnt = 0;
                        cmp = 0;
                        for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            clubCnt++;
                        }

                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            documentSnapshot.getReference().collection("posts").get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            cmp++;
                                            for (DocumentSnapshot document : task.getResult()) {
                                                postList.add(document.toObject(PostInfo.class));
                                            }
                                            if(last && cmp == clubCnt){
                                                sortPostList();
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    public void queryByMinorAndOnlyRecruit(List<String> Minors, boolean last) {
        db.collection("clubs").whereIn("minor", Minors).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot queryDocumentSnapshots = task.getResult();
                        clubCnt = 0;
                        cmp = 0;
                        for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            clubCnt++;
                        }

                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            documentSnapshot.getReference().collection("posts")
                                    .whereEqualTo("recruit", true).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            cmp++;
                                            for (DocumentSnapshot document : task.getResult()) {
                                                postList.add(document.toObject(PostInfo.class));
                                            }
                                            if(last && cmp == clubCnt){
                                                sortPostList();
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    public void queryByMinorNotIn(List<String> Minors, boolean last) {
        db.collection("clubs").whereNotIn("minor", Minors).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot queryDocumentSnapshots = task.getResult();
                        clubCnt = 0;
                        cmp = 0;
                        for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            clubCnt++;
                        }

                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            documentSnapshot.getReference().collection("posts").get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            cmp++;
                                            for (DocumentSnapshot document : task.getResult()) {
                                                postList.add(document.toObject(PostInfo.class));
                                            }
                                            if(last && cmp == clubCnt){
                                                sortPostList();
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    public void queryByMinorNotInAndOnlyRecruit(List<String> Minors, boolean last) {
        db.collection("clubs").whereNotIn("minor", Minors).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot queryDocumentSnapshots = task.getResult();
                        clubCnt = 0;
                        cmp = 0;
                        for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            clubCnt++;
                        }

                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            documentSnapshot.getReference().collection("posts")
                                    .whereEqualTo("recruit", true).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            cmp++;
                                            for (DocumentSnapshot document : task.getResult()) {
                                                postList.add(document.toObject(PostInfo.class));
                                            }
                                            if(last && cmp == clubCnt){
                                                sortPostList();
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    public void sortPostList(){
        Collections.sort(postList);
        for (PostInfo postInfo : postList) {
            Log.e("post", "" + postInfo.getUpTime() + ", "+ postInfo.getName() + ", " + postInfo.getIconUrl()
                    + ", " + postInfo.getMajor() + ", " + postInfo.getMinor());
        }
        recyclerView = view.findViewById(R.id.fragmrnt_home_recyclerview);
        homeRecyclerViewAdapter = new HomeRecyclerViewAdapter(postList, mContext, docRef, scrapList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(homeRecyclerViewAdapter);
    }
}
