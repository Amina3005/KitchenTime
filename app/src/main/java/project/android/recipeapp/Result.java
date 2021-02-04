package project.android.recipeapp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result {

    @SerializedName("recipes")
    private List<Food> result = null;

    public List<Food> getResult() {
        return  result;
    }


    @SerializedName("results")
    private List<Food> res = null;

    public List<Food> getRes() {
        return res;
    }
    public void setRes(List<Food> res) {
        this.res = res;
    }

}
