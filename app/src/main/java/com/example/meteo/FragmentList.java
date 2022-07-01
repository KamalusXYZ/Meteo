package com.example.meteo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.example.meteo.databinding.FragmentListBinding;


public class FragmentList<P extends ListProvider<E>, E> extends Fragment {
    private VMListProvider<P, E> vm;

    public FragmentList() {
        // Required empty public constructor
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Récupérer l'instance Singleton de VMListProvider.
        vm = new ViewModelProvider(requireActivity()).get(VMListProvider.class);
        // Indiquer que ce fragment a un menu.
        setHasOptionsMenu(true);

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
        // Définir un écouteur de clique sur les items de la liste.
        listView.setOnItemClickListener((parent, view, position, id)-> vm.setPosition(position));
        // Observer la liste du provider et peupler l'adapter.
        vm.getMldList(false).observe(getViewLifecycleOwner(), list -> {
            adapter.clear();
            adapter.addAll(list);
        });
        //Retourne le fragment inflaté.
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // Appeler la méthode parente.
        super.onCreateOptionsMenu(menu, inflater);
        // Inflater le layout du menu.
        inflater.inflate(R.menu.menu_list, menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Si clique sur refresh, actualiser.
        if (item.getItemId() == R.id.action_refresh) {
            vm.getMldList(true);
            return true;
        }
        // Sinon , laisser le parenttraiter le clique.
        return super.onOptionsItemSelected(item);
    }
}