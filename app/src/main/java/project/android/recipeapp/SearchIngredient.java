package project.android.recipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

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
    //private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    ArrayAdapter<String> arrayAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_by_ingredients, container, false);

        toolbar = view.findViewById(R.id.toolbar_search);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);

        //recyclerView = view.findViewById(R.id.recycler_search_view);
        //layoutManager = new LinearLayoutManager(getActivity());

        //IngredientAdapter ad = new IngredientAdapter(getActivity(),ingredientList);
        //recyclerView.setAdapter(ad);
        //recyclerView.setLayoutManager(layoutManager);

        chipGroup = view.findViewById(R.id.chip_group);
        searchBtn = view.findViewById(R.id.ingredient_search_btn);
        inputEditText = view.findViewById(R.id.text_input_et);






        inputEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Ingredient selected = (Ingredient) adapterView.getItemAtPosition(i);
                onItemSelected(selected);
            }
        });


        inputEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = inputEditText.getText().toString();
                if (!input.equals("")) {
                    searchByIngredients(input);
                    //SearchAdapterIngredient adapter = new SearchAdapterIngredient(getActivity(), ingredientList);
                    inputEditText.setAdapter(arrayAdapter);
                    //recyclerView.setAdapter(adapter);
                    //adapter.notifyDataSetChanged();
                } else
                    Toast.makeText(getActivity(), "Select ingredients", Toast.LENGTH_LONG).show();
            }
        });



        /*
        inputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputEditText.setEnabled(true);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String input = inputEditText.getText().toString();
                if (!input.equals("")) {
                    searchByIngredients(input);
                    SearchAdapterIngredient adapter = new SearchAdapterIngredient(getActivity(), ingredientList);
                    inputEditText.setAdapter(arrayAdapter);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } else
                    Toast.makeText(getActivity(), "Select ingredients", Toast.LENGTH_LONG).show();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                inputEditText.setEnabled(true);
            }
        });

         */


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> tmp = SearchAdapterIngredient.ingredients;
                if(tmp.isEmpty()){
                    Toast.makeText(getActivity(), "You must select at least one ingredient", Toast.LENGTH_LONG).show();
                }
                else{
                    Intent searchResultsIntent = new Intent(getActivity(), SearchResultActivity.class);
                    startActivity(searchResultsIntent);
                }
            }
        });

        return view;
    }


    public void searchByIngredients(String query) {
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

    public void onItemSelected (Ingredient ingredient) {
        Chip chip = new Chip(getActivity());
        chip.setText(ingredient.getNam());
        chip.setChipIcon(ContextCompat.getDrawable(getActivity(), Integer.parseInt(ingredient.getThumbnail())));
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
}
