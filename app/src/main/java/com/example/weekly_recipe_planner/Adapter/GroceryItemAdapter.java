package com.example.weekly_recipe_planner.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weekly_recipe_planner.Model.GroceryItem;
import com.example.weekly_recipe_planner.R;

import java.util.List;

public class GroceryItemAdapter extends RecyclerView.Adapter<GroceryItemAdapter.GroceryItemViewHolder> {
    private List<GroceryItem> items;
    private OnGroceryItemClickListener listener;

    public interface OnGroceryItemClickListener {
        void onItemClick(GroceryItem item);
        void onStoreClick(GroceryItem item);
    }

    public GroceryItemAdapter(List<GroceryItem> items, OnGroceryItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GroceryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grocery, parent, false);
        return new GroceryItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryItemViewHolder holder, int position) {
        GroceryItem item = items.get(position);

        holder.nameTextView.setText(item.getName());
        holder.checkBox.setChecked(item.isPurchased());

        // Display quantity and unit if available
        if (item.getQuantity() > 0) {
            String quantityText = String.valueOf(item.getQuantity());
            if (item.getUnit() != null && !item.getUnit().isEmpty()) {
                quantityText += " " + item.getUnit();
            }
            holder.quantityTextView.setText(quantityText);
            holder.quantityTextView.setVisibility(View.VISIBLE);
        } else {
            holder.quantityTextView.setVisibility(View.GONE);
        }

        // Display store if assigned
        if (item.getStoreName() != null && !item.getStoreName().isEmpty()) {
            holder.storeTextView.setText(item.getStoreName());
            holder.storeTextView.setVisibility(View.VISIBLE);
        } else {
            holder.storeTextView.setVisibility(View.GONE);
        }

        holder.checkBox.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });

        holder.storeTextView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onStoreClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class GroceryItemViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView nameTextView, quantityTextView, storeTextView;

        public GroceryItemViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.groceryCheckBox);
            nameTextView = itemView.findViewById(R.id.groceryItemName);
            quantityTextView = itemView.findViewById(R.id.groceryQuantity);
            storeTextView = itemView.findViewById(R.id.groceryStore);
        }
    }
}
