package com.example.clubdictionary.Category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.clubdictionary.R;

public class ArtsFragment extends Fragment {

    public ArtsFragment() {
    }

    public static ArtsFragment newInstance(){
        return new ArtsFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_arts, container, false);
        return view;
    }
}