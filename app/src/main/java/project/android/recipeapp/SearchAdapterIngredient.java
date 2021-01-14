package project.android.recipeapp;

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

public class SearchAdapterIngredient extends RecyclerView.Adapter<SearchAdapterIngredient.MyViewHolder> {


    private Context context;
    private List<Ingredient> mData;
    public static List<String> ingredients;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        LinearLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.img_ingr);
            textView = view.findViewById(R.id.txt_name_ingr);
            linearLayout = view.findViewById(R.id.root_linear);
        }
    }

    SearchAdapterIngredient (Context context, List<Ingredient> mData) {
        this.context = context;
        this.mData = mData;
        ingredients = new ArrayList<>();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Ingredient current = mData.get(position);
        holder.textView.setText(current.getNam());
        Glide.with(holder.imageView.getContext()).load(current.getThumbnail()).into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!current.isSelected()) {
                    ingredients.add(current.getNam());
                } else if (current.isSelected()) {
                    ingredients.remove(current.getNam());
                }
                current.setSelected();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
