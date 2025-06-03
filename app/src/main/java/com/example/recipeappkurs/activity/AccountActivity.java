package com.example.recipeappkurs.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.recipeappkurs.R;

public class AccountActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // Получение SharedPreferences
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);

        // Поиск элементов интерфейса
        EditText editTextName = findViewById(R.id.editTextName);
        EditText editTextEmail = findViewById(R.id.editTextEmail);
        EditText editTextDiet = findViewById(R.id.editTextDiet);
        EditText editTextGoals = findViewById(R.id.editTextGoals);

        // Загрузка сохраненных данных
        editTextName.setText(prefs.getString("name", ""));
        editTextEmail.setText(prefs.getString("email", ""));
        editTextDiet.setText(prefs.getString("diet", ""));
        editTextGoals.setText(prefs.getString("goals", ""));

        // Поиск кнопки сохранения
        Button buttonSave = findViewById(R.id.buttonSave);

        // Установка обработчика нажатия
        buttonSave.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("name", editTextName.getText().toString());
            editor.putString("email", editTextEmail.getText().toString());
            editor.putString("diet", editTextDiet.getText().toString());
            editor.putString("goals", editTextGoals.getText().toString());
            editor.apply();
            Toast.makeText(AccountActivity.this, "Сохранено", Toast.LENGTH_SHORT).show();
        });
    }
}