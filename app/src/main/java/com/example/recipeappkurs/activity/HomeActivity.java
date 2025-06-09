package com.example.recipeappkurs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.recipeappkurs.R;

import java.util.Objects;

// Главная активность приложения с кнопками перехода к другим разделам
public class HomeActivity extends BaseActivity {

    // Метод вызывается при создании активности
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home); // Устанавливаем макет главного экрана
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false); // Отключаем кнопку "назад" в ActionBar

        // Инициализация кнопок по идентификаторам
        Button btnMealPlan = findViewById(R.id.btnMealPlan);     // Кнопка перехода к плану питания
        Button btnRecipes = findViewById(R.id.btnRecipes);       // Кнопка перехода к списку рецептов

        // Обработка нажатия на кнопку "План питания"
        btnMealPlan.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MealPlanActivity.class);
            startActivity(intent); // Переход к активности планирования питания
        });

        // Обработка нажатия на кнопку "Рецепты"
        btnRecipes.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent); // Переход к активности со списком рецептов
        });

    }
}
