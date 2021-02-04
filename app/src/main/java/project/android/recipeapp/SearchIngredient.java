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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  SearchIngredient extends Fragment implements RecyclerItemSelectedListener,View.OnClickListener {

    private Toolbar toolbar;
    private ChipGroup chipGroup;
    private Button searchBtn;
    private AutoCompleteTextView inputEditText;
    private List<String> ingredientList = new ArrayList<>();

    private RecyclerView rv;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Food> foodList = new ArrayList<>();
    private JSONArray array;

    ArrayAdapter<String> arrayAdapter;

    String string;
    String sea;

    List<String> chipList = new ArrayList<>();

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

        string = inputEditText.getText().toString();

        return view;
    }

    String selected;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputEditText.setOnItemClickListener((adapterView, view1, i, l) -> {
            selected = (String) adapterView.getItemAtPosition(i);
            SearchIngredient.this.onItemSelected(selected);
            inputEditText.setText("");
            chipList.add(selected);
        });

        inputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                string = charSequence.toString();
                if (!string.equals("")) {
                    loadSuggestions(string);
                }else
                    Toast.makeText(getActivity(), "Select something...", Toast.LENGTH_LONG).show();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        searchBtn.setOnClickListener(v -> {
            InputMethodManager in = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(inputEditText.getWindowToken(),0);
            sea = getSelectedChips(chipGroup);
            getResults(sea);
        });

        if (savedInstanceState != null) {
            string = savedInstanceState.getString("name");
            sea = savedInstanceState.getString("results");
            inputEditText.setText(string);

            loadSuggestions(string);
            getResults(sea);
            chipList = savedInstanceState.getStringArrayList("chip");
            for (int i = 0; i < chipList.size(); i++) {
                String s = chipList.get(i);
                onItemSelected(s);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("name",string);
        outState.putString("results", sea);
        outState.putStringArrayList("chip", (ArrayList<String>) chipList);
        super.onSaveInstanceState(outState);
    }

    public void loadSuggestions(String query) {
        FoodApi foodApi = RetrofitClient.getFoodApi();
        Call<List<Ingredient>> stringCall = foodApi.ingredientList(string);
        stringCall.enqueue(new Callback<List<Ingredient>>() {
            @Override
            public void onResponse(@NotNull Call<List<Ingredient>> call, @NotNull Response<List<Ingredient>> response) {
                List<Ingredient> names = response.body();
                ingredientList.clear();
                for (int i = 0; i < Objects.requireNonNull(names).size(); i++) {
                    ingredientList.add(names.get(i).getNames());
                }
                arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line,ingredientList);
                inputEditText.setAdapter(arrayAdapter);
                inputEditText.setThreshold(1);
            }

            @Override
            public void onFailure(@NotNull Call<List<Ingredient>> call, @NotNull Throwable t) {
                Log.e("the error with" , t.toString());
            }
        });
    }

    public void onItemSelected (String s) {
        Chip chip = new Chip(requireActivity());
        chip.setText(s);
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

    public void getResults(String ingredients) {
        swipeRefreshLayout.setRefreshing(true);
        FoodApi foodApi = RetrofitClient.getFoodApi();
        Call<List<Food>> ingredientLst = foodApi.ingredients(sea);
        ingredientLst.enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(@NotNull Call<List<Food>> call, @NotNull Response<List<Food>> response) {
                List<Food> fodies = response.body();
                foodList.clear();
                for (int i = 0; i < Objects.requireNonNull(fodies).size(); i++) {
                    foodList.add(fodies.get(i));
                }
                swipeRefreshLayout.setRefreshing(false);
                FoodAdapter foodAdapter = new FoodAdapter();
                foodAdapter.setMyFoodList(foodList);
                rv.setAdapter(foodAdapter);
            }
            @Override
            public void onFailure(@NotNull Call<List<Food>> call, @NotNull Throwable t) {
                Log.e("the res error is:", t.toString());
            }
        });


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
