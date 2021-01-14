package project.android.recipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FoodActivity extends AppCompatActivity {

    private ImageView foodImage;
    private TextView name, time, description;
    private List<Ingredient> ingrList = new ArrayList<Ingredient>();
    private RecyclerView rv;

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
        final int id = intent.getIntExtra("id",0);
        loadRecipe(id);

    }


    public void loadRecipe(final int recipeId) {
        String url = "https://api.spoonacular.com/recipes/" + recipeId + "/information?includeNutrition=false&apiKey=0b04dac1a42848bfa0e68732c13df794";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                Glide.with(foodImage.getContext()).load(response.getString("image")).into(foodImage);

                name.setText(response.getString("title"));
                time.setText(String.valueOf(response.getInt("readyInMinutes")));
                description.setText(response.getString("instructions"));
                try {
                    if (response.getString("instructions").equals("")) {
                        throw new Exception("No Instructions");
                    } else
                        description.setText(Html.fromHtml(response.getString("instructions")));
                } catch (Exception e) {
                    Log.e("Instructionserror", e.toString());
                }

                JSONArray ingredientArray = response.getJSONArray("extendedIngredients");
                for (int i = 0; i < ingredientArray.length(); i++) {
                    JSONObject jsonObject = ingredientArray.getJSONObject(i);
                    ingrList.add(new Ingredient(jsonObject.optString("originalString"), jsonObject.optString("image")));
                }
                IngredientAdapter myAdapter = new IngredientAdapter(getApplicationContext(),ingrList);
                rv.setAdapter(myAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Log.e("RequestError", error.toString()));

        requestQueue.add(request);
    }

}