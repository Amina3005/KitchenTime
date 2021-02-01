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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainFragment extends Fragment implements View.OnClickListener{
    private RecyclerView recyclerView;
    public FoodAdapter adapter = new FoodAdapter();
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Food> myFoodList = new ArrayList<>();

    private TextView emptyView;
    public ImageButton searchImg;
    public List<Food> searchList ;
    public EditText searchEt;

    String s;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);

        recyclerView = view.findViewById(R.id.recycler_view);
        swipeRefreshLayout = view.findViewById(R.id.swipe_layout);

        searchImg = view.findViewById(R.id.main_search_btn);
        emptyView = view.findViewById(R.id.empty_view);

        searchEt =  view.findViewById(R.id.main_search_et);
        s = searchEt.getText().toString();

        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                s = charSequence.toString();
                if (!s.equals("")) {
                    searchRecipe(s);
                } else
                    Toast.makeText(getActivity(), "Type something...", Toast.LENGTH_LONG).show();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        searchImg.setOnClickListener(this);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        swipeRefreshLayout.setOnRefreshListener(MainFragment.this::loadRecipeData);


        if (savedInstanceState != null) {
            s = savedInstanceState.getString("title");
            searchEt.setText(s);
            searchRecipe(s);
        } else
            loadRecipeData();

    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("title",s);
        super.onSaveInstanceState(outState);
    }


    public void searchRecipe (String query) {
        searchList = new ArrayList<>();
        swipeRefreshLayout.setRefreshing(true);
        String URL = " https://api.spoonacular.com/recipes/search?query=" + query + "&number=10&apiKey=0b04dac1a42848bfa0e68732c13df794";
        RequestQueue requestQueue = Volley.newRequestQueue(requireActivity());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, response -> {
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
        }, error -> Log.e("the error is: ", error.toString()));
        requestQueue.add(jsonObjectRequest);
    }


    public void loadRecipeData() {
        swipeRefreshLayout.setRefreshing(true);
        String url = "https://api.spoonacular.com/recipes/random?number=50&apiKey=0b04dac1a42848bfa0e68732c13df794";
        RequestQueue requestQueue = Volley.newRequestQueue(requireActivity());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONArray recipeArray = response.getJSONArray("recipes");
                for (int i = 0; i < recipeArray.length(); i++) {
                    JSONObject recipes = recipeArray.getJSONObject(i);
                    myFoodList.add(new Food(recipes.getInt("id"), recipes.optString("image"),
                            recipes.getString("title")));

                }
                swipeRefreshLayout.setRefreshing(false);
                adapter.setMyFoodList(myFoodList);
                recyclerView.setAdapter(adapter);
            } catch (JSONException e) {
                Log.e("error", "recipe's error");
            }
        }, error -> Log.e("daily point", "i've reached daily points"));
        requestQueue.add(request);
    }

    @Override
    public void onClick(View view) {
        InputMethodManager in = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(searchEt.getWindowToken(),0);
        if (view == searchImg) {
            if (!s.equals("")) {
                swipeRefreshLayout.setRefreshing(true);
                searchRecipe(s);
            }
            else
                Toast.makeText(getActivity(), "Type something...", Toast.LENGTH_LONG).show();
        }
    }

}
