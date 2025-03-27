package com.example.weekly_recipe_planner.Model;

import java.util.List;

public class MealPlan {
    private int id;
    private int recipeId;
    private String day;
    private String date;
    private String recipeName;
    private String recipeImage;
    private List<MealItem> meals;

    // Getters and setters for all fields
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getRecipeId() {
        return recipeId;
    }
    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }
    public String getDay() {
        return day;
    }
    public void setDay(String day) {
        this.day = day;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getRecipeName() {
        return recipeName;
    }
    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }
    public String getRecipeImage() {
        return recipeImage;
    }
    public void setRecipeImage(String recipeImage) {
        this.recipeImage = recipeImage;
    }

    public List<MealItem> getMeals() {
        return meals;
    }
}
