package project.android.recipeapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import project.android.recipeapp.R;
import project.android.recipeapp.model.Ingredient;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private Context context;
    private List<Ingredient> data;
    public static List<String> ingredientsList;

    public static class IngredientViewHolder extends RecyclerView.ViewHolder {
        ImageView ingred_image;
        TextView ingredients;
        LinearLayout rootView;

        IngredientViewHolder(View view) {
            super(view);
            ingred_image = view.findViewById(R.id.img_ingr);
            ingredients = view.findViewById(R.id.txt_name_ingr);
            rootView = view.findViewById(R.id.root_linear);
        }
    }

    public IngredientAdapter(Context context, List<Ingredient> data) {
        this.context = context;
        this.data = data;
        ingredientsList = new ArrayList<>();
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ingredient_row,parent,false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        holder.ingredients.setText(data.get(position).getName());
        Glide.with(holder.ingred_image.getContext()).load(data.get(position).getImage()).into(holder.ingred_image);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
