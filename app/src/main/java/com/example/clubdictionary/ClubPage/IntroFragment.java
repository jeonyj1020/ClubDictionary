package com.example.clubdictionary.ClubPage;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.clubdictionary.ClubInfo;
import com.example.clubdictionary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class IntroFragment extends Fragment {
    private String TAG = "IntroFragment";
    String name;
    public static IntroFragment newInstance(int number, String name) {
        IntroFragment introFragment = new IntroFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("number", number);
        bundle.putString("name", name);
        introFragment.setArguments(bundle);
        return introFragment;
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

        View view = LayoutInflater.from(inflater.getContext()).inflate(R.layout.fragment_clubpage_intro, container, false);
        TextView introduce = view.findViewById(R.id.introduce);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String str;
        db.collection("clubs").whereEqualTo("name", name).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        //여기서부터 해야함
                        ClubInfo clubInfo = document.toObject(ClubInfo.class);
                        introduce.setText(clubInfo.getIntroduce());
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        return view;
    }


}

