package com.example.recipeappkurs.usecase;

import com.example.recipeappkurs.db.DBHelper;
import com.example.recipeappkurs.model.Recipe;

import java.util.List;

public class SearchRecipes {
    private final DBHelper dbHelper;

    // Конструктор с передачей зависимости DBHelper
    public SearchRecipes(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    // Ищет рецепты по названию или возвращает все, если запрос пустой
    public List<Recipe> execute(String query) {
        if (query.isEmpty()) {
            return dbHelper.getAllRecipes();
        } else {
            return dbHelper.searchRecipesByName(query);
        }
    }
}
