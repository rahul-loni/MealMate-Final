package com.example.weekly_recipe_planner.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.weekly_recipe_planner.Model.MealItem;
import com.example.weekly_recipe_planner.R;

import java.util.List;

public class MealItemAdapter extends RecyclerView.Adapter<MealItemAdapter.MealItemViewHolder> {

    private List<MealItem> mealItems;
    private OnMealItemClickListener listener;

    public interface OnMealItemClickListener {
        void onMealItemClick(MealItem mealItem);
        void onRemoveClick(MealItem mealItem);
    }

    public MealItemAdapter(List<MealItem> mealItems, OnMealItemClickListener listener) {
        this.mealItems = mealItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MealItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meal_plan_recipe, parent, false);
        return new MealItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealItemViewHolder holder, int position) {
        MealItem mealItem = mealItems.get(position);
        holder.bind(mealItem, listener);
    }

    @Override
    public int getItemCount() {
        return mealItems.size();
    }

    static class MealItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView recipeImage;
        private TextView mealTypeTextView;
        private TextView recipeNameTextView;
        private ImageView removeButton;

        public MealItemViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeImage = itemView.findViewById(R.id.recipeImage);
            mealTypeTextView = itemView.findViewById(R.id.mealTypeTextView);
            recipeNameTextView = itemView.findViewById(R.id.recipeNameTextView);
            removeButton = itemView.findViewById(R.id.removeButton);
        }

        public void bind(MealItem mealItem, OnMealItemClickListener listener) {
            mealTypeTextView.setText(mealItem.getMealType());
            recipeNameTextView.setText(mealItem.getRecipeName());

            if (mealItem.getImageUrl() != null && !mealItem.getImageUrl().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(mealItem.getImageUrl())
                        .placeholder(R.drawable.ic_placeholder)
                        .into(recipeImage);
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMealItemClick(mealItem);
                }
            });

            removeButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onRemoveClick(mealItem);
                }
            });
        }
    }
}
