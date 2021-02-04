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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends Fragment implements View.OnClickListener{
    private RecyclerView recyclerView;
    public FoodAdapter adapter = new FoodAdapter();
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Food> myFoodList = new ArrayList<>();

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
        swipeRefreshLayout.setRefreshing(true);
        FoodApi foodApi = RetrofitClient.getFoodApi();
        Call<Result> resultCall = foodApi.searchList(s);
        resultCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(@NotNull Call<Result> call, @NotNull Response<Result> response) {
                searchList = Objects.requireNonNull(response.body()).getRes();
                for (int i = 0; i < searchList.size(); i++) {
                    String s  = searchList.get(i).getImage();
                    searchList.get(i).setImage("https://spoonacular.com/recipeImages/" + s);
                    response.body().setRes(searchList);
                }
                swipeRefreshLayout.setRefreshing(false);
                adapter.setMyFoodList(searchList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(@NotNull Call<Result> call, @NotNull Throwable t) {
                Log.e("the error is: ", t.toString());
            }
        });
    }

    public void loadRecipeData() {
        swipeRefreshLayout.setRefreshing(true);
        FoodApi foodApi = RetrofitClient.getFoodApi();
        Call<Result> call = foodApi.foodList();
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(@NotNull Call<Result> call, @NotNull Response<Result> response) {
                myFoodList = Objects.requireNonNull(response.body()).getResult();
                swipeRefreshLayout.setRefreshing(false);
                adapter.setMyFoodList(myFoodList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(@NotNull Call<Result> call, @NotNull Throwable t) {
                Log.e("daily point", "i've reached daily points");
            }
        });
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
