package com.example.clubdictionary.Category;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clubdictionary.R;

import java.util.ArrayList;
import java.util.List;

public class Category2Fragment extends Fragment {

    Context mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public Category2Fragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category2, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.category_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        List<ExpandableListAdapter.Item> data = new ArrayList<>();

        ExpandableListAdapter.Item social = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "사회 분과");
        social.invisibleChildren = new ArrayList<>();
        social.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "교육 봉사"));
        social.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "벽화 봉사"));
        social.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "청소년 봉사"));
        social.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "봉사"));

        ExpandableListAdapter.Item study = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "학술 분과");
        study.invisibleChildren = new ArrayList<>();
        study.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "IT"));
        study.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "인문"));
        study.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "자연"));
        study.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "진로/창업/발명"));

        ExpandableListAdapter.Item arts = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "문예 분과");
        arts.invisibleChildren = new ArrayList<>();
        arts.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "밴드"));
        arts.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "악기"));
        arts.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "노래"));
        arts.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "연극"));
        arts.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "미술"));
        arts.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "요리"));
        arts.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "댄스"));
        arts.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "사진 / 영상"));
        arts.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "수공예"));

        ExpandableListAdapter.Item sports = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "체육 분과");
        sports.invisibleChildren = new ArrayList<>();
        sports.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "구기"));
        sports.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "라켓 스포츠"));
        sports.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "무술"));
        sports.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "익스트림 스포츠"));
        sports.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "양궁"));
        sports.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "게임"));

        ExpandableListAdapter.Item religion = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "종교 분과");
        religion.invisibleChildren = new ArrayList<>();
        religion.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "기독교"));
        religion.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "불교"));
        religion.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "가톨릭"));
        religion.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "증산도"));

        data.add(social);
        data.add(study);
        data.add(arts);
        data.add(sports);
        data.add(religion);

        recyclerView.setAdapter(new ExpandableListAdapter(data));

        return view;
    }
}
