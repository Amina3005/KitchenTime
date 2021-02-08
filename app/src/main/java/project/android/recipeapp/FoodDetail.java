package project.android.recipeapp;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class FoodDetail {

    @SerializedName("image")
    private String image;
    @SerializedName("title")
    private String title;
    @SerializedName("readyInMinutes")
    private Integer time;
    @SerializedName("instructions")
    private String instructions;
    @SerializedName("spoonacularSourceUrl")
    private String spoonacularUrl;


    public FoodDetail (String image, String title, Integer time, String instructions, String spoonacularUrl) {
        this.image = image;
        this.title = title;
        this.time = time;
        this.instructions = instructions;
        this.spoonacularUrl = spoonacularUrl;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public Integer getTime() {
        return time;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getSpoonacularUrl() { return spoonacularUrl; }

    @SerializedName("extendedIngredients")
    private List<Ingredient> ingrList = null;

    public List<Ingredient> getIngrList() {
        return ingrList;
    }

}
