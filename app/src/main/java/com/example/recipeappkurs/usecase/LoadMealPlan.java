package com.example.recipeappkurs.usecase;

import com.example.recipeappkurs.db.DBHelper;
import com.example.recipeappkurs.model.Recipe;

import java.util.List;
import java.util.Map;

public class LoadMealPlan {
    private final DBHelper dbHelper;

    // Конструктор с передачей зависимости DBHelper
    public LoadMealPlan(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    // Загружает план питания для переданного списка дат
    // Возвращает Map<дата, Map<приём пищи, рецепт>>
    public Map<String, Map<String, Recipe>> execute(List<String> dates) {
        return dbHelper.getMealPlanForDates(dates);
    }
}
