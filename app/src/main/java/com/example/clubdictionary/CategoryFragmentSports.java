package com.example.clubdictionary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CategoryFragmentSports extends Fragment {

    public CategoryFragmentSports() {
    }

    public static CategoryFragmentSports newInstance(){
        return new CategoryFragmentSports();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_sports, container, false);
        return view;
    }
}
