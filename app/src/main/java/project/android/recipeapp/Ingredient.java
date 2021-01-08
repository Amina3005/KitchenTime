package project.android.recipeapp;

public class Ingredient {
    private String nam;
    private String Thumbnail;
    private boolean selected;

    Ingredient(String name, String thumbnail) {
        this.nam = name;
        Thumbnail = "https://spoonacular.com/cdn/ingredients_100x100/" + thumbnail;
        selected = false;
    }

    public String getNam() {
        return nam;
    }

    public String getThumbnail() {
        return Thumbnail;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected() {
        selected = !selected;
    }
}
