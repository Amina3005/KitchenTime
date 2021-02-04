package project.android.recipeapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FoodApi {

    @GET("recipes/random?number=50&")
    Call<Result> foodList();

    @GET("recipes/search?&number=10&")
    Call<Result> searchList(@Query("query") String query);

    @GET("recipes/{id}/information?includeNutrition=false&")
    Call<FoodDetail> foodDetail(@Path("id") int id);

    @GET("food/ingredients/autocomplete?number=5&")
    Call<List<Ingredient>> ingredientList(@Query("query") String query);

    @GET("recipes/findByIngredients?&number=5&")
    Call<List<Food>> ingredients(@Query("ingredients") String ingredients);

}
