package com.example.weekly_recipe_planner.Helpers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.example.weekly_recipe_planner.Model.Recipe;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecipeImportHelper {
    public static void handleRecipeImport(Context context, Intent intent) {
        if (intent == null || intent.getData() == null) return;

        Uri uri = intent.getData();
        String url = uri.toString();

        new Thread(() -> {
            try {
                Document doc = Jsoup.connect(url).get();
                String title = doc.title();

                // Extract ingredients (this will vary based on website structure)
                List<String> ingredients = new ArrayList<>();
                Elements ingredientElements = doc.select("li.ingredient, span.ingredient, div.ingredients li");
                for (Element element : ingredientElements) {
                    ingredients.add(element.text());
                }

                // Extract instructions
                List<String> instructions = new ArrayList<>();
                Elements instructionElements = doc.select("li.instruction, div.instructions li, div.step");
                for (Element element : instructionElements) {
                    instructions.add(element.text());
                }

                // Create a Recipe object
                Recipe recipe = new Recipe();
                recipe.setName(title);
                recipe.setIngredients(TextUtils.join("\n", ingredients));
                recipe.setInstructions(TextUtils.join("\n", instructions));

                // You can now save this recipe to your database
                // and notify the user it was imported successfully
            } catch (IOException e) {
                e.printStackTrace();
                // Notify user of import failure
            }
        }).start();
    }
}
