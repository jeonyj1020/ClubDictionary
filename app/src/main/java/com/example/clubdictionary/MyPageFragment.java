package com.example.clubdictionary;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.clubdictionary.UserManagement.LoginActivity;
import com.example.clubdictionary.UserManagement.PasswordResetActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class MyPageFragment extends Fragment {

    TextView passwordResetTextView, applyTextView,logoutTextView,deleteTextView;
    private FirebaseAuth mAuth ;

    public MyPageFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mypage, container, false);
        //LinearLayout mypage_user_profile = view.findViewById(R.id.mypage_user_profile);
        //mypage_user_profile.bringToFront();
        applyTextView = view.findViewById(R.id.mypage_apply_club);
        logoutTextView = view.findViewById(R.id.mypage_log_out);
        deleteTextView = view.findViewById(R.id.mypage_withdrawal);
        passwordResetTextView = view.findViewById(R.id.mypage_password_reset);

        mAuth = FirebaseAuth.getInstance();
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
                Intent email = new Intent(Intent.ACTION_SEND);
                email.setType("plain/text");
                String[] address = {"clubdictionary@gmail.com"};
                email.putExtra(Intent.EXTRA_EMAIL, address);
                email.putExtra(Intent.EXTRA_SUBJECT, "신비한 동아리 사전 동아리/소모임 신청 메일입니다.");
                email.putExtra(Intent.EXTRA_TEXT, "동아리 이름:\n\n동아리 활동 요약(날짜, 회비, 활동 장소):\n\n중앙 동아리 여부:\n\n기타 문의 사항:\n\n");
                startActivity(email);
            }
        });

        logoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                requireActivity().finish();
            }
        });

        deleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(mAuth.getCurrentUser()).delete();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                requireActivity().finish();
            }
        });

        return view;
    }
}
