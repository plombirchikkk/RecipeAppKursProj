package com.example.recipeappkurs.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.recipeappkurs.R;
import com.example.recipeappkurs.db.DBHelper;
import com.example.recipeappkurs.model.Recipe;

import java.util.List;

public class RecipeAdapter extends ArrayAdapter<Recipe> {
    private final DBHelper dbHelper;
    private final boolean showDetailsOnClick;
    private final boolean showDeleteButton;
    private final boolean showAddButton;
    private final OnRecipeSelectedListener onRecipeSelectedListener;

    public interface OnRecipeSelectedListener {
        void onRecipeSelected(int recipeId);
    }

    public RecipeAdapter(Context context, List<Recipe> recipes, DBHelper dbHelper,
                         boolean showDetailsOnClick, boolean showDeleteButton,
                         boolean showAddButton, OnRecipeSelectedListener onRecipeSelectedListener) {
        super(context, 0, recipes);
        this.dbHelper = dbHelper;
        this.showDetailsOnClick = showDetailsOnClick;
        this.showDeleteButton = showDeleteButton;
        this.showAddButton = showAddButton;
        this.onRecipeSelectedListener = onRecipeSelectedListener;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_recipe, parent, false);
            holder = new ViewHolder();
            holder.tvRecipeName = convertView.findViewById(R.id.tvRecipeName);
            holder.btnDelete = convertView.findViewById(R.id.btnDelete);
            holder.btnAdd = convertView.findViewById(R.id.btnAdd);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Recipe recipe = getItem(position);
        assert recipe != null;
        holder.tvRecipeName.setText(recipe.getRecipeName());

        if (showDetailsOnClick) {
            convertView.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(recipe.getRecipeName());
                builder.setMessage(recipe.getDetails());
                builder.setPositiveButton("OK", null);
                builder.show();
            });
        } else {
            convertView.setOnClickListener(null);
        }

        holder.btnDelete.setVisibility(showDeleteButton ? View.VISIBLE : View.GONE);
        if (showDeleteButton) {
            holder.btnDelete.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Удалить рецепт");
                builder.setMessage("Вы уверены, что хотите удалить рецепт \"" + recipe.getRecipeName() + "\"?");
                builder.setPositiveButton("Да", (dialog, which) -> {
                    dbHelper.deleteRecipe(recipe.getId());
                    remove(recipe);
                    notifyDataSetChanged();
                    Toast.makeText(getContext(), "Рецепт удален", Toast.LENGTH_SHORT).show();
                });
                builder.setNegativeButton("Нет", null);
                builder.show();
            });
        } else {
            holder.btnDelete.setOnClickListener(null);
        }

        holder.btnAdd.setVisibility(showAddButton ? View.VISIBLE : View.GONE);
        if (showAddButton) {
            holder.btnAdd.setOnClickListener(v -> {
                if (onRecipeSelectedListener != null) {
                    onRecipeSelectedListener.onRecipeSelected(recipe.getId());
                }
            });
        } else {
            holder.btnAdd.setOnClickListener(null);
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView tvRecipeName;
        ImageButton btnDelete;
        ImageButton btnAdd;
    }
}