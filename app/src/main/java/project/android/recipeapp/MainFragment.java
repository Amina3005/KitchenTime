package project.android.recipeapp;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
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

public class MainFragment extends Fragment implements View.OnClickListener{
    private RecyclerView recyclerView;
    private FoodAdapter adapter = new FoodAdapter();
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Food> myFoodList = new ArrayList<>();

    private TextView emptyView;
    public ImageButton searchImg;
    public List<Food> searchList;
    private EditText searchEt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);

        recyclerView = view.findViewById(R.id.recycler_view);
        swipeRefreshLayout = view.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            MainFragment.this.loadRecipeData();
        });
        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        loadRecipeData();

        searchImg = view.findViewById(R.id.main_search_btn);
        emptyView = view.findViewById(R.id.empty_view);

        searchEt =  view.findViewById(R.id.main_search_et);

        searchImg.setOnClickListener(this);



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchEt.setEnabled(true);

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String s = searchEt.getText().toString();
                if (s != "") {
                    searchRecipe(s);
                } else
                    Toast.makeText(getActivity(), "Type something...", Toast.LENGTH_LONG).show();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchEt.setEnabled(true);

            }
        });




    }




    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("title",searchEt.getText().toString());
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null) {
            String a = savedInstanceState.getString("title");
            searchEt.setText(a);
        }
    }

    public void searchRecipe (String query) {
        searchList = new ArrayList<Food>();
        swipeRefreshLayout.setRefreshing(true);
        String URL = " https://api.spoonacular.com/recipes/search?query=" + query + "&number=10&apiKey=0b04dac1a42848bfa0e68732c13df794";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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
        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(searchEt.getWindowToken(),0);
        if (view == searchImg) {
            if (!searchEt.getText().toString().equals("")) {
                swipeRefreshLayout.setRefreshing(true);
                searchRecipe(searchEt.getText().toString());
            }
            else
                Toast.makeText(getActivity(), "Type something...", Toast.LENGTH_LONG).show();
        }
    }
}
