package com.example.recipeappkurs.usecase;

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
        if (dbHelper.recipeExists(recipe.getRecipeName())) {
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
            // Выбрасываем исключение, если рецепт уже есть в базе
            throw new RuntimeException("Уже есть такой рецепт");
        }
    }
}
