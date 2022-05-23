package com.example.clubdictionary.Category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.clubdictionary.R;

public class ReligionFragment extends Fragment {

    public ReligionFragment() {
    }

    public static ReligionFragment newInstance(){
        return new ReligionFragment();
    }


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_religion, container, false);
        return view;
    }
}