package com.example.weekly_recipe_planner;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weekly_recipe_planner.Adapter.GroceryItemAdapter;
import com.example.weekly_recipe_planner.Helpers.DatabaseHelper;
import com.example.weekly_recipe_planner.Model.GroceryItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.List;

public class GroceryListFragment extends Fragment {

    private RecyclerView recyclerView;
    private GroceryItemAdapter adapter;
    private DatabaseHelper dbHelper;
    private String userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // Enable options menu in fragment

        // Get current user ID
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();
        }

        dbHelper = new DatabaseHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grocery_list, container, false);

        recyclerView = view.findViewById(R.id.groceryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        loadGroceryList();

        return view;
    }

    private void loadGroceryList() {
        List<GroceryItem> groceryItems = dbHelper.getGroceryList(userId);
        adapter = new GroceryItemAdapter(groceryItems, new GroceryItemAdapter.OnGroceryItemClickListener() {
            @Override
            public void onItemClick(GroceryItem item) {
                // Toggle purchased status
                item.setPurchased(!item.isPurchased());
                dbHelper.updateGroceryItem(item, userId);
                loadGroceryList(); // Refresh list
            }

            @Override
            public void onStoreClick(GroceryItem item) {
                // Open store selection
                // Implement this if you have store selection functionality
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.grocery_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_share_list) {
            shareGroceryList();
            return true;
        } else if (id == R.id.action_clear_purchased) {
            clearPurchasedItems();
            return true;
        } else if (id == R.id.action_generate_plan) {
            generateFromMealPlan();
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
            shareText.append("\n");
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText.toString());
        startActivity(Intent.createChooser(shareIntent, "Share Grocery List"));
    }

    private void clearPurchasedItems() {
        int count = dbHelper.clearPurchasedItems(userId);
        if (count > 0) {
            Toast.makeText(getActivity(), "Cleared " + count + " purchased items", Toast.LENGTH_SHORT).show();
            loadGroceryList();
        } else {
            Toast.makeText(getActivity(), "No purchased items to clear", Toast.LENGTH_SHORT).show();
        }
    }

    private void generateFromMealPlan() {
        // Get current week's start and end dates
        String startDate = getStartOfWeekDate();
        String endDate = getEndOfWeekDate();

        dbHelper.generateGroceryListFromMealPlan(userId, startDate, endDate);
        loadGroceryList();
        Toast.makeText(getActivity(), "Grocery list generated from meal plan", Toast.LENGTH_SHORT).show();
    }

    private String getStartOfWeekDate() {
        // Implement logic to get start of week date (e.g., "2023-11-20")
        return ""; // Return formatted date string
    }

    private String getEndOfWeekDate() {
        // Implement logic to get end of week date
        return ""; // Return formatted date string
    }

    @Override
    public void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}