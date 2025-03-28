package com.example.weekly_recipe_planner.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.weekly_recipe_planner.Model.GroceryItem;
import com.example.weekly_recipe_planner.Model.MealPlan;
import com.example.weekly_recipe_planner.Model.Recipe;
import com.example.weekly_recipe_planner.Model.Store;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MealMate.db";
    private static final int DATABASE_VERSION = 1;
    // Column names (should be at the top of your DatabaseHelper)
//    private static final String COLUMN_ID = "id";
//    private static final String COLUMN_USER_ID = "user_id";
//    private static final String COLUMN_STORE_ID = "store_id";
//    private static final String TABLE_GROCERY_ITEMS = "grocery_items";
    // Tables
    private static final String TABLE_RECIPES = "recipes";
    private static final String TABLE_MEAL_PLANS = "meal_plans";
    private static final String TABLE_GROCERY_ITEMS = "grocery_items";
    private static final String TABLE_STORES = "stores";

    // Common column
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USER_ID = "user_id";

    // Recipes table columns
    private static final String COLUMN_RECIPE_NAME = "name";
    private static final String COLUMN_IMAGE_PATH = "image_path";
    private static final String COLUMN_CUISINE = "cuisine";
    private static final String COLUMN_MEAL_TYPE = "meal_type";
    private static final String COLUMN_DIETARY = "dietary";
    private static final String COLUMN_PREP_TIME = "prep_time";
    private static final String COLUMN_COOK_TIME = "cook_time";
    private static final String COLUMN_SERVINGS = "servings";
    private static final String COLUMN_INGREDIENTS = "ingredients";
    private static final String COLUMN_INSTRUCTIONS = "instructions";
    private static final String COLUMN_IS_FAVORITE = "is_favorite";

    // Meal plans table columns
    private static final String COLUMN_RECIPE_ID = "recipe_id";
    private static final String COLUMN_DAY = "day";
    private static final String COLUMN_DATE = "date";

    // Grocery items table columns
    private static final String COLUMN_ITEM_NAME = "name";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_UNIT = "unit";
    private static final String COLUMN_IS_PURCHASED = "is_purchased";
    private static final String COLUMN_STORE_ID = "store_id";
    private static final String COLUMN_PRICE = "price";

    // Stores table columns
    private static final String COLUMN_STORE_NAME = "name";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create recipes table
        String CREATE_RECIPES_TABLE = "CREATE TABLE " + TABLE_RECIPES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_ID + " TEXT,"
                + COLUMN_RECIPE_NAME + " TEXT,"
                + COLUMN_IMAGE_PATH + " TEXT,"
                + COLUMN_CUISINE + " TEXT,"
                + COLUMN_MEAL_TYPE + " TEXT,"
                + COLUMN_DIETARY + " TEXT,"
                + COLUMN_PREP_TIME + " INTEGER,"
                + COLUMN_COOK_TIME + " INTEGER,"
                + COLUMN_SERVINGS + " INTEGER,"
                + COLUMN_INGREDIENTS + " TEXT,"
                + COLUMN_INSTRUCTIONS + " TEXT,"
                + COLUMN_IS_FAVORITE + " INTEGER DEFAULT 0"
                + ")";
        db.execSQL(CREATE_RECIPES_TABLE);

        // Create meal plans table
        String CREATE_MEAL_PLANS_TABLE = "CREATE TABLE " + TABLE_MEAL_PLANS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_ID + " TEXT,"
                + COLUMN_RECIPE_ID + " INTEGER,"
                + COLUMN_DAY + " TEXT,"
                + COLUMN_DATE + " TEXT"
                + ")";
        db.execSQL(CREATE_MEAL_PLANS_TABLE);

        // Create grocery items table
        String CREATE_GROCERY_ITEMS_TABLE = "CREATE TABLE " + TABLE_GROCERY_ITEMS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_ID + " TEXT,"
                + COLUMN_ITEM_NAME + " TEXT,"
                + COLUMN_CATEGORY + " TEXT,"
                + COLUMN_QUANTITY + " REAL,"
                + COLUMN_UNIT + " TEXT,"
                + COLUMN_IS_PURCHASED + " INTEGER DEFAULT 0,"
                + COLUMN_STORE_ID + " INTEGER,"
                + COLUMN_PRICE + " REAL"
                + ")";
        db.execSQL(CREATE_GROCERY_ITEMS_TABLE);

        // Create stores table
        String CREATE_STORES_TABLE = "CREATE TABLE " + TABLE_STORES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_ID + " TEXT,"
                + COLUMN_STORE_NAME + " TEXT,"
                + COLUMN_ADDRESS + " TEXT,"
                + COLUMN_LATITUDE + " REAL,"
                + COLUMN_LONGITUDE + " REAL"
                + ")";
        db.execSQL(CREATE_STORES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEAL_PLANS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROCERY_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORES);
        onCreate(db);
    }

    // Recipe CRUD operations
    public long addRecipe(Recipe recipe, String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_RECIPE_NAME, recipe.getName());
        values.put(COLUMN_IMAGE_PATH, recipe.getImagePath());
        values.put(COLUMN_CUISINE, recipe.getCuisine());
        values.put(COLUMN_MEAL_TYPE, recipe.getMealType());
        values.put(COLUMN_DIETARY, recipe.getDietary());
        values.put(COLUMN_PREP_TIME, recipe.getPrepTime());
        values.put(COLUMN_COOK_TIME, recipe.getCookTime());
        values.put(COLUMN_SERVINGS, recipe.getServings());
        values.put(COLUMN_INGREDIENTS, recipe.getIngredients());
        values.put(COLUMN_INSTRUCTIONS, recipe.getInstructions());
        values.put(COLUMN_IS_FAVORITE, recipe.isFavorite() ? 1 : 0);

        long id = db.insert(TABLE_RECIPES, null, values);
        db.close();
        return id;
    }

    public List<Recipe> getAllRecipes(String userId) {
        List<Recipe> recipes = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_RECIPES + " WHERE " + COLUMN_USER_ID + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{userId});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                try {
                    int idIndex = cursor.getColumnIndex(COLUMN_ID);
                    int nameIndex = cursor.getColumnIndex(COLUMN_RECIPE_NAME);
                    int imagePathIndex = cursor.getColumnIndex(COLUMN_IMAGE_PATH);
                    int cuisineIndex = cursor.getColumnIndex(COLUMN_CUISINE);
                    int mealTypeIndex = cursor.getColumnIndex(COLUMN_MEAL_TYPE);
                    int dietaryIndex = cursor.getColumnIndex(COLUMN_DIETARY);
                    int prepTimeIndex = cursor.getColumnIndex(COLUMN_PREP_TIME);
                    int cookTimeIndex = cursor.getColumnIndex(COLUMN_COOK_TIME);
                    int servingsIndex = cursor.getColumnIndex(COLUMN_SERVINGS);
                    int ingredientsIndex = cursor.getColumnIndex(COLUMN_INGREDIENTS);
                    int instructionsIndex = cursor.getColumnIndex(COLUMN_INSTRUCTIONS);
                    int favoriteIndex = cursor.getColumnIndex(COLUMN_IS_FAVORITE);

                    // Ensure columns exist
                    if (idIndex != -1) Recipe.setId(cursor.getInt(idIndex));
                    if (nameIndex != -1) Recipe.setName(cursor.getString(nameIndex));
                    if (imagePathIndex != -1) Recipe.setImagePath(cursor.getString(imagePathIndex));
                    if (cuisineIndex != -1) Recipe.setCuisine(cursor.getString(cuisineIndex));
                    if (mealTypeIndex != -1) Recipe.setMealType(cursor.getString(mealTypeIndex));
                    if (dietaryIndex != -1) Recipe.setDietary(cursor.getString(dietaryIndex));
                    if (prepTimeIndex != -1) Recipe.setPrepTime(cursor.getInt(prepTimeIndex));
                    if (cookTimeIndex != -1) Recipe.setCookTime(cursor.getInt(cookTimeIndex));
                    if (servingsIndex != -1) Recipe.setServings(cursor.getInt(servingsIndex));
                    if (ingredientsIndex != -1)
                        Recipe.setIngredients(cursor.getString(ingredientsIndex));
                    if (instructionsIndex != -1)
                        Recipe.setInstructions(cursor.getString(instructionsIndex));
                    if (favoriteIndex != -1) Recipe.setFavorite(cursor.getInt(favoriteIndex) == 1);
                } catch (Exception e) {
                    Log.e("DB_ERROR", "Error reading cursor data: " + e.getMessage());
                }
            } while (cursor.moveToNext());
        } else {
            Log.e("DB_ERROR", "Cursor is empty or null");
        }


    }
        // Meal Plan operations
    public long addMealPlan(MealPlan mealPlan, String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_RECIPE_ID, mealPlan.getRecipeId());
        values.put(COLUMN_DAY, mealPlan.getDay());
        values.put(COLUMN_DATE, mealPlan.getDate());

        long id = db.insert(TABLE_MEAL_PLANS, null, values);
        db.close();
        return id;
    }

    public List<MealPlan> getWeeklyMealPlan(String userId, String startDate, String endDate) {
        List<MealPlan> mealPlans = new ArrayList<>();
        String selectQuery = "SELECT mp.*, r." + COLUMN_RECIPE_NAME + ", r." + COLUMN_IMAGE_PATH +
                " FROM " + TABLE_MEAL_PLANS + " mp " +
                "INNER JOIN " + TABLE_RECIPES + " r ON mp." + COLUMN_RECIPE_ID + " = r." + COLUMN_ID + " " +
                "WHERE mp." + COLUMN_USER_ID + " = ? AND mp." + COLUMN_DATE + " BETWEEN ? AND ? " +
                "ORDER BY mp." + COLUMN_DATE + ", mp." + COLUMN_DAY;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{userId, startDate, endDate});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                try {
                    int idIndex = cursor.getColumnIndex(COLUMN_ID);
                    int recipeIdIndex = cursor.getColumnIndex(COLUMN_RECIPE_ID);
                    int dayIndex = cursor.getColumnIndex(COLUMN_DAY);
                    int dateIndex = cursor.getColumnIndex(COLUMN_DATE);
                    int recipeNameIndex = cursor.getColumnIndex(COLUMN_RECIPE_NAME);
                    int imagePathIndex = cursor.getColumnIndex(COLUMN_IMAGE_PATH);

                    // Ensure indexes are valid before accessing values
                    MealPlan mealPlan = new MealPlan();
                    if (idIndex != -1) mealPlan.setId(cursor.getInt(idIndex));
                    if (recipeIdIndex != -1) mealPlan.setRecipeId(cursor.getInt(recipeIdIndex));
                    if (dayIndex != -1) mealPlan.setDay(cursor.getString(dayIndex));
                    if (dateIndex != -1) mealPlan.setDate(cursor.getString(dateIndex));
                    if (recipeNameIndex != -1)
                        mealPlan.setRecipeName(cursor.getString(recipeNameIndex));
                    if (imagePathIndex != -1)
                        mealPlan.setRecipeImage(cursor.getString(imagePathIndex));

                } catch (Exception e) {
                    Log.e("DB_ERROR", "Error reading cursor data: " + e.getMessage());
                }
            } while (cursor.moveToNext());
        } else {
            Log.e("DB_ERROR", "Cursor is empty or null");
        }

    }
        // Grocery List operations
    public long addGroceryItem(GroceryItem item, String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_ITEM_NAME, item.getName());
        values.put(COLUMN_CATEGORY, item.getCategory());
        values.put(COLUMN_QUANTITY, item.getQuantity());
        values.put(COLUMN_UNIT, item.getUnit());
        values.put(COLUMN_IS_PURCHASED, item.isPurchased() ? 1 : 0);
        values.put(COLUMN_STORE_ID, item.getStoreId());
        values.put(COLUMN_PRICE, item.getPrice());

        long id = db.insert(TABLE_GROCERY_ITEMS, null, values);
        db.close();
        return id;
    }

    public List<GroceryItem> getGroceryList(String userId) {
        List<GroceryItem> items = new ArrayList<>();
        String selectQuery = "SELECT g.*, s." + COLUMN_STORE_NAME + " FROM " + TABLE_GROCERY_ITEMS + " g " +
                "LEFT JOIN " + TABLE_STORES + " s ON g." + COLUMN_STORE_ID + " = s." + COLUMN_ID + " " +
                "WHERE g." + COLUMN_USER_ID + " = ? AND g." + COLUMN_IS_PURCHASED + " = 0 " +
                "ORDER BY g." + COLUMN_CATEGORY + ", g." + COLUMN_ITEM_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{userId});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                try {
                    // Debugging: Print column names
                    Log.d("DB_COLUMNS", "Columns: " + Arrays.toString(cursor.getColumnNames()));

                    // Get column indexes safely
                    int idIndex = cursor.getColumnIndex(COLUMN_ID);
                    int nameIndex = cursor.getColumnIndex(COLUMN_ITEM_NAME);
                    int categoryIndex = cursor.getColumnIndex(COLUMN_CATEGORY);
                    int quantityIndex = cursor.getColumnIndex(COLUMN_QUANTITY);
                    int unitIndex = cursor.getColumnIndex(COLUMN_UNIT);
                    int purchasedIndex = cursor.getColumnIndex(COLUMN_IS_PURCHASED);
                    int storeIdIndex = cursor.getColumnIndex(COLUMN_STORE_ID);
                    int storeNameIndex = cursor.getColumnIndex(COLUMN_STORE_NAME);
                    int priceIndex = cursor.getColumnIndex(COLUMN_PRICE);

                    // Ensure indexes are valid before accessing them
                    GroceryItem item = new GroceryItem();
                    if (idIndex != -1) item.setId(cursor.getInt(idIndex));
                    if (nameIndex != -1) item.setName(cursor.getString(nameIndex));
                    if (categoryIndex != -1) item.setCategory(cursor.getString(categoryIndex));
                    if (quantityIndex != -1) item.setQuantity(cursor.getDouble(quantityIndex));
                    if (unitIndex != -1) item.setUnit(cursor.getString(unitIndex));
                    if (purchasedIndex != -1) item.setPurchased(cursor.getInt(purchasedIndex) == 1);
                    if (storeIdIndex != -1) item.setStoreId(cursor.getInt(storeIdIndex));
                    if (storeNameIndex != -1) item.setStoreName(cursor.getString(storeNameIndex));
                    if (priceIndex != -1) item.setPrice(cursor.getDouble(priceIndex));

                } catch (Exception e) {
                    Log.e("DB_ERROR", "Error reading cursor data: " + e.getMessage());
                }
            } while (cursor.moveToNext());
        } else {
            Log.e("DB_ERROR", "Cursor is empty or null");
        }
    }

    // Store operations
    public long addStore(Store store, String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_STORE_NAME, store.getName());
        values.put(COLUMN_ADDRESS, store.getAddress());
        values.put(COLUMN_LATITUDE, store.getLatitude());
        values.put(COLUMN_LONGITUDE, store.getLongitude());

        long id = db.insert(TABLE_STORES, null, values);
        db.close();
        return id;
    }

    public List<Store> getStores(String userId) {
        List<Store> stores = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_STORES + " WHERE " + COLUMN_USER_ID + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{userId});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                try {
                    Log.d("DB_COLUMNS", "Columns: " + Arrays.toString(cursor.getColumnNames()));

                    int idIndex = cursor.getColumnIndex(COLUMN_ID);
                    int nameIndex = cursor.getColumnIndex(COLUMN_STORE_NAME);
                    int addressIndex = cursor.getColumnIndex(COLUMN_ADDRESS);
                    int latitudeIndex = cursor.getColumnIndex(COLUMN_LATITUDE);
                    int longitudeIndex = cursor.getColumnIndex(COLUMN_LONGITUDE);

                    Store store = new Store();
                    if (idIndex != -1) store.setId(cursor.getInt(idIndex));
                    if (nameIndex != -1) store.setName(cursor.getString(nameIndex));
                    if (addressIndex != -1) store.setAddress(cursor.getString(addressIndex));
                    if (latitudeIndex != -1) store.setLatitude(cursor.getDouble(latitudeIndex));
                    if (longitudeIndex != -1) store.setLongitude(cursor.getDouble(longitudeIndex));

                } catch (Exception e) {
                    Log.e("DB_ERROR", "Error reading cursor: " + e.getMessage());
                }
            } while (cursor.moveToNext());
        } else {
            Log.e("DB_ERROR", "Cursor is empty or null");
        }


    }
    public int assignStoreToGroceryItem(int itemId, int storeId, String userId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_STORE_ID, storeId);

        // Update only if the item belongs to the current user
        return db.update(TABLE_GROCERY_ITEMS,
                values,
                COLUMN_ID + " = ? AND " + COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(itemId), userId});
    }
    public int updateGroceryItem(GroceryItem item, String userId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEM_NAME, item.getName());
        values.put(COLUMN_CATEGORY, item.getCategory());
        values.put(COLUMN_QUANTITY, item.getQuantity());
        values.put(COLUMN_UNIT, item.getUnit());
        values.put(COLUMN_IS_PURCHASED, item.isPurchased() ? 1 : 0);
        values.put(COLUMN_STORE_ID, item.getStoreId());
        values.put(COLUMN_PRICE, item.getPrice());

        // Update only if the item belongs to the current user
        return db.update(TABLE_GROCERY_ITEMS,
                values,
                COLUMN_ID + " = ? AND " + COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(item.getId()), userId});
    }
    public int clearPurchasedItems(String userId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Delete only purchased items that belong to this user
        return db.delete(TABLE_GROCERY_ITEMS,
                COLUMN_IS_PURCHASED + " = 1 AND " + COLUMN_USER_ID + " = ?",
                new String[]{userId});
    }
    public List<GroceryItem> generateGroceryListFromMealPlan(int userId, String startDate, String endDate) {
        List<GroceryItem> groceryList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT DISTINCT g.id, g.name, g.price, g.description, g.image, g.location " +
                "FROM meal_plan mp " +
                "JOIN recipe r ON mp.recipe_id = r.id " +
                "JOIN recipe_grocery rg ON r.id = rg.recipe_id " +
                "JOIN grocery g ON rg.grocery_id = g.id " +
                "WHERE mp.user_id = ? AND mp.date BETWEEN ? AND ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId), startDate, endDate});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                String image = cursor.getString(cursor.getColumnIndexOrThrow("image"));
                String location = cursor.getString(cursor.getColumnIndexOrThrow("location"));

                groceryList.add(new GroceryItem(id, name, price, description, image, location));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return groceryList;
    }

}
