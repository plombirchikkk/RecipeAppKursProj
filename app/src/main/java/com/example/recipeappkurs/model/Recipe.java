package com.example.recipeappkurs.model;

// Класс модели для представления рецепта.
public class Recipe {
    private final int id;
    private final String type;
    private final String recipeName;
    private final String ingredients;
    private final String description;
    private final String instructions;
    private final String prepTime;
    private final String difficulty;

    // Конструктор для создания объекта рецепта.
    public Recipe(int id, String type, String recipeName, String ingredients, String description,
                  String instructions, String prepTime, String difficulty) {
        this.id = id;
        this.type = type;
        this.recipeName = recipeName;
        this.ingredients = ingredients;
        this.description = description;
        this.instructions = instructions;
        this.prepTime = prepTime;
        this.difficulty = difficulty;
    }

    // Получение идентификатора рецепта.
    public int getId() {
        return id;
    }

    // Получение типа рецепта.
    public String getType() {
        return type;
    }

    // Получение названия рецепта.
    public String getRecipeName() {
        return recipeName;
    }

    // Получение списка ингредиентов.
    public String getIngredients() {
        return ingredients;
    }

    // Получение описания рецепта.
    public String getDescription() {
        return description;
    }

    // Получение инструкций по приготовлению.
    public String getInstructions() {
        return instructions;
    }

    // Получение времени приготовления.
    public String getPrepTime() {
        return prepTime;
    }

    // Получение уровня сложности.
    public String getDifficulty() {
        return difficulty;
    }

    // Формирование строки с деталями рецепта.
    public String getDetails() {
        String typeDisplay = type.equals("Dish") ? "Блюдо" : "Напиток";
        StringBuilder details = new StringBuilder();
        details.append("=== ").append(typeDisplay).append(" ===\n");
        details.append("Название: ").append(recipeName).append("\n");
        details.append("Ингредиенты:\n").append(ingredients).append("\n");
        details.append("Описание:\n").append(description).append("\n");
        details.append("Инструкции:\n").append(instructions).append("\n");
        if (type.equals("Drink")) {
            details.append("Время приготовления: ").append(prepTime).append("\n");
            details.append("Сложность: ").append(difficulty).append("\n");
        }
        return details.toString();
    }

}