package com.example.recipeappkurs.usecase;

import com.example.recipeappkurs.db.DBHelper;

public class SetMealPlanEntry {
    private final DBHelper dbHelper;

    // Конструктор с передачей зависимости DBHelper
    public SetMealPlanEntry(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    // Устанавливает рецепт в план питания по дате и времени приёма пищи
    public void execute(String date, String mealTime, int recipeId) {
        dbHelper.setMealPlanEntry(date, mealTime, recipeId);
    }
}
