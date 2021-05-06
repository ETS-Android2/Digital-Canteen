package com.example.canteenapp.FoodList;

public class FoodModel {
    String foodname,foodpicture,foodprize,foodid;
    void FoodModel(){

    }

    public String getFoodid() {
        return foodid;
    }

    public void setFoodid(String foodid) {
        this.foodid = foodid;
    }

    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public String getFoodpicture() {
        return foodpicture;
    }

    public void setFoodpicture(String foodpicture) {
        this.foodpicture = foodpicture;
    }

    public String getFoodprize() {
        return foodprize;
    }

    public void setFoodprize(String foodprize) {
        this.foodprize = foodprize;
    }
}
