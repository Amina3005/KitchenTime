# Description 
This my first project which I made from scratch.
Android application that offers different recipes using [Spoonacular API](https://spoonacular.com/food-api/) and designed to allow users to search recipes based on what they have in the kitchen. 

This app contains 2 Activities (MainActivity and FoodActivity) and 2 Fragments (Homefragment and SearchFragment).

**MainActivity** connected with fragments which could switch via the bottom navigation bar.

**FoodActivity**, here we can see detailed information about the certain recipes. Detailed information is :
* Image with ImageView.
* Title of the recipe with TextView.
* Ready in minutes with TextView and image of timer.
* List of ingredients with RecyclerView
* Instruction with TextView

Each of them sits in CardView.

**HomeFragment** includes a random list of recipes, search recipes by the name of the recipe. 
List of recipes - random list of recipes from Spoonacular API using RecyclerView. After each login to the app, you can see new random 50 recipes on your list.

**SearchFragment** include:
* EditText where we can enter ingredients which we have in the kitchen,
* ChipGroup. After clicking on a certain ingredient, it turns into a chip. I chose chipGroup , because it's more flexible you can remove this chip at any time,
* SearchButton. I pass a list of ingredients into the button and after the click, we can see a list of results - recipes.
* RecyclerView where we can see a list of results.

For example, if you don’t have chicken, the app won’t show any recipes that contain chicken, even if you have all of the other necessary ingredients for a certain dish.

### Technologies and libraries

In the app I used these techologies: 
* [Retrofit](https://square.github.io/retrofit/), 
* Android SDK, 
* Git (Version Control System) 

I used these libraries:
* [Glide](https://github.com/bumptech/glide)
* OkHTTP
* [Material Design](https://github.com/material-components/material-components-android)
* Also before Retrofit I used Volley library

After screen rotation information doesn't remove, for example, if you were looking for an apple pie recipe, then after turning the screen the search results will be saved, that is, they won't be recreated.

This function works with onSaveInstanceState() method. 
# Screenshots

## Main page
![mainpage](https://user-images.githubusercontent.com/63249052/107252809-e9177c80-6a5f-11eb-82d3-1303536b3d05.png)

## Search recipes
![search_main](https://user-images.githubusercontent.com/63249052/104006584-e4497980-51d0-11eb-84b0-e68d3b1ab4eb.jpg)

## Recipe Activity
![recipeDetail](https://user-images.githubusercontent.com/63249052/107253126-3693e980-6a60-11eb-8e53-52ef973fcb6c.png)

## Search recipes by ingredients 
![searchByIngr](https://user-images.githubusercontent.com/63249052/107253430-7f4ba280-6a60-11eb-8904-7065107c1b52.png)
