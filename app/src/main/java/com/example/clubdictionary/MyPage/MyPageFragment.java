package com.example.clubdictionary.MyPage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaMetadata;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.clubdictionary.ClubPage.ClubPageActivity;
import com.example.clubdictionary.R;
import com.example.clubdictionary.UserManagement.LoginActivity;
import com.example.clubdictionary.UserManagement.MemberInfo;
import com.example.clubdictionary.UserManagement.PasswordResetActivity;
import com.example.clubdictionary.WritePost.PostInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyPageFragment extends Fragment {

    TextView passwordResetTextView, applyTextView,logoutTextView,deleteTextView, mypage_profile_name,
            myScrapList, myClubTextView;
    CircleImageView mypage_profile_pic;
    private FirebaseAuth mAuth ;
    DocumentSnapshot document;
    String name = null;
    String type;

    public MyPageFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mypage, container, false);
        mypage_profile_pic = view.findViewById(R.id.mypage_profile_pic);
        applyTextView = view.findViewById(R.id.mypage_apply_club);
        logoutTextView = view.findViewById(R.id.mypage_log_out);
        deleteTextView = view.findViewById(R.id.mypage_withdrawal);
        passwordResetTextView = view.findViewById(R.id.mypage_password_reset);
        mypage_profile_name = view.findViewById(R.id.mypage_profile_name);
        myScrapList = view.findViewById(R.id.mypage_scrap);
        myClubTextView = view.findViewById(R.id.mypage_club_managing);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if(name != null){
            mypage_profile_name.setText(name);
        }

        db.collection("users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    document = task.getResult();
                    if(!document.exists()){
                        db.collection("clubs").document(user.getUid()).get().
                                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    document = task.getResult();
                                    mypage_profile_name.setText(document.get("name").toString());
                                    Log.d("type", "클럽입니다");
                                    type = "clubs";
                                    myClubTextView.setTextColor(getResources().getColor(R.color.black_191919));
                                    myClubTextView.setClickable(true);
                                }
                            }
                        });
                    }
                    else {
                        mypage_profile_name.setText(document.get("name").toString());
                        name = document.get("name").toString();
                        type = "users";
                    }
                }
            }
        });


        passwordResetTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PasswordResetActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //finish();
            }
        });

        applyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(getActivity())
                        .setTitle("알람 팝업")
                        .setMessage("이메일을 보내시겠습니까?")
                        .setPositiveButton("보내기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent email = new Intent(Intent.ACTION_SEND);
                                email.setType("plain/text");
                                String[] address = {"clubdictionary@gmail.com"};
                                email.putExtra(Intent.EXTRA_EMAIL, address);
                                email.putExtra(Intent.EXTRA_SUBJECT, "신비한 동아리 사전 동아리/소모임 신청 메일입니다.");
                                email.putExtra(Intent.EXTRA_TEXT, "동아리 이름:\n\n동아리 활동 요약(날짜, 회비, 활동 장소):\n\n중앙 동아리 여부:\n\n기타 문의 사항:\n\n");
                                startActivity(email);
                            }
                        })
                        .show();

            }
        });

        logoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("알람 팝업")
                        .setMessage("로그 아웃하시겠습니까??")
                        .setPositiveButton("로그 아웃", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                requireActivity().finish();
                            }
                        })
                        .show();

            }
        });

        deleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(getActivity())
                        .setTitle("알람 팝업")
                        .setMessage("삭제하시겠습니까?")
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Objects.requireNonNull(mAuth.getCurrentUser()).delete();
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                requireActivity().finish();
                            }
                        })
                        .show();
            }
        });

        myScrapList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ScrapListActivity.class);
                intent.putExtra("type", type);
                startActivity(intent);
            }
        });

        myClubTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ClubPageActivity.class);
                intent.putExtra("name", document.get("name").toString());
                startActivity(intent);
            }
        });
        return view;
    }
}
