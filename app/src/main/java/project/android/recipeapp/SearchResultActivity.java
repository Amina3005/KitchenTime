package project.android.recipeapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {

    private TextView ingredients_list;
    private RecyclerView rv;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Food> foodList = new ArrayList<>();
    private JSONArray array;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results_activity);

        ingredients_list = findViewById(R.id.ingredients_list_name);
        rv = findViewById(R.id.recycler_search_results);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        rv.setLayoutManager(new GridLayoutManager(this,2));
        String search = getString(SearchAdapterIngredient.ingredients);
        ingredients_list.setText(search);
        getResults(search);

    }

    public void getResults(String query) {
        swipeRefreshLayout.setRefreshing(true);
        String url = "https://api.spoonacular.com/recipes/findByIngredients?ingredients=" + query + "&number=30&apiKey=0b04dac1a42848bfa0e68732c13df794";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    array = response;
                    Log.i("the res loas is: ", String.valueOf(array));
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        foodList.add(new Food(jsonObject.getInt("id"),
                                jsonObject.optString("image"), jsonObject.getString("title")));
                    }
                    swipeRefreshLayout.setRefreshing(false);
                    FoodAdapter adapter = new FoodAdapter();
                    adapter.setMyFoodList(foodList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("the res error is:", error.toString());
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    public String getString (List<String> ingredientLst) {
        StringBuilder res = new StringBuilder(ingredientLst.get(0));
        for (int i = 1; i < ingredientLst.size(); i++) {
            res.append(" , ").append(ingredientLst.get(i));
        }
        return res.toString();
    }
}
