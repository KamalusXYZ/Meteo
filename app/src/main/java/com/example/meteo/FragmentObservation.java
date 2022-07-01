package com.example.meteo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.meteo.databinding.FragmentListBinding;
import com.example.meteo.databinding.FragmentObservationBinding;


public class FragmentObservation extends Fragment {
    private VMListProvider<OWM, OWM.Observation> vm;

    public FragmentObservation() {
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
        FragmentObservationBinding binding = FragmentObservationBinding.inflate(inflater, container, false);

        //TODO

        //Retourne le fragment inflaté.
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // Appeler la méthode parente.
        super.onCreateOptionsMenu(menu, inflater);
        // Inflater le layout du menu.
        inflater.inflate(R.menu.menu_list, menu); // TODO

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // TODO
        // Si clique sur refresh, actualiser.
        if (item.getItemId() == R.id.action_refresh) {
            vm.getMldList(true);
            return true;
        }
        // Sinon , laisser le parenttraiter le clique.
        return super.onOptionsItemSelected(item);
    }
}