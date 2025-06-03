package com.example.recipeappkurs.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;


import com.example.recipeappkurs.R;
import com.example.recipeappkurs.db.DBHelper;
import com.example.recipeappkurs.model.Recipe;
import com.example.recipeappkurs.usecase.AddRecipe;

// Класс активности для добавления нового рецепта
public class AddRecipeActivity extends BaseActivity {

    // Объявление полей для ввода данных
    private EditText etName, etIngredients, etDescription, etInstructions, etPrepTime;
    private Spinner etDifficulty; // Выпадающий список для выбора сложности
    private RadioButton radioDish, radioDrink; // Радиокнопки для выбора типа рецепта
    private AddRecipe addRecipe;
    // Метод вызывается при создании активности
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe); // Устанавливаем макет активности

        addRecipe = new AddRecipe(new DBHelper(this)); // Инициализация use case

        // Инициализация полей ввода по их идентификаторам
        etName = findViewById(R.id.editName);
        etIngredients = findViewById(R.id.editIngredients);
        etDescription = findViewById(R.id.editDescription);
        etInstructions = findViewById(R.id.editInstructions);
        etPrepTime = findViewById(R.id.editPrepTime);
        etDifficulty = findViewById(R.id.spinnerDifficulty);

        // Настройка адаптера для выпадающего списка сложности
        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"Легко", "Средне", "Сложно"} // Варианты сложности
        );
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etDifficulty.setAdapter(difficultyAdapter); // Устанавливаем адаптер для Spinner

        // Инициализация радиокнопок и кнопки сохранения
        radioDish = findViewById(R.id.radioDish);
        radioDrink = findViewById(R.id.radioDrink);
        // Кнопка сохранения рецепта
        Button btnSave = findViewById(R.id.btnSave);


        // Обработчик нажатия на кнопку "Сохранить"
        btnSave.setOnClickListener(v -> {
            // Получаем значения из полей ввода
            String name = etName.getText().toString();
            String ingredients = etIngredients.getText().toString();
            String description = etDescription.getText().toString();
            String instructions = etInstructions.getText().toString();
            String type = radioDish.isChecked() ? "Dish" : "Drink"; // Определяем тип рецепта

            // Если выбран напиток, получаем время приготовления и сложность
            String prepTime = radioDrink.isChecked() ? etPrepTime.getText().toString() : null;
            String difficulty = radioDrink.isChecked() ? (String) etDifficulty.getSelectedItem() : null;

            Recipe newRecipe = new Recipe(-1,type, name, ingredients, description, instructions, prepTime, difficulty);
            // Добавляем рецепт в базу данных
            addRecipe.execute(newRecipe);

            // Устанавливаем результат и закрываем активность
            setResult(RESULT_OK);
            finish();
        });
    }
}
