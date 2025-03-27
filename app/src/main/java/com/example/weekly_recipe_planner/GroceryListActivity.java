package com.example.weekly_recipe_planner;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weekly_recipe_planner.Adapter.GroceryItemAdapter;
import com.example.weekly_recipe_planner.Helpers.DatabaseHelper;
import com.example.weekly_recipe_planner.Model.GroceryItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.List;

public class GroceryListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private GroceryItemAdapter adapter;
    private DatabaseHelper dbHelper;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();
        }

        dbHelper = new DatabaseHelper(this);

        recyclerView = findViewById(R.id.groceryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadGroceryList();
    }

    private void loadGroceryList() {
        List<GroceryItem> items = dbHelper.getGroceryList(userId);
        adapter = new GroceryItemAdapter(items, new GroceryItemAdapter.OnGroceryItemClickListener() {
            @Override
            public void onItemClick(GroceryItem item) {
                // Handle item click (e.g., mark as purchased)
                item.setPurchased(!item.isPurchased());
                dbHelper.updateGroceryItem(item, userId);
                loadGroceryList();
            }

            @Override
            public void onStoreClick(GroceryItem item) {
                // Open store selection
                Intent intent = new Intent(GroceryListActivity.this, StoreSelectionActivity.class);
                intent.putExtra("item_id", item.getId());
                startActivityForResult(intent, 1);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.grocery_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_share_list) {
            shareGroceryList();
            return true;
        } else if (item.getItemId() == R.id.action_clear_purchased) {
            clearPurchasedItems();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareGroceryList() {
        List<GroceryItem> items = dbHelper.getGroceryList(userId);

        StringBuilder shareText = new StringBuilder("My Grocery List:\n\n");
        String currentCategory = "";

        for (GroceryItem item : items) {
            if (!item.getCategory().equals(currentCategory)) {
                currentCategory = item.getCategory();
                shareText.append("\n").append(currentCategory).append(":\n");
            }

            shareText.append("- ").append(item.getName());
            if (item.getQuantity() > 0) {
                shareText.append(" (").append(item.getQuantity());
                if (item.getUnit() != null && !item.getUnit().isEmpty()) {
                    shareText.append(" ").append(item.getUnit());
                }
                shareText.append(")");
            }

            if (item.getStoreName() != null && !item.getStoreName().isEmpty()) {
                shareText.append(" - Buy at ").append(item.getStoreName());
            }

            shareText.append("\n");
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText.toString());
        startActivity(Intent.createChooser(shareIntent, "Share Grocery List"));
    }

    private void clearPurchasedItems() {
        dbHelper.clearPurchasedItems(userId);
        loadGroceryList();
        Toast.makeText(this, "Purchased items cleared", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadGroceryList();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadGroceryList();
    }
}