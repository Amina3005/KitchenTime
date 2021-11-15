package project.android.recipeapp.UI;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import project.android.recipeapp.FoodApi;
import project.android.recipeapp.R;
import project.android.recipeapp.RetrofitClient;
import project.android.recipeapp.adapters.IngredientAdapter;
import project.android.recipeapp.model.FoodDetail;
import project.android.recipeapp.model.Ingredient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodActivity extends AppCompatActivity {

    private ImageView foodImage;
    private TextView name, time, description;
    private List<Ingredient> ingrList = new ArrayList<>();
    private RecyclerView rv;
    Integer id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        foodImage = findViewById(R.id.food_image);
        name = findViewById(R.id.name_text_view);
        time = findViewById(R.id.time_text_view);
        description = findViewById(R.id.description_text_view);
        rv = findViewById(R.id.recycler_view_ingredient);
        rv.setLayoutManager(new GridLayoutManager(this,2));

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            foodImage.setImageResource(bundle.getInt("Image"));
            name.setText(bundle.getString("name"));
        }

        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);
        loadRecipe(id);

    }


    // logic!!! not here
    public void loadRecipe(int RecipeId) {
        FoodApi foodApi = RetrofitClient.getFoodApi();
        Call<FoodDetail> foodDetailCall = foodApi.foodDetail(RecipeId);

        foodDetailCall.enqueue(new Callback<FoodDetail>() {
            @Override
            public void onResponse(@NotNull Call<FoodDetail> call, @NotNull Response<FoodDetail> response) {
                Glide.with(foodImage.getContext()).
                        load(Objects.requireNonNull(response.body()).
                                getImage()).into(foodImage);
                name.setText(response.body().getTitle());
                time.setText(String.valueOf(response.body().getTime()));
                description.setText(response.body().getInstructions());
                try {
                    if (response.body().getInstructions().equals("")) {
                        throw new Exception("No Instructions");
                    } else
                        description.setText(Html.fromHtml(response.body().getInstructions()));
                } catch (Exception e) {
                    String msg = "Unfortunately, the recipe you were looking for not found, to view the original recipe click on the link below:"
                            + "<a href=" + response.body().getSpoonacularUrl() + ">"
                            + response.body().getSpoonacularUrl() + "</a>";
                    description.setMovementMethod(LinkMovementMethod.getInstance());
                    description.setText(Html.fromHtml(msg));
                }

                ingrList = response.body().getIngrList();
                IngredientAdapter myAdapter = new IngredientAdapter(getApplicationContext(),ingrList);
                rv.setAdapter(myAdapter);
            }

            @Override
            public void onFailure(@NotNull Call<FoodDetail> call, @NotNull Throwable t) {
                Log.e("RequestError", t.toString());
            }
        });
    }

}