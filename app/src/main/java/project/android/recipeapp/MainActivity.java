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

    Fragment fragment;
    FragmentManager fm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainFragment = new MainFragment();
        searchIngredient = new SearchIngredient();

        fm = getSupportFragmentManager();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mainFragment).commit();


        fragment = fm.findFragmentById(R.id.fragment_container);
        fragment = getSupportFragmentManager().getFragment(savedInstanceState, "title");

        if (savedInstanceState != null) {
            //fm.beginTransaction().replace(R.id.fragment_container, searchIngredient);
            //mainFragment = (MainFragment) getSupportFragmentManager().getFragment(savedInstanceState, "title");
            Log.e("f error", "oncreate error activity");
            //searchIngredient = (SearchIngredient) getSupportFragmentManager().getFragment(savedInstanceState, "name");
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        Log.e("save activity error", "onsaveinstance error with activity");
        getSupportFragmentManager().putFragment(outState,"title", searchIngredient);
        //getSupportFragmentManager().putFragment(outState, "title", fragment);
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