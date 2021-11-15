package project.android.recipeapp.model;

import com.google.gson.annotations.SerializedName;

public class Ingredient {

    @SerializedName("originalString")
    private String name;
    @SerializedName("image")
    private String image;
    @SerializedName("name")
    private String names;

    private static final String BASE_URL = "https://spoonacular.com/cdn/ingredients_100x100/";

    public Ingredient(String name, String image, String names) {
        this.name = name;
        this.image = BASE_URL + image;
        this.names = names;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return BASE_URL + image;
    }

    public String getNames() {
        return names;
    }

}
