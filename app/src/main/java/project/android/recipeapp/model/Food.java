package project.android.recipeapp.model;

import com.google.gson.annotations.SerializedName;

public class Food {

    @SerializedName("id")
    private Integer id;
    @SerializedName("title")
    private String title;
    @SerializedName("image")
    private String image;

    public Food (Integer id, String title, String image) {
        this.id = id;
        this.title = title;
        this.image =  image;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return  image;
    }

    public void setImage (String image) {
        this.image = image;
    }

}
