package com.example.weekly_recipe_planner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

import com.example.weekly_recipe_planner.Helpers.DatabaseHelper;
import com.example.weekly_recipe_planner.Model.Store;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.List;

public class StoreSelectionActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Spinner storeSpinner;
    private Button saveButton;
    private GoogleMap mMap;
    private DatabaseHelper dbHelper;
    private String userId;
    private int itemId;
    private List<Store> stores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_selection);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();
        }

        itemId = getIntent().getIntExtra("item_id", -1);

        dbHelper = new DatabaseHelper(this);

        storeSpinner = findViewById(R.id.storeSpinner);
        saveButton = findViewById(R.id.saveStoreButton);

        // Initialize map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        loadStores();

        saveButton.setOnClickListener(v -> saveStoreSelection());
    }

    private void loadStores() {
        stores = dbHelper.getStores(userId);

        ArrayAdapter<Store> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, stores);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        storeSpinner.setAdapter(adapter);

        storeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mMap != null && position >= 0 && position < stores.size()) {
                    Store store = stores.get(position);
                    LatLng storeLocation = new LatLng(store.getLatitude(), store.getLongitude());
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(storeLocation).title(store.getName()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(storeLocation, 15));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void saveStoreSelection() {
        if (itemId == -1) return;

        int selectedPosition = storeSpinner.getSelectedItemPosition();
        if (selectedPosition >= 0 && selectedPosition < stores.size()) {
            Store selectedStore = stores.get(selectedPosition);
            dbHelper.assignStoreToGroceryItem(itemId, selectedStore.getId(), userId);

            Intent resultIntent = new Intent();
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (!stores.isEmpty()) {
            Store firstStore = stores.get(0);
            LatLng storeLocation = new LatLng(firstStore.getLatitude(), firstStore.getLongitude());
            mMap.addMarker(new MarkerOptions().position(storeLocation).title(firstStore.getName()));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(storeLocation, 15));
        }
    }
}