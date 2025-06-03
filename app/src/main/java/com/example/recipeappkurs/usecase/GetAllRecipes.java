package com.example.recipeappkurs.usecase;

import com.example.recipeappkurs.db.DBHelper;
import com.example.recipeappkurs.model.Recipe;

import java.util.List;

public class GetAllRecipes {
    private final DBHelper dbHelper;

    // Конструктор с передачей зависимости DBHelper
    public GetAllRecipes(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    // Получает список всех рецептов из базы данных
    public List<Recipe> execute() {
        return dbHelper.getAllRecipes();
    }
}
