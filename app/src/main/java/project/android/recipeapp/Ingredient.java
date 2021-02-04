package project.android.recipeapp;

import com.google.gson.annotations.SerializedName;

public class Ingredient {

    @SerializedName("originalString")
    private String name;
    @SerializedName("image")
    private String image;
    @SerializedName("name")
    private String names;

    public  Ingredient(String name, String image,String names) {
        this.name = name;
        this.image = "https://spoonacular.com/cdn/ingredients_100x100/" + image;
        this.names = names;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return "https://spoonacular.com/cdn/ingredients_100x100/" + image;
    }

    public String getNames() {
        return names;
    }


}
