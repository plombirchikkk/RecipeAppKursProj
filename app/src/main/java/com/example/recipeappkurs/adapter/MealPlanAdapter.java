package com.example.recipeappkurs.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.recipeappkurs.R;
import com.example.recipeappkurs.db.DBHelper;
import com.example.recipeappkurs.model.Recipe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

// Адаптер для отображения плана питания.
public class MealPlanAdapter extends BaseExpandableListAdapter {
    private final Context context;
    private List<String> weekDates;
    private Map<String, Map<String, Recipe>> mealPlan;
    private final List<String> mealTimes;
    private final DBHelper dbHelper;

    // Конструктор адаптера.
    public MealPlanAdapter(Context context, List<String> weekDates, Map<String, Map<String, Recipe>> mealPlan, List<String> mealTimes) {
        this.context = context;
        this.weekDates = weekDates;
        this.mealPlan = mealPlan;
        this.mealTimes = mealTimes;
        this.dbHelper = new DBHelper(context);
    }

    // Возвращает количество групп (дней недели).
    @Override
    public int getGroupCount() {
        return weekDates.size();
    }

    // Возвращает количество дочерних элементов (времен приема пищи).
    @Override
    public int getChildrenCount(int groupPosition) {
        return mealTimes.size();
    }

    // Получает отформатированную дату для группы.
    @Override
    public String getGroup(int groupPosition) {
        String dateStr = weekDates.get(groupPosition);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE, d MMMM", new Locale("ru"));
        try {
            Date date = inputFormat.parse(dateStr);
            assert date != null;
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateStr;
        }
    }

    // Получает время приема пищи для дочернего элемента.
    @Override
    public String getChild(int groupPosition, int childPosition) {
        return mealTimes.get(childPosition);
    }

    // Возвращает ID группы.
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    // Возвращает ID дочернего элемента.
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    // Указывает, стабильны ли ID.
    @Override
    public boolean hasStableIds() {
        return false;
    }

    // Создает или обновляет вид для группы (дня).
    @SuppressLint("InflateParams")
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(android.R.layout.simple_expandable_list_item_1, null);
        }
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(getGroup(groupPosition));
        return convertView;
    }

    // Создает или обновляет вид для дочернего элемента (приема пищи).
    @SuppressLint("InflateParams")
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_meal_plan, null);
        }
        TextView mealTimeText = convertView.findViewById(R.id.meal_time_text);
        TextView recipeText = convertView.findViewById(R.id.recipe_text);
        Button btnDelete = convertView.findViewById(R.id.btnDeleteMealPlan);

        String date = weekDates.get(groupPosition);
        String mealTime = mealTimes.get(childPosition);
        mealTimeText.setText(mealTime);
        Recipe recipe = Objects.requireNonNull(mealPlan.get(date)).get(mealTime);
        recipeText.setText(recipe != null ? recipe.getRecipeName() : "Не выбрано");

        if (recipe != null) {
            btnDelete.setVisibility(View.VISIBLE);
            btnDelete.setOnClickListener(v -> {
                dbHelper.deleteMealPlanEntry(date, mealTime);
                Objects.requireNonNull(mealPlan.get(date)).put(mealTime, null);
                notifyDataSetChanged();
            });
        } else {
            btnDelete.setVisibility(View.GONE);
        }

        return convertView;
    }

    // Указывает, можно ли выбрать дочерний элемент.
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    // Устанавливает новые даты недели.
    public void setWeekDates(List<String> newWeekDates) {
        this.weekDates = newWeekDates;
    }

    // Устанавливает новый план питания.
    public void setMealPlan(Map<String, Map<String, Recipe>> newMealPlan) {
        this.mealPlan = newMealPlan;
    }
}