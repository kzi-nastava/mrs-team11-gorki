package ftn.mrs_team11_gorki.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import ftn.mrs_team11_gorki.R;
import ftn.mrs_team11_gorki.databinding.ActivityMainBinding;
import ftn.mrs_team11_gorki.fragments.LoginFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private AppBarConfiguration appBarConfiguration;
    private DrawerLayout drawer;
    private NavController navController;
    private Toolbar toolbar;
    private boolean loggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        drawer = binding.drawerLayout;
        toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        navController = Navigation.findNavController(this, R.id.fragment_nav_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.unuserHomeFragment, R.id.homeFragment
                ).setOpenableLayout(drawer).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navigationView, navController);
        updateDrawerLock();
        Drawable icon = binding.toolbar.getNavigationIcon();
        if(icon != null){
            icon.setTint(getColor(R.color.white));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        menu.clear();
        if(loggedIn){
            getMenuInflater().inflate(R.menu.toolbar_menu_logged_in, menu);
        } else {
            getMenuInflater().inflate(R.menu.toolbar_menu_guest, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();
        if(id == R.id.nav_login) {
            new LoginFragment().show(getSupportFragmentManager(), "loginDialog");
            loggedIn = true;
            invalidateOptionsMenu();
            updateDrawerLock();
            return true;
        }
        if(id == R.id.nav_register){
            navController.navigate(R.id.passengerRegistrationFragment);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void updateDrawerLock() {
        drawer.setDrawerLockMode(
                loggedIn ? DrawerLayout.LOCK_MODE_UNLOCKED : DrawerLayout.LOCK_MODE_LOCKED_CLOSED
        );
    }

}