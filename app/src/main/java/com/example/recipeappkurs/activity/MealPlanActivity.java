package com.example.recipeappkurs.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ExpandableListView;


import com.example.recipeappkurs.R;
import com.example.recipeappkurs.adapter.MealPlanAdapter;
import com.example.recipeappkurs.db.DBHelper;
import com.example.recipeappkurs.model.Recipe;
import com.example.recipeappkurs.usecase.GetRecipeById;
import com.example.recipeappkurs.usecase.LoadMealPlan;
import com.example.recipeappkurs.usecase.SetMealPlanEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MealPlanActivity extends BaseActivity {
    private MealPlanAdapter adapter;
    private List<String> mealTimes;
    private List<String> weekDates;
    private Map<String, Map<String, Recipe>> mealPlan;
    private Calendar currentWeekStart;
    private String selectedDate;
    private String selectedMealTime;
    private LoadMealPlan loadMealPlan;
    private GetRecipeById getRecipeById;
    private SetMealPlanEntry setMealPlanEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);

        // Инициализация зависимостей и use-case'ов
        DBHelper dbHelper = new DBHelper(this);
        loadMealPlan = new LoadMealPlan(dbHelper);
        getRecipeById = new GetRecipeById(dbHelper);
        setMealPlanEntry = new SetMealPlanEntry(dbHelper);

        // Список времён приёмов пищи
        mealTimes = Arrays.asList("Завтрак", "Обед", "Ужин");

        // Устанавливаем начало текущей недели (с понедельника)
        currentWeekStart = Calendar.getInstance();
        currentWeekStart.setFirstDayOfWeek(Calendar.MONDAY);
        currentWeekStart.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        // Получаем даты недели и загружаем план питания
        weekDates = getWeekDatesFromStart(currentWeekStart);
        mealPlan = loadMealPlan.execute(weekDates);

        // Настройка ExpandableListView с адаптером
        ExpandableListView listView = findViewById(R.id.meal_plan_list);
        adapter = new MealPlanAdapter(this, weekDates, mealPlan, mealTimes);
        listView.setAdapter(adapter);

        // Кнопки переключения недель
        Button btnPrevWeek = findViewById(R.id.btnPrevWeek);
        Button btnNextWeek = findViewById(R.id.btnNextWeek);

        btnPrevWeek.setOnClickListener(v -> {
            currentWeekStart.add(Calendar.WEEK_OF_YEAR, -1); // Переключение на предыдущую неделю
            refreshMealPlan();
        });

        btnNextWeek.setOnClickListener(v -> {
            currentWeekStart.add(Calendar.WEEK_OF_YEAR, 1); // Переключение на следующую неделю
            refreshMealPlan();
        });

        // Обработка выбора ячейки плана питания
        listView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            selectedDate = weekDates.get(groupPosition);
            selectedMealTime = mealTimes.get(childPosition);

            // Переход к выбору рецепта
            Intent intent = new Intent(MealPlanActivity.this, SelectRecipeActivity.class);
            startActivityForResult(intent, 1);
            return true;
        });

        updateTitle(); // Устанавливаем заголовок экрана (диапазон дат)
    }

    // Получает список дат недели, начиная с переданной даты
    private List<String> getWeekDatesFromStart(Calendar start) {
        Calendar tempCal = (Calendar) start.clone();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<String> weekDates = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            weekDates.add(dateFormat.format(tempCal.getTime()));
            tempCal.add(Calendar.DAY_OF_MONTH, 1);
        }
        return weekDates;
    }

    // Обновляет отображение плана питания при смене недели
    private void refreshMealPlan() {
        weekDates = getWeekDatesFromStart(currentWeekStart);
        mealPlan = loadMealPlan.execute(weekDates);
        adapter.setWeekDates(weekDates);
        adapter.setMealPlan(mealPlan);
        adapter.notifyDataSetChanged();
        updateTitle();
    }

    // Обновляет заголовок экрана (даты текущей недели)
    private void updateTitle() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Calendar weekEnd = (Calendar) currentWeekStart.clone();
        weekEnd.add(Calendar.DAY_OF_MONTH, 6);
        String title = dateFormat.format(currentWeekStart.getTime()) + " - " + dateFormat.format(weekEnd.getTime());
        setTitle(title);
    }

    // Обработка результата выбора рецепта
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            int recipeId = data.getIntExtra("recipe_id", -1);
            if (recipeId != -1) {
                Recipe selectedRecipe = getRecipeById.execute(recipeId);
                if (selectedRecipe != null) {
                    Objects.requireNonNull(mealPlan.get(selectedDate)).put(selectedMealTime, selectedRecipe); // Обновляем модель
                    adapter.notifyDataSetChanged(); // Обновляем UI
                    setMealPlanEntry.execute(selectedDate, selectedMealTime, selectedRecipe.getId()); // Сохраняем в БД
                }
            }
        }
    }
}
