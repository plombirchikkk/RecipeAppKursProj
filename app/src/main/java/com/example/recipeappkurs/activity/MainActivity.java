package com.example.recipeappkurs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.recipeappkurs.R;
import com.example.recipeappkurs.adapter.RecipeAdapter;
import com.example.recipeappkurs.db.DBHelper;
import com.example.recipeappkurs.model.Recipe;
import com.example.recipeappkurs.usecase.EnsureDefaultRecipes;
import com.example.recipeappkurs.usecase.GetAllRecipes;
import com.example.recipeappkurs.usecase.SearchRecipes;

import java.util.List;

public class MainActivity extends BaseActivity {
    private TextView tvDetails;
    private RecipeAdapter adapter;
    private EnsureDefaultRecipes ensureDefaultRecipes;
    private GetAllRecipes getAllRecipes;
    private SearchRecipes searchRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация базы данных и use case-ов
        DBHelper dbHelper = new DBHelper(this);
        ensureDefaultRecipes = new EnsureDefaultRecipes(dbHelper);
        getAllRecipes = new GetAllRecipes(dbHelper);
        searchRecipes = new SearchRecipes(dbHelper);

        // Получение ссылок на элементы UI
        ListView lvRecipes = findViewById(R.id.listRecipes);
        tvDetails = findViewById(R.id.tvDetails);
        SearchView searchView = findViewById(R.id.searchView);

        // Установка адаптера с рецептами
        adapter = new RecipeAdapter(this, getAllRecipes.execute(), dbHelper, true, true, false, null);
        lvRecipes.setAdapter(adapter);

        // Обработка поиска рецептов
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query); // Поиск при нажатии Enter
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                performSearch(newText); // Поиск при изменении текста
                return true;
            }
        });

        // Отображение описания рецепта при выборе
        lvRecipes.setOnItemClickListener((parent, view, position, id) -> {
            Recipe recipe = adapter.getItem(position);
            assert recipe != null;
            tvDetails.setText(recipe.getDescription());
        });

        // Переход к экрану добавления рецепта
        findViewById(R.id.btnAddRecipe).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddRecipeActivity.class);
            startActivityForResult(intent, 1);
        });

        loadData(); // Загрузка данных при запуске
    }

    // Загрузка и отображение всех рецептов
    private void loadData() {
        ensureDefaultRecipes.execute(); // Убедиться, что есть рецепты по умолчанию
        List<Recipe> allRecipes = getAllRecipes.execute();
        adapter.clear();
        adapter.addAll(allRecipes);
        adapter.notifyDataSetChanged();

        // Отображение сообщения, если рецептов нет
        if (allRecipes.isEmpty()) {
            tvDetails.setText(getString(R.string.no_recipes));
        } else {
            tvDetails.setText("");
        }
    }

    // Выполнение поиска по названию рецепта
    private void performSearch(String query) {
        List<Recipe> searchResults;
        if (query.isEmpty()) {
            searchResults = getAllRecipes.execute();
        } else {
            searchResults = searchRecipes.execute(query);
        }
        adapter.clear();
        adapter.addAll(searchResults);
        adapter.notifyDataSetChanged();
    }

    // Обновление списка после добавления нового рецепта
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadData();
        }
    }
}
