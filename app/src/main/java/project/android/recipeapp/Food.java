package project.android.recipeapp;

public class Food {
    private String image;
    private String title;
    private int id;

    Food(int id, String image, String title) {
        this.id = id;
        this.image = image;
        this.title = title;
    }

    public  int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return title;
    }

}
