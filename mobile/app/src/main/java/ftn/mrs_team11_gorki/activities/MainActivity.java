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
import androidx.navigation.NavOptions;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import ftn.mrs_team11_gorki.R;
import ftn.mrs_team11_gorki.databinding.ActivityMainBinding;
import ftn.mrs_team11_gorki.fragments.LoginFragment;

// >>> CHANGE: import TokenStorage
import ftn.mrs_team11_gorki.auth.TokenStorage;

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

        // >>> CHANGE: umesto setupWithNavController direktno, dodaj listener da uhvati logout
        NavigationView navView = binding.navigationView;

        // ovo ostaje da bi ostale stavke drawer-a radile normalno
        NavigationUI.setupWithNavController(navView, navController);

        // >>> CHANGE: ručno hvatamo klik na logout (jer nema fragment destination)
        navView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_logout) {
                doLogout(); // >>> CHANGE
                return true;
            }

            // sve ostalo: neka radi kao i pre (navigacija po id-jevima)
            boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
            if (handled) drawer.closeDrawers();
            return handled;
        });

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

            // >>> CHANGE (preporuka): nemoj ovde odmah loggedIn=true,
            // nego tek kad login uspe (u LoginFragment onResponse)
            // ali ako trenutno ostavljaš ovako - radiće ti UI.
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

    // >>> CHANGE: nova metoda za logout
    private void doLogout() {
        // 1) obriši token / user info iz storage-a
        new TokenStorage(this).clear(); // ako kod tebe nije clear(), zameni imenom tvoje metode

        // 2) resetuj UI stanje
        loggedIn = false;
        invalidateOptionsMenu();
        updateDrawerLock();

        // 3) zatvori drawer
        drawer.closeDrawers();

        // 4) idi na unuserHome i očisti backstack (da ne može back na ulogovane stranice)
        NavOptions navOptions = new NavOptions.Builder()
                .setPopUpTo(R.id.base_navigation, true)
                .build();

        navController.navigate(R.id.unuserHomeFragment, null, navOptions);
    }
}