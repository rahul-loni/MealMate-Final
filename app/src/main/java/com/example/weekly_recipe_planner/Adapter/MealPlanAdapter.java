package com.example.weekly_recipe_planner.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weekly_recipe_planner.Model.MealItem;
import com.example.weekly_recipe_planner.Model.MealPlan;
import com.example.weekly_recipe_planner.R;

import java.util.List;

public class MealPlanAdapter extends RecyclerView.Adapter<MealPlanAdapter.MealPlanViewHolder> {

    private List<MealPlan> mealPlans;
    private OnMealPlanClickListener listener;

    public interface OnMealPlanClickListener {
        void onMealPlanClick(MealPlan mealPlan);
        void onRemoveClick(MealPlan mealPlan);
    }

    public MealPlanAdapter(List<MealPlan> mealPlans, OnMealPlanClickListener listener) {
        this.mealPlans = mealPlans;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MealPlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meal_plan_day, parent, false);
        return new MealPlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealPlanViewHolder holder, int position) {
        MealPlan mealPlan = mealPlans.get(position);
        holder.bind(mealPlan, listener);
    }

    @Override
    public int getItemCount() {
        return mealPlans.size();
    }

    public void updateData(List<MealPlan> newMealPlans) {
        this.mealPlans = newMealPlans;
        notifyDataSetChanged();
    }

    static class MealPlanViewHolder extends RecyclerView.ViewHolder {
        private TextView dayTextView;
        private TextView dateTextView;
        private RecyclerView mealsRecyclerView;

        public MealPlanViewHolder(@NonNull View itemView) {
            super(itemView);
            dayTextView = itemView.findViewById(R.id.dayNameTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            mealsRecyclerView = itemView.findViewById(R.id.mealsRecyclerView);
        }

        public void bind(MealPlan mealPlan, OnMealPlanClickListener listener) {
            dayTextView.setText(mealPlan.getDay());
            dateTextView.setText(mealPlan.getDate());

            // Setup inner RecyclerView for meals of the day
            MealItemAdapter adapter = new MealItemAdapter(mealPlan.getMeals(), new MealItemAdapter.OnMealItemClickListener() {
                @Override
                public void onMealItemClick(MealItem mealItem) {
                    if (listener != null) {
                        listener.onMealPlanClick(mealPlan);
                    }
                }

                @Override
                public void onRemoveClick(MealItem mealItem) {
                    if (listener != null) {
                        listener.onRemoveClick(mealPlan);
                    }
                }
            });

            mealsRecyclerView.setAdapter(adapter);
            mealsRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        }
    }
}
