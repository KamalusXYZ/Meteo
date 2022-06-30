package com.example.meteo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.meteo.databinding.ActivityMeteoBinding;

public class ActivityMeteo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Appel de la méthode parente.
        super.onCreate(savedInstanceState);
        // Récupérer la classe auto-générée de binding du layout.
        ActivityMeteoBinding binding = ActivityMeteoBinding.inflate(this.getLayoutInflater());
        // Afficher le layout.
        this.setContentView(binding.getRoot());
        // Supporter l'ActionBar via une Toolbar
        this.setSupportActionBar(binding.toolbar);
        // Récupérer l'instance Singleton de VMListProvider.
        VMListProvider<OWM, OWM.Observation> vm = new ViewModelProvider(this).get(VMListProvider.class);
        // Définir le provider dans le VM.
        vm.setProvider(new OWM());

        // La première fois, charger une instance de FragmentList.
        if (savedInstanceState == null) {
            this
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new FragmentList())
                    .commit();
        }
    }
}