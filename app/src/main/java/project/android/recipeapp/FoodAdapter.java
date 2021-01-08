package project.android.recipeapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout containerView;
        public CardView cardView;
        public ImageView imageView;
        public TextView nameView;


        FoodViewHolder(View view) {
            super(view);
            containerView = view.findViewById(R.id.recipe_row);
            cardView = view.findViewById(R.id.recipe_card_view);
            imageView = view.findViewById(R.id.recipe_row_image_view);
            nameView = view.findViewById(R.id.recipe_row_text_view);

        }

    }

    private List<Food> mData = new ArrayList<>();


    public void setMyFoodList(List<Food> myFoodList) {
        this.mData = myFoodList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_row, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food current = mData.get(position);
        Glide.with(holder.imageView.getContext()).load(current.getImage())
                .placeholder(R.drawable.ic_baseline_image_not_supported_24).into(holder.imageView);
        holder.nameView.setText(current.getName());
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), FoodActivity.class);
            intent.putExtra("id", current.getId());
            intent.putExtra("Image", current.getImage());
            intent.putExtra("name", current.getName());
            view.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
