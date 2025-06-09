package com.example.recipeappkurs.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

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
        Button btnSave = findViewById(R.id.btnSave);

        // Обработчик нажатия на кнопку "Сохранить"
        btnSave.setOnClickListener(v -> {
            // Проверка заполнения всех обязательных полей
            boolean isValid = true;
            String type = null;

            // Проверка выбора типа рецепта
            if (!radioDish.isChecked() && !radioDrink.isChecked()) {
                Toast.makeText(this, "Пожалуйста, выберите тип: Блюдо или Напиток", Toast.LENGTH_SHORT).show();
                isValid = false;
            } else {
                type = radioDish.isChecked() ? "Dish" : "Drink";
            }

            // Проверка поля названия
            String name = etName.getText().toString().trim();
            if (name.isEmpty()) {
                etName.setError("Название обязательно");
                isValid = false;
            } else {
                etName.setError(null);
            }

            // Проверка поля ингредиентов
            String ingredients = etIngredients.getText().toString().trim();
            if (ingredients.isEmpty()) {
                etIngredients.setError("Ингредиенты обязательны");
                isValid = false;
            } else {
                etIngredients.setError(null);
            }

            // Проверка поля описания
            String description = etDescription.getText().toString().trim();
            if (description.isEmpty()) {
                etDescription.setError("Описание обязательно");
                isValid = false;
            } else {
                etDescription.setError(null);
            }

            // Проверка поля инструкций
            String instructions = etInstructions.getText().toString().trim();
            if (instructions.isEmpty()) {
                etInstructions.setError("Инструкции обязательны");
                isValid = false;
            } else {
                etInstructions.setError(null);
            }

            // Проверка поля времени приготовления для напитков
            String prepTime = null;
            if (type != null && type.equals("Drink")) {
                prepTime = etPrepTime.getText().toString().trim();
                if (prepTime.isEmpty()) {
                    etPrepTime.setError("Время приготовления обязательно для напитков");
                    isValid = false;
                } else {
                    etPrepTime.setError(null);
                }
            }

            // Получение сложности (всегда имеет значение, так как это Spinner)
            String difficulty = (String) etDifficulty.getSelectedItem();

            // Если все проверки пройдены, сохраняем рецепт
            if (isValid) {
                Recipe newRecipe = new Recipe(-1, type, name, ingredients, description, instructions, prepTime, difficulty);
                try {
                    addRecipe.execute(newRecipe);
                    setResult(RESULT_OK);
                    finish();
                } catch (RuntimeException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            // Если проверки не пройдены, ошибки отображаются на полях
        });
    }
}