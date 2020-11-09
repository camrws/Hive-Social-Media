package com.example.hivefrontend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.hivefrontend.Login.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

/**
 * Main activity which the app opens on, if there is a user logged in.
 */
public class MainActivity extends AppCompatActivity {
    public BottomNavigationView navView;
    public ImageView hiveLogo;
    public ImageButton gearIcon;
    public NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!(SharedPrefManager.getInstance(this).isLoggedIn())) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        navView = findViewById(R.id.nav_view);
        hiveLogo = (ImageView) findViewById(R.id.hiveLogo);
        gearIcon = (ImageButton) findViewById(R.id.gearIcon);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_notifications, R.id.navigation_buzz, R.id.navigation_profile)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                destinationChange(destination.getId());
            }
        });
        NavigationUI.setupWithNavController(navView, navController);
    }


    /**
     * Handles if a new fragment within the activity is selected
     * @param id The id of the destination
     */
    public void destinationChange(int id) {
        if(id == R.id.navigation_buzz) {
            navView.setVisibility(View.GONE);
            gearIcon.setVisibility(View.GONE);
        } else {
            navView.setVisibility(View.VISIBLE);
            gearIcon.setVisibility(View.VISIBLE);
        }

        if(id == R.id.navigation_profile || id == R.id.navigation_buzz) {
            hiveLogo.setVisibility(View.GONE);
        } else {
            hiveLogo.setVisibility(View.VISIBLE);
        }

    }


    /**
     * Opens the Android-default settings activity
     * @param view the view.
     */
    public void viewSettings(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

}