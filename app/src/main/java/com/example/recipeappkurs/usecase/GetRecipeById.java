package com.example.recipeappkurs.usecase;

import com.example.recipeappkurs.db.DBHelper;
import com.example.recipeappkurs.model.Recipe;

public class GetRecipeById {
    private final DBHelper dbHelper;

    // Конструктор с передачей зависимости DBHelper
    public GetRecipeById(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    // Возвращает рецепт по его ID из базы данных
    public Recipe execute(int id) {
        return dbHelper.getRecipeById(id);
    }
}
