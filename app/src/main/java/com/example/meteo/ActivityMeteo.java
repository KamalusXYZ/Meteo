package com.example.meteo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

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
        // Supporter l'ActionBar via une Toolbar.
        this.setSupportActionBar(binding.toolbar);
        // Charger une instance de Fragmentlist dans le FrameLayout.
        this.getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, new FragmentList())
                .commit();
    }
}