# 🥗 <span style="color:#4CAF50">Recipe App Kurs</span>

## 📋 <span style="color:#2196F3">Обзор</span>
Это **Android-приложение** для управления рецептами и планирования питания. Пользователи могут:
- Добавлять, искать и удалять рецепты
- Создавать план питания на неделю

Приложение использует локальную базу данных **SQLite** и построено по модульной архитектуре с использованием `Use Case`, адаптеров и `Activity`.

---

## 🍲 <span style="color:#FF9800">Функционал</span>

### 📌 Управление рецептами
- ➕ Добавление новых рецептов (блюда или напитки)
- 🔍 Поиск рецептов по названию
- 📄 Просмотр деталей рецепта
- 🗑️ Удаление рецептов с подтверждением

### 📆 Планирование питания
- 🗓️ Организация питания по дням недели и времени суток (завтрак, обед, ужин)
- 📌 Назначение рецептов на определённые приёмы пищи
- 🔄 Переключение между неделями
- ❌ Удаление записей из плана


### 🥘 Стандартные рецепты
- Спагетти болоньезе 🍝
- Куриный стир-фрай 🍗
- Маргарита 🍸
- Пина Колада 🍹

---

## 🧱 <span style="color:#9C27B0">Структура проекта</span>

### 📁 Java-классы

#### Активности
- `HomeActivity.java`: Главный экран
- `MainActivity.java`: Список рецептов, поиск, добавление
- `AddRecipeActivity.java`: Форма добавления рецепта
- `MealPlanActivity.java`: План питания на неделю
- `SelectRecipeActivity.java`: Выбор рецепта для плана
- `BaseActivity.java`: Базовый класс с ActionBar

#### Модель
- `Recipe.java`: Модель рецепта

#### Use Cases
- `AddRecipe.java`: Добавление рецепта
- `EnsureDefaultRecipes.java`: Стандартные рецепты
- `GetAllRecipes.java`: Получение всех рецептов
- `GetRecipeById.java`: Получение рецепта по ID
- `LoadMealPlan.java`: Загрузка плана питания
- `SearchRecipes.java`: Поиск по названию
- `SetMealPlanEntry.java`: Установка записи в плане питания

#### Адаптеры
- `RecipeAdapter.java`: Отображение рецептов в `ListView`
- `MealPlanAdapter.java`: План питания в `ExpandableListView`

---

## 🖼️ <span style="color:#00BCD4">XML-макеты</span>

- `activity_home.xml`: Главный экран
- `activity_main.xml`: Список рецептов + поиск
- `activity_add_recipe.xml`: Форма добавления рецепта
- `activity_meal_plan.xml`: План питания
- `activity_select_recipe.xml`: Выбор рецепта
- `list_item_recipe.xml`: Элемент списка рецептов
- `list_item_meal_plan.xml`: Элемент плана питания

---

## 📦 <span style="color:#795548">Зависимости</span>

- **SQLite** через `DBHelper`
- **Android SDK**:
  - `AppCompatActivity`
  - `ListView`
  - `ExpandableListView`
  - `SearchView`
  - `SharedPreferences`

---

## ⚙️ <span style="color:#607D8B">Установка</span>

## ⚙️ <span style="color:#607D8B">Требования</span>

- 💻 **Android Studio**
- 📦 **Android SDK** (совместимый уровень API)
- ☕ **Java 8** или выше

---

## ▶️ <span style="color:#FF5722">Использование</span>

### 🏠 <span style="color:#4CAF50">Главный экран</span>
- 🔄 Переход к разделам: **Рецепты**, **Питание**, **Аккаунт**

### 📖 <span style="color:#03A9F4">Рецепты</span>
- 🔍 Поиск рецептов по названию  
- 👁️ Просмотр подробной информации  
- ➕ Добавление новых рецептов  
- 🗑️ Удаление рецептов с подтверждением

### 🍽️ <span style="color:#9C27B0">План питания</span>
- 📅 Выбор рецептов по дням недели и времени суток (завтрак, обед, ужин)  
- ⏪⏩ Навигация между неделями  
- ❌ Удаление записей из плана

---

## 📝 <span style="color:#3F51B5">Примечания</span>

- 🌐 Интерфейс и предустановленные рецепты — **на русском языке**
- 💾 Используется `DBHelper` для хранения данных.

---

## 👩‍💻 <span style="color:#009688">Автор</span>

**Соломатова Милена Юрьевна**
