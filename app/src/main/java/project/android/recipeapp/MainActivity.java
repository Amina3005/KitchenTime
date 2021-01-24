package project.android.recipeapp;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    public MainFragment mainFragment;
    public SearchIngredient searchIngredient;

    FragmentManager fm = getSupportFragmentManager();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainFragment = new MainFragment();
        searchIngredient = new SearchIngredient();




        if (savedInstanceState != null) {
            mainFragment = (MainFragment) getSupportFragmentManager().getFragment(savedInstanceState, "title");
            Log.e("f error", "oncreate error activity");
            //searchIngredient = (SearchIngredient) getSupportFragmentManager().getFragment(savedInstanceState, "title");
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mainFragment).commit();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        fm.putFragment(outState,"title", mainFragment);
        fm.putFragment(outState, "title", searchIngredient);

        Log.e("save activity error", "onsaveinstance error with activity");

        super.onSaveInstanceState(outState);
    }




    BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selected = null;
            switch (item.getItemId()) {
                case R.id.nav_home:
                    selected = mainFragment;
                    break;
                case R.id.nav_ingredients:
                    selected = searchIngredient;
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selected).commit();
            return true;

        }
    };



}