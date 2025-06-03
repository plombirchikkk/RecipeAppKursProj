package com.example.recipeappkurs.activity;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Включаем кнопку "Назад" в панели действий
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Обработка нажатия на кнопку "Назад" в ActionBar
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Возврат к предыдущему экрану
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
