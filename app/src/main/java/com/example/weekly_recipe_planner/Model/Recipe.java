package com.example.weekly_recipe_planner.Model;

public class Recipe {
    private int id;
    private String name;
    private String imagePath;
    private String cuisine;
    private String mealType;
    private String dietary;
    private int prepTime;
    private int cookTime;
    private int servings;
    private String ingredients;
    private String instructions;
    private boolean isFavorite;

    // Getters and setters for all fields
    public int getId() {
        return id;
    }
    public static void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public static void setName(String name) {
        this.name = name;
    }
    public String getImagePath() {
        return imagePath;
    }
    public static void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public String getCuisine() {
        return cuisine;
    }
    public static void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }
    public String getMealType() {
        return mealType;
    }
    public static void setMealType(String mealType) {
        this.mealType = mealType;
    }
    public String getDietary() {
        return dietary;
    }
    public static void setDietary(String dietary) {
        this.dietary = dietary;
    }
    public int getPrepTime() {
        return prepTime;
    }
    public static void setPrepTime(int prepTime) {
        this.prepTime = prepTime;
    }
    public int getCookTime() {
        return cookTime;
    }
    public static void setCookTime(int cookTime) {
        this.cookTime = cookTime;
    }
    public int getServings() {
        return servings;
    }
    public static void setServings(int servings) {
        this.servings = servings;
    }
    public String getIngredients() {
        return ingredients;
    }
    public static void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }
    public String getInstructions() {
        return instructions;
    }
    public static void setInstructions(String instructions) {
        this.instructions = instructions;
    }
    public boolean isFavorite() {
        return isFavorite;
    }
    public static void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
