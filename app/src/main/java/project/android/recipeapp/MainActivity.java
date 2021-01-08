package project.android.recipeapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private RecyclerView recyclerView;
    private FoodAdapter adapter = new FoodAdapter();
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Food> myFoodList = new ArrayList<>();

    private TextView  emptyView;
    public ImageButton searchImg;
    public List<Food> searchList;
    private EditText searchEt;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        swipeRefreshLayout = findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(this::loadRecipeData);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        loadRecipeData();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchImg = findViewById(R.id.main_search_btn);
        emptyView = findViewById(R.id.empty_view);

        searchEt =  findViewById(R.id.main_search_et);


        searchImg.setOnClickListener(this);
        searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    if (!textView.getText().toString().equals("")) {
                        emptyView.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(true);
                        recyclerView.setAlpha(0);
                        searchRecipe(textView.getText().toString());
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Type something...", Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });

    }


    public void searchRecipe (String query) {
        searchList = new ArrayList<Food>();
        swipeRefreshLayout.setRefreshing(true);
        String URL = " https://api.spoonacular.com/recipes/search?query=" + query + "&number=10&apiKey=0b04dac1a42848bfa0e68732c13df794";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray searchArr = response.getJSONArray("results");
                    Log.i("the search res is", String.valueOf(searchArr));

                    for (int i = 0; i < searchArr.length(); i++) {
                        JSONObject jsonObject = searchArr.getJSONObject(i);
                        searchList.add(new Food(jsonObject.getInt("id"),
                                "https://spoonacular.com/recipeImages/" + jsonObject.optString("image"),
                                jsonObject.getString("title")));
                    }
                    swipeRefreshLayout.setRefreshing(false);
                    if (searchList.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                    } else {
                        emptyView.setVisibility(View.GONE);
                        adapter.setMyFoodList(searchList);
                        recyclerView.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> Log.e("the error is: ", error.toString()));
        requestQueue.add(jsonObjectRequest);
    }

    public void loadRecipeData() {
        swipeRefreshLayout.setRefreshing(true);
        String url = "https://api.spoonacular.com/recipes/random?number=50&apiKey=0b04dac1a42848bfa0e68732c13df794";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray recipeArray = response.getJSONArray("recipes");
                    for (int i = 0; i < recipeArray.length(); i++) {
                        JSONObject recipes = recipeArray.getJSONObject(i);
                        myFoodList.add(new Food(recipes.getInt("id"), recipes.optString("image"),
                                recipes.getString("title")));

                    }
                    swipeRefreshLayout.setRefreshing(false);
                    adapter.setMyFoodList(myFoodList);

                } catch (JSONException e) {
                    Log.e("error", "recipe's error");
                }
            }
        }, error -> Log.e("error", "Recipe list error"));
        requestQueue.add(request);
    }


    @Override
    public void onClick(View view) {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(searchEt.getWindowToken(),0);
        if (view == searchImg) {
            if (!searchEt.getText().toString().equals("")) {
                swipeRefreshLayout.setRefreshing(true);
                searchRecipe(searchEt.getText().toString());
            }
            else
                Toast.makeText(getApplicationContext(), "Type something...", Toast.LENGTH_LONG).show();
        }
    }

}