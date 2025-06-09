package com.example.recipeappkurs.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.recipeappkurs.model.Recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

// Класс для работы с базой данных рецептов.
public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "recipes.db";
    private static final int DATABASE_VERSION = 4;

    private static final String TABLE_RECIPES = "recipes";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_INGREDIENTS = "ingredients";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_INSTRUCTIONS = "instructions";
    private static final String COLUMN_PREP_TIME = "prep_time";
    private static final String COLUMN_DIFFICULTY = "difficulty";

    // Конструктор помощника базы данных.
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Создание таблиц и добавление начальных данных.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RECIPES_TABLE = "CREATE TABLE " + TABLE_RECIPES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TYPE + " TEXT NOT NULL CHECK(" + COLUMN_TYPE + " IN ('Dish', 'Drink')),"
                + COLUMN_NAME + " TEXT NOT NULL UNIQUE,"
                + COLUMN_INGREDIENTS + " TEXT NOT NULL,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_INSTRUCTIONS + " TEXT,"
                + COLUMN_PREP_TIME + " TEXT,"
                + COLUMN_DIFFICULTY + " TEXT" + ")";
        db.execSQL(CREATE_RECIPES_TABLE);

        // Добавление рецептов по умолчанию.
        db.execSQL("INSERT INTO " + TABLE_RECIPES + " (type, name, ingredients, description, instructions) VALUES ('Dish', 'Спагетти болоньезе', 'Спагетти, фарш говяжий, томатный соус, лук, чеснок, оливковое масло, соль, перец', 'Классическое итальянское блюдо с ароматным мясным соусом.', 'Отварить спагетти согласно инструкции на упаковке. В сковороде обжарить лук и чеснок на оливковом масле, добавить фарш, обжарить до готовности. Добавить томатный соус, соль и перец. Тушить 15 минут. Подать соус с отваренными спагетти.')");
        db.execSQL("INSERT INTO " + TABLE_RECIPES + " (type, name, ingredients, description, instructions) VALUES ('Dish', 'Куриный стир-фрай', 'Куриная грудка, овощи (болгарский перец, брокколи, морковь), соевый соус, чеснок, имбирь, растительное масло', 'Быстрое и полезное блюдо с нежным куриным мясом и хрустящими овощами.', 'Нарезать курицу кубиками. Разогреть масло в воке, добавить чеснок и имбирь, затем курицу. Жарить до готовности курицы. Добавить овощи и соевый соус, жарить до готовности овощей.')");
        db.execSQL("INSERT INTO " + TABLE_RECIPES + " (type, name, ingredients, description, instructions, prep_time, difficulty) VALUES ('Drink', 'Маргарита', 'Текила, лимонный сок, трипл сек, соль для ободка', 'Освежающий коктейль, идеальный для лета.', 'Обмакнуть край бокала в соль. В шейкере смешать текилу, лимонный сок и трипл сек с льдом. Взбить и перелить в бокал. Украсить долькой лимона.', '5 минут', 'Легко')");
        db.execSQL("INSERT INTO " + TABLE_RECIPES + " (type, name, ingredients, description, instructions, prep_time, difficulty) VALUES ('Drink', 'Пина Колада', 'Ром, ананасный сок, кокосовый крем, лед', 'Тропический коктейль, напоминающий отдых на пляже.', 'В блендере смешать ром, ананасный сок, кокосовый крем и лед. Взбить до однородности. Подать в бокале, украсив долькой ананаса и вишенкой.', '5 минут', 'Легко')");
        Log.d("DBHelper", "Inserting default recipes");

        // Создание таблицы для планов питания.
        db.execSQL("CREATE TABLE meal_plans (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "date TEXT NOT NULL," +
                "meal_time TEXT NOT NULL," +
                "recipe_id INTEGER NOT NULL," +
                "UNIQUE(date, meal_time)," +
                "FOREIGN KEY(recipe_id) REFERENCES recipes(id)" +
                ")");
    }

    // Обновление структуры базы данных.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 4) {
            // Создать временную таблицу с UNIQUE на name
            db.execSQL("CREATE TABLE recipes_new (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "type TEXT NOT NULL CHECK(type IN ('Dish', 'Drink'))," +
                    "name TEXT NOT NULL UNIQUE," +
                    "ingredients TEXT NOT NULL," +
                    "description TEXT," +
                    "instructions TEXT," +
                    "prep_time TEXT," +
                    "difficulty TEXT" +
                    ")");
            // Копировать данные, удаляя дубликатов
            db.execSQL("INSERT INTO recipes_new (id, type, name, ingredients, description, instructions, prep_time, difficulty) " +
                    "SELECT MIN(id), type, name, ingredients, description, name, prep_time, difficulty FROM " + TABLE_RECIPES + " GROUP BY name");
            // Удалить старую таблицу
            db.execSQL("DROP TABLE " + TABLE_RECIPES);
            // Переименовать новую таблицу
            db.execSQL("ALTER TABLE recipes_new RENAME TO " + TABLE_RECIPES);
            // Обновить meal_plans
            db.execSQL("DROP TABLE IF EXISTS meal_plans");
            db.execSQL("CREATE TABLE meal_plans (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "date TEXT NOT NULL," +
                    "meal_time TEXT NOT NULL," +
                    "recipe_id INTEGER NOT NULL," +
                    "UNIQUE(date, meal_time)," +
                    "FOREIGN KEY(recipe_id) REFERENCES recipes(id)" +
                    ")");
        }
    }

    // Удаление дубликатов рецептов
    public void removeDuplicateRecipes() {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("DBHelper", "Removing duplicate recipes");
        db.execSQL("DELETE FROM " + TABLE_RECIPES + " WHERE id NOT IN (SELECT MIN(id) FROM " + TABLE_RECIPES + " GROUP BY name)");
        db.close();
    }

    // Добавление или обновление записи плана питания.
    public void setMealPlanEntry(String date, String meal_time, int recipe_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", date);
        values.put("meal_time", meal_time);
        values.put("recipe_id", recipe_id);
        db.replace("meal_plans", null, values);
        db.close();
    }

    // Получение планов питания для списка дат.
    public Map<String, Map<String, Recipe>> getMealPlanForDates(List<String> dates) {
        Map<String, Map<String, Recipe>> mealPlan = new HashMap<>();
        for (String date : dates) {
            mealPlan.put(date, new HashMap<>());
            for (String mealTime : Arrays.asList("Завтрак", "Обед", "Ужин")) {
                Objects.requireNonNull(mealPlan.get(date)).put(mealTime, null);
            }
        }
        SQLiteDatabase db = this.getReadableDatabase();
        StringBuilder sb = new StringBuilder("SELECT date, meal_time, recipe_id FROM meal_plans WHERE date IN (");
        for (int i = 0; i < dates.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append("?");
        }
        sb.append(")");
        Cursor cursor = db.rawQuery(sb.toString(), dates.toArray(new String[0]));
        while (cursor.moveToNext()) {
            String date = cursor.getString(0);
            String mealTime = cursor.getString(1);
            int recipeId = cursor.getInt(2);
            Recipe recipe = getRecipeById(recipeId);
            if (recipe != null && mealPlan.containsKey(date) && Objects.requireNonNull(mealPlan.get(date)).containsKey(mealTime)) {
                Objects.requireNonNull(mealPlan.get(date)).put(mealTime, recipe);
            }
        }
        cursor.close();
        return mealPlan;
    }

    // Удаление записи плана питания.
    public void deleteMealPlanEntry(String date, String meal_time) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("meal_plans", "date = ? AND meal_time = ?", new String[]{date, meal_time});
        db.close();
    }

    // Добавление нового рецепта.
    public void addRecipe(String type, String name, String ingredients, String description,
                          String instructions, String prepTime, String difficulty) {
        Log.d("DBHelper", "Adding recipe: " + name);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_INGREDIENTS, ingredients);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_INSTRUCTIONS, instructions);
        values.put(COLUMN_PREP_TIME, prepTime);
        values.put(COLUMN_DIFFICULTY, difficulty);
        db.insert(TABLE_RECIPES, null, values);
        db.close();
    }

    // Получение всех рецептов.
    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_RECIPES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Recipe recipe = getRecipeFromCursor(cursor);
                recipes.add(recipe);
            } while (cursor.moveToNext());
        }
        cursor.close();
        Log.d("DBHelper", "Retrieved " + recipes.size() + " recipes");
        return recipes;
    }

    // Получение рецепта по ID.
    public Recipe getRecipeById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECIPES, null, COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        Recipe recipe = null;
        if (cursor.moveToFirst()) {
            recipe = getRecipeFromCursor(cursor);
        }
        cursor.close();
        return recipe;
    }

    // Проверка существования рецепта по имени.
    public boolean recipeExists(String name) {
        String selectQuery = "SELECT 1 FROM " + TABLE_RECIPES + " WHERE " + COLUMN_NAME + " = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{name});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists; // Возвращает true, если рецепт существует
    }

    // Удаление рецепта по ID.
    public void deleteRecipe(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RECIPES, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Поиск рецептов по имени.
    public List<Recipe> searchRecipesByName(String query) {
        List<Recipe> recipes = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_RECIPES +
                " WHERE LOWER(" + COLUMN_NAME + ") LIKE LOWER(?)";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{"%" + query.toLowerCase() + "%"});
        if (cursor.moveToFirst()) {
            do {
                recipes.add(getRecipeFromCursor(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return recipes;
    }

    // Преобразование данных курсора в объект рецепта.
    private Recipe getRecipeFromCursor(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
        String type = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
        String ingredients = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INGREDIENTS));
        String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
        String instructions = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INSTRUCTIONS));
        String prepTime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PREP_TIME));
        String difficulty = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIFFICULTY));
        return new Recipe(id, type, name, ingredients, description, instructions, prepTime, difficulty);
    }
}