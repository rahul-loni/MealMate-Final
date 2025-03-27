package com.example.weekly_recipe_planner.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.weekly_recipe_planner.Model.Recipe;
import com.example.weekly_recipe_planner.R;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private List<Recipe> recipes;
    private OnRecipeClickListener listener;

    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    public RecipeAdapter(List<Recipe> recipes, OnRecipeClickListener listener) {
        this.recipes = recipes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);

        holder.nameTextView.setText(recipe.getName());
        holder.cuisineTextView.setText(recipe.getCuisine());
        holder.timeTextView.setText(recipe.getPrepTime() + " min prep • " + recipe.getCookTime() + " min cook");

        if (recipe.getImagePath() != null && !recipe.getImagePath().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(recipe.getImagePath())
                    .into(holder.imageView);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRecipeClick(recipe);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView, cuisineTextView, timeTextView;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.recipeImage);
            nameTextView = itemView.findViewById(R.id.recipeName);
            cuisineTextView = itemView.findViewById(R.id.recipeCuisine);
            timeTextView = itemView.findViewById(R.id.recipeTime);
        }
    }
}
