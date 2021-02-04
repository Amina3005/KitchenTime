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


    public FoodDetail (String image, String title, Integer time, String instructions) {
        this.image = image;
        this.title = title;
        this.time = time;
        this.instructions = instructions;
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

    @SerializedName("extendedIngredients")
    private List<Ingredient> ingrList = null;

    public List<Ingredient> getIngrList() {
        return ingrList;
    }

}
