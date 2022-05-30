package com.example.clubdictionary.Group;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.example.clubdictionary.GroupPostInfo;
import com.example.clubdictionary.R;
import com.example.clubdictionary.UserManagement.MemberInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GroupWriteActivity extends AppCompatActivity {
    private static final String TAG = "GroupWriteActivity";
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    long lnow = System.currentTimeMillis();
    String now = String.valueOf(lnow);
    Toolbar toolbar;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_write);
        EditText title = findViewById(R.id.title);
        EditText content = findViewById(R.id.content);
        Button applyButton = findViewById(R.id.applyButton);
        TextView toKakao = findViewById(R.id.toKakao);
        toolbar = findViewById(R.id.gw_tool_bar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





        toKakao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onKakao(view);
            }
        });
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileUpdate();
            }
        });
    }

    private void profileUpdate() {
        EditText title = findViewById(R.id.title);
        EditText content = findViewById(R.id.content);
        EditText kakaoLink = findViewById(R.id.kakaoLink);
        String titleStr = title.getText().toString();
        String contentStr = content.getText().toString();
        String kakaoLinkStr = kakaoLink.getText().toString();

        Date date = new Date(lnow);
        SimpleDateFormat sdf = new SimpleDateFormat("MM / dd HH : mm");
        String getTime = sdf.format(date);
        if(isKakaoLink(kakaoLinkStr)){
            if (title.length() > 0 && content.length() > 0) {

                GroupPostInfo groupPostInfo = new GroupPostInfo(titleStr, contentStr, kakaoLinkStr, user.getUid(), getTime, now);
                uploader(groupPostInfo);
                Log.e("!!!", "작성됨");
                finish();
            } else {
                Log.e("!!!", "제목 내용 다 적어라");
                toast("제목과 내용을 모두 입력해주세요.\n카카오톡링크는 필수입니다.");
            }
        }
        else{
            Log.e("!!!", "링크 제대로");
            toast("옵바른 오픈 카카오톡 링크를 입력해주세요.");
        }

    }

    private boolean isKakaoLink(String str){
        if(str.length()<=23)return false;

        return str.substring(0, 22).equals("https://open.kakao.com");
    }
    private void toast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    private void uploader(GroupPostInfo groupPostInfo) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("group").document(now).set(groupPostInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                toast("게시글을 등록하였습니다.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                toast("게시글을 등록하지 못했습니다.");
            }
        });
    }

    public boolean getPackageList() {
        boolean isExist = false;

        PackageManager pkgMgr = getPackageManager();
        List<ResolveInfo> mApps;
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mApps = pkgMgr.queryIntentActivities(mainIntent, 0);

        try {
            for (int i=0; i<mApps.size(); i++){
                if(mApps.get(i).activityInfo.packageName.startsWith("com.kakao.talk")){
                    isExist = true;
                    break;
                }
            }
        }
        catch (Exception e){
            isExist = false;
        }
        return isExist;
    }

    public void onKakao(View v) {
        if(getPackageList()) {
            Intent intent = getPackageManager().getLaunchIntentForPackage("com.kakao.talk");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else{
            String url = "market://details?id="+"com.kakao.talk";
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(i);
        }
    }
}