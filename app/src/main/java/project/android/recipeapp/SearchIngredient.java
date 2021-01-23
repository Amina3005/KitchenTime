package project.android.recipeapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchIngredient extends Fragment implements RecyclerItemSelectedListener,View.OnClickListener {

    private Toolbar toolbar;
    private ChipGroup chipGroup;
    private Button searchBtn;
    private AutoCompleteTextView inputEditText;
    private List<String> ingredientList = new ArrayList<>();


    ArrayAdapter<String> arrayAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_by_ingredients, container, false);

        toolbar = view.findViewById(R.id.toolbar_search);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);


        chipGroup = view.findViewById(R.id.chip_group);
        searchBtn = view.findViewById(R.id.ingredient_search_btn);
        inputEditText = view.findViewById(R.id.text_input_et);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        rv = view.findViewById(R.id.recycler_search_results);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));


        inputEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = (String) adapterView.getItemAtPosition(i);
                onItemSelected(selected);
                inputEditText.setText("");
            }
        });


        inputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                loadSuggestions(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sea = getSelectedChips(chipGroup);
                getResults(sea);
            }
        });

        return view;
    }


    public void loadSuggestions(String query) {
        String url = "https://api.spoonacular.com/food/ingredients/autocomplete?query=" + query + "&number=5&apiKey=0b04dac1a42848bfa0e68732c13df794";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONArray ingrArr = response;
                            Log.i("the load inrg error is:", String.valueOf(ingrArr));
                            ingredientList.clear();
                            for (int i = 0; i < ingrArr.length(); i++) {
                                JSONObject jsonObject = ingrArr.getJSONObject(i);
                                ingredientList.add(jsonObject.getString("name"));
                                        //(new Ingredient(jsonObject.getString("name"),jsonObject.optString("image")));
                            }

                            arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line,ingredientList);
                            inputEditText.setAdapter(arrayAdapter);
                            inputEditText.setThreshold(1);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("the error with" , error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void onItemSelected (String s) {
        Chip chip = new Chip(getActivity());
        chip.setText(s);
        //chip.setChipIcon(ContextCompat.getDrawable(getActivity(), Integer.parseInt(ingredient.getThumbnail())));
        chip.setCloseIconVisible(true);
        chip.setCheckable(false);
        chip.setClickable(false);
        chip.setOnCloseIconClickListener(this);
        chipGroup.addView(chip);

    }


    @Override
    public void onClick(View view) {
        Chip chip = (Chip) view;
        chipGroup.removeView(chip);
    }




    private RecyclerView rv;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Food> foodList = new ArrayList<>();
    private JSONArray array;

    public void getResults(String ingredients) {
        swipeRefreshLayout.setRefreshing(true);
        String url = "https://api.spoonacular.com/recipes/findByIngredients?ingredients=" + ingredients + "&number=5&apiKey=0b04dac1a42848bfa0e68732c13df794";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    array = response;
                    Log.i("the res loas is: ", String.valueOf(array));
                    foodList.clear();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        foodList.add(new Food(jsonObject.getInt("id"),
                                jsonObject.optString("image"), jsonObject.getString("title")));
                    }
                    swipeRefreshLayout.setRefreshing(false);
                    FoodAdapter adapter = new FoodAdapter();
                    adapter.setMyFoodList(foodList);
                    rv.setAdapter(adapter);
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


    public String getSelectedChips (ChipGroup c) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip ch = (Chip) chipGroup.getChildAt(i);
            String s = ch.getText().toString();
            res.append(" , ").append(s);

        }
        return res.toString();
    }



}
