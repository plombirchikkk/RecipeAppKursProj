package com.example.recipeappkurs.usecase;

import android.util.Log;

import com.example.recipeappkurs.db.DBHelper;
import com.example.recipeappkurs.model.Recipe;

public class AddRecipe {
    private final DBHelper dbHelper;

    // Конструктор с передачей зависимости DBHelper
    public AddRecipe(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    // Добавляет рецепт, если с таким названием ещё не существует
    public void execute(Recipe recipe) {
        Log.d("AddRecipe", "Trying to add recipe: " + recipe.getRecipeName());
        if (!dbHelper.recipeExists(recipe.getRecipeName())) {
            Log.d("AddRecipe", "Recipe does not exist, adding: " + recipe.getRecipeName());
            dbHelper.addRecipe(
                    recipe.getType(),
                    recipe.getRecipeName(),
                    recipe.getIngredients(),
                    recipe.getDescription(),
                    recipe.getInstructions(),
                    recipe.getPrepTime(),
                    recipe.getDifficulty()
            );
        } else {
            Log.d("AddRecipe", "Recipe already exists: " + recipe.getRecipeName());
            throw new RuntimeException("Уже есть такой рецепт");
        }
    }
}