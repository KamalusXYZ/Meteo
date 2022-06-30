package com.example.meteo;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.meteo.OWM.Observation;


import com.example.meteo.databinding.ActivityMeteoBinding;
import com.example.meteo.databinding.FragmentListBinding;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.ArrayList;

import com.example.meteo.OWM;


public class FragmentList<P extends ListProvider<E>, E> extends Fragment {
    private VMListProvider<P, E> vm;

    public FragmentList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Récupérer l'instance Singleton de VMListProvider.
        vm = new ViewModelProvider(requireActivity()).get(VMListProvider.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Récupérer la classe auto-générée de binding du layout.
        FragmentListBinding binding = FragmentListBinding.inflate(inflater, container, false);
        // Récupérer le ListView depuis le layout via son id.
        ListView listView = binding.list;
        // Créer un ArrayAdapter dédié.
        ArrayAdapter<E> adapter = vm.getProvider().getAdapter(this.getContext());
        // Associer le ListView à l'ArrayAdapter.
        listView.setAdapter(adapter);
        // Observer la liste du provider et peupler l'adapter.
        vm.getLDList().observe(getViewLifecycleOwner(), list -> {
            adapter.clear();
            adapter.addAll(list);
        });
        //Retourne le fragment inflaté.
        return binding.getRoot();
    }
}