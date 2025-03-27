package com.example.weekly_recipe_planner;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.weekly_recipe_planner.Helpers.DatabaseHelper;
import com.example.weekly_recipe_planner.Model.Recipe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.UUID;

public class AddRecipeActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText nameEditText, ingredientsEditText, instructionsEditText;
    private Spinner cuisineSpinner, mealTypeSpinner, dietarySpinner;
    private EditText prepTimeEditText, cookTimeEditText, servingsEditText;
    private ImageView recipeImageView;
    private Button saveButton, addImageButton;

    private Uri imageUri;
    private DatabaseHelper dbHelper;
    private String userId;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();
        }

        dbHelper = new DatabaseHelper(this);
        storage = FirebaseStorage.getInstance();

        initializeViews();
        setupSpinners();

        addImageButton.setOnClickListener(v -> openImageChooser());
        saveButton.setOnClickListener(v -> saveRecipe());
    }

    private void initializeViews() {
        nameEditText = findViewById(R.id.recipeNameEditText);
        ingredientsEditText = findViewById(R.id.ingredientsEditText);
        instructionsEditText = findViewById(R.id.instructionsEditText);
        cuisineSpinner = findViewById(R.id.cuisineSpinner);
        mealTypeSpinner = findViewById(R.id.mealTypeSpinner);
        dietarySpinner = findViewById(R.id.dietarySpinner);
        prepTimeEditText = findViewById(R.id.prepTimeEditText);
        cookTimeEditText = findViewById(R.id.cookTimeEditText);
        servingsEditText = findViewById(R.id.servingsEditText);
        recipeImageView = findViewById(R.id.recipeImageView);
        saveButton = findViewById(R.id.saveButton);
        addImageButton = findViewById(R.id.addImageButton);
    }

    private void setupSpinners() {
        // Cuisine options
        ArrayAdapter<CharSequence> cuisineAdapter = ArrayAdapter.createFromResource(this,
                R.array.cuisine_options, android.R.layout.simple_spinner_item);
        cuisineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cuisineSpinner.setAdapter(cuisineAdapter);

        // Meal type options
        ArrayAdapter<CharSequence> mealTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.meal_type_options, android.R.layout.simple_spinner_item);
        mealTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mealTypeSpinner.setAdapter(mealTypeAdapter);

        // Dietary options
        ArrayAdapter<CharSequence> dietaryAdapter = ArrayAdapter.createFromResource(this,
                R.array.dietary_options, android.R.layout.simple_spinner_item);
        dietaryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dietarySpinner.setAdapter(dietaryAdapter);
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            recipeImageView.setImageURI(imageUri);
        }
    }

    private void saveRecipe() {
        String name = nameEditText.getText().toString().trim();
        String ingredients = ingredientsEditText.getText().toString().trim();
        String instructions = instructionsEditText.getText().toString().trim();
        String cuisine = cuisineSpinner.getSelectedItem().toString();
        String mealType = mealTypeSpinner.getSelectedItem().toString();
        String dietary = dietarySpinner.getSelectedItem().toString();

        int prepTime = 0;
        try {
            prepTime = Integer.parseInt(prepTimeEditText.getText().toString());
        } catch (NumberFormatException e) {
            prepTimeEditText.setError("Please enter a valid number");
            return;
        }

        int cookTime = 0;
        try {
            cookTime = Integer.parseInt(cookTimeEditText.getText().toString());
        } catch (NumberFormatException e) {
            cookTimeEditText.setError("Please enter a valid number");
            return;
        }

        int servings = 0;
        try {
            servings = Integer.parseInt(servingsEditText.getText().toString());
        } catch (NumberFormatException e) {
            servingsEditText.setError("Please enter a valid number");
            return;
        }

        if (name.isEmpty()) {
            nameEditText.setError("Recipe name is required");
            return;
        }

        if (ingredients.isEmpty()) {
            ingredientsEditText.setError("Ingredients are required");
            return;
        }

        if (instructions.isEmpty()) {
            instructionsEditText.setError("Instructions are required");
            return;
        }

        Recipe recipe = new Recipe();
        recipe.setName(name);
        recipe.setIngredients(ingredients);
        recipe.setInstructions(instructions);
        recipe.setCuisine(cuisine);
        recipe.setMealType(mealType);
        recipe.setDietary(dietary);
        recipe.setPrepTime(prepTime);
        recipe.setCookTime(cookTime);
        recipe.setServings(servings);

        if (imageUri != null) {
            uploadImageAndSaveRecipe(recipe);
        } else {
            saveRecipeToDatabase(recipe, null);
        }
    }

    private void uploadImageAndSaveRecipe(Recipe recipe) {
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child("recipe_images/" + UUID.randomUUID().toString());

        imagesRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    imagesRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        saveRecipeToDatabase(recipe, imageUrl);
                    });
                })
                .addOnFailureListener(e -> {
                    // Image upload failed, save recipe without image
                    saveRecipeToDatabase(recipe, null);
                });
    }

    private void saveRecipeToDatabase(Recipe recipe, String imageUrl) {
        recipe.setImagePath(imageUrl);
        long id = dbHelper.addRecipe(recipe, userId);

        if (id != -1) {
            // Recipe saved successfully
            setResult(RESULT_OK);
            finish();
        } else {
            // Failed to save recipe
            Toast.makeText(this, "Failed to save recipe", Toast.LENGTH_SHORT).show();
        }
    }

}