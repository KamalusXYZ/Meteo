package com.example.meteo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.meteo.databinding.ActivityMeteoBinding;
import com.google.android.material.snackbar.Snackbar;

public class ActivityMeteo extends AppCompatActivity {
    @SuppressWarnings("unchecked")
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
        // Préparer la snackBar du chargement.
        Snackbar loadingBar = Snackbar.make(binding.container, R.string.info_loading, Snackbar.LENGTH_INDEFINITE);
        // Observer l'état du ViewModel pour réagir aux changements.
        vm.getMldState().observe(this, state -> {
            switch (state) {
                case VMListProvider.STATE_CLICK_ON_ITEM:
                    // Remplacer le fragmentlist par le FragmentObservation en ajoutant la transaction à l'historique.
                    // Remplacer le fragmentlist par le FragmentObservation en ajoutant la transaction à l'historique.
                    this
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, new FragmentObservation())
                            .addToBackStack(null)
                            .commit();
                    break;

                case VMListProvider.STATE_LOADING_STARTS:
                    // Afficher le Snackbar de chargement
                    loadingBar.show();
                    break;
                case VMListProvider.STATE_NO_INTERNET:
                    // Afficher un snackbar d'absence d'internet.
                    Snackbar.make(binding.container, R.string.info_no_internet, Snackbar.LENGTH_LONG).show();
                    break;
                case VMListProvider.STATE_LOADING_ENDS:
                    // Masquer le Snackbar de chargement.
                    loadingBar.dismiss();
                    break;


                case VMListProvider.STATE_DONE:
                    // Ne rien faire, présent pour éviter une boucle sans fin.
                    return;
            }
            vm.setStateDone();
        });
        // Observer les changements de position pour réagir aux cliques sur les itemsde la liste.


        // La première fois, charger une instance de FragmentList dans le frameLayout.
        if (savedInstanceState == null) {
            this
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new FragmentList<OWM, OWM.Observation>())
                    .commit();
        }
    }
}