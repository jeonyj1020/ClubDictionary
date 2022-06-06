package com.example.clubdictionary.ClubPage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.clubdictionary.R;
import com.example.clubdictionary.WritePost.PostInfo;
import com.example.clubdictionary.view.ExpandableHeightGridView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PostFragment extends Fragment {
    String name;
    ArrayList<PostInfo> postList = new ArrayList<>();
    boolean isManager;
    DocumentReference clubRef;
    int postCnt, cmp;
    private ExpandableHeightGridView gridview = null;
    private GridViewAdapter adapter = null;

    public static PostFragment newInstance(int number, String name) {
        PostFragment postFragment = new PostFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("number", number);
        bundle.putString("name", name);
        postFragment.setArguments(bundle);
        return postFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            int num = getArguments().getInt("number");
            name = getArguments().getString("name");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(inflater.getContext()).inflate(R.layout.fragment_clubpage_post, container, false);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        db.collection("clubs").whereEqualTo("name", name).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(QueryDocumentSnapshot now : task.getResult()){
                            clubRef = now.getReference();
                            String Uid = (String) now.get("Uid");
                            if(Uid == user.getUid()) isManager = true;
                            else isManager = false;
                        }
                    }
                }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        clubRef.collection("posts").orderBy("upTime", Query.Direction.DESCENDING)
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        QuerySnapshot querySnapshot = task.getResult();
                                        cmp =0;
                                        postCnt = querySnapshot.size();
                                        for(DocumentSnapshot now : querySnapshot){
                                            cmp++;
                                            postList.add(now.toObject(PostInfo.class));
                                            if(postCnt == cmp){
                                                gridview = (ExpandableHeightGridView) view.findViewById(R.id.post_gridview);
                                                gridview.setExpanded(true);
                                                adapter = new GridViewAdapter(postList, isManager, getContext());
                                                gridview.setAdapter(adapter);
                                            }
                                        }
                                    }
                                });
                    }
                });

        return view;
    }
}

