package com.example.recipeappkurs.usecase;

import com.example.recipeappkurs.db.DBHelper;

import java.util.Arrays;
import java.util.List;

public class EnsureDefaultRecipes {
    private final DBHelper dbHelper;

    // Конструктор с передачей зависимости DBHelper
    public EnsureDefaultRecipes(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    // Добавляет набор стандартных рецептов, если их нет в базе
    public void execute() {
        // Список названий рецептов по умолчанию
        List<String> defaultRecipeNames = Arrays.asList(
                "Спагетти болоньезе",
                "Куриный стир-фрай",
                "Маргарита",
                "Пина Колада"
        );

        // Проверяем и добавляем каждый рецепт, если он отсутствует
        for (String name : defaultRecipeNames) {
            if (dbHelper.recipeExists(name)) {
                switch (name) {
                    case "Спагетти болоньезе":
                        dbHelper.addRecipe(
                                "Dish",
                                "Спагетти болоньезе",
                                "Спагетти, фарш говяжий, томатный соус, лук, чеснок, оливковое масло, соль, перец",
                                "Классическое итальянское блюдо с ароматным мясным соусом.",
                                "Отварить спагетти... Подать соус с отваренными спагетти.",
                                "30 минут",
                                "Средне"
                        );
                        break;
                    case "Куриный стир-фрай":
                        dbHelper.addRecipe(
                                "Dish",
                                "Куриный стир-фрай",
                                "Куриная грудка, овощи..., растительное масло",
                                "Быстрое и полезное блюдо с нежным куриным мясом и хрустящими овощами.",
                                "Нарезать курицу... жарить до готовности овощей.",
                                "20 минут",
                                "Легко"
                        );
                        break;
                    case "Маргарита":
                        dbHelper.addRecipe(
                                "Drink",
                                "Маргарита",
                                "Текила, лимонный сок, трипл сек, соль для ободка",
                                "Освежающий коктейль, идеальный для лета.",
                                "Обмакнуть край бокала... украсить долькой лимона.",
                                "5 минут",
                                "Легко"
                        );
                        break;
                    case "Пина Колада":
                        dbHelper.addRecipe(
                                "Drink",
                                "Пина Колада",
                                "Ром, ананасный сок, кокосовый крем, лед",
                                "Тропический коктейль, напоминающий отдых на пляже.",
                                "В блендере смешать... украсить долькой ананаса.",
                                "5 минут",
                                "Легко"
                        );
                        break;
                }
            }
        }
    }
}
