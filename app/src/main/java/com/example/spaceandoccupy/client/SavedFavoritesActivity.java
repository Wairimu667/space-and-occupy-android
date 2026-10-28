package com.example.spaceandoccupy.client;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.spaceandoccupy.adapter.PropertyAdapter;
import com.example.spaceandoccupy.databinding.ActivitySavedFavoritesBinding;
import com.example.spaceandoccupy.model.Property;
import com.example.spaceandoccupy.repository.PropertyRepository;
import com.example.spaceandoccupy.repository.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class SavedFavoritesActivity extends AppCompatActivity implements PropertyAdapter.OnPropertyClickListener {

    private ActivitySavedFavoritesBinding binding;
    private PropertyRepository repository;
    private SessionManager sessionManager;
    private PropertyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        binding = ActivitySavedFavoritesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.rootLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        repository = PropertyRepository.getInstance(this);
        sessionManager = repository.getSessionManager();

        binding.toolbar.setNavigationOnClickListener(v -> finish());

        adapter = new PropertyAdapter(this, null, this);
        binding.rvFavorites.setLayoutManager(new LinearLayoutManager(this));
        binding.rvFavorites.setAdapter(adapter);

        loadFavorites();
    }

    private void loadFavorites() {
        repository.getProperties(new PropertyRepository.RepositoryCallback<List<Property>>() {
            @Override
            public void onSuccess(List<Property> result) {
                List<Property> favorites = new ArrayList<>();
                if (result != null) {
                    for (Property p : result) {
                        if (p.isFavorite()) {
                            favorites.add(p);
                        }
                    }
                }

                if (favorites.isEmpty()) {
                    binding.rvFavorites.setVisibility(View.GONE);
                    binding.layoutEmpty.setVisibility(View.VISIBLE);
                } else {
                    binding.rvFavorites.setVisibility(View.VISIBLE);
                    binding.layoutEmpty.setVisibility(View.GONE);
                    adapter.updateData(favorites);
                }
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(SavedFavoritesActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPropertyClick(Property property) {
        Intent intent = new Intent(SavedFavoritesActivity.this, PropertyDetailActivity.class);
        intent.putExtra("property", property);
        startActivity(intent);
    }

    @Override
    public void onFavoriteClick(Property property, boolean isFavorite) {
        sessionManager.toggleFavorite(property.getId());
        Toast.makeText(this, isFavorite ? "Saved to Favorites" : "Removed from Favorites", Toast.LENGTH_SHORT).show();
        loadFavorites();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFavorites();
    }
}
