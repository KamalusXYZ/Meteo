package com.example.meteo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.meteo.databinding.ActivityMeteoBinding;
import com.example.meteo.databinding.FragmentListBinding;


public class FragmentList extends Fragment {

    public FragmentList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Récupérer la classe auto-générée de Binding du layout.
        FragmentListBinding binding = FragmentListBinding.inflate(inflater, container, false);
        //TODO

        // Retourner le fragment inflaté.
        return binding.getRoot();
    }
}