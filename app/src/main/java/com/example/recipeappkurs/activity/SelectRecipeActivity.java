package com.example.recipeappkurs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.example.recipeappkurs.R;
import com.example.recipeappkurs.adapter.RecipeAdapter;
import com.example.recipeappkurs.db.DBHelper;
import com.example.recipeappkurs.model.Recipe;
import com.example.recipeappkurs.usecase.GetAllRecipes;

import java.util.List;

public class SelectRecipeActivity extends BaseActivity implements RecipeAdapter.OnRecipeSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_recipe);

        DBHelper dbHelper = new DBHelper(this);
        GetAllRecipes getAllRecipes = new GetAllRecipes(dbHelper);

        // Получаем список всех рецептов
        ListView listView = findViewById(R.id.listView);
        List<Recipe> recipes = getAllRecipes.execute();

        // Инициализируем адаптер с передачей слушателя выбора рецепта
        RecipeAdapter adapter = new RecipeAdapter(this, recipes, dbHelper, true, false, true, this);
        listView.setAdapter(adapter);
    }

    // Обработка выбора рецепта из списка, возвращает ID выбранного рецепта
    @Override
    public void onRecipeSelected(int recipeId) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("recipe_id", recipeId);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
