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
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import ftn.mrs_team11_gorki.R;
import ftn.mrs_team11_gorki.auth.TokenStorage;
import ftn.mrs_team11_gorki.databinding.ActivityMainBinding;
import ftn.mrs_team11_gorki.fragments.LoginFragment;
import ftn.mrs_team11_gorki.fragments.SupportChatDialogFragment;

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


        refreshAuthStateFromStorage();

        applyDrawerMenuByRole();

        setupDrawerNavigationListener();

        updateDrawerLock();

        Drawable icon = binding.toolbar.getNavigationIcon();
        if (icon != null) {
            icon.setTint(getColor(R.color.white));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();

        TokenStorage ts = new TokenStorage(this);
        String token = ts.getToken();
        boolean isLoggedIn = token != null && !token.isEmpty();
        String role=ts.getRole();
        if (!isLoggedIn) {
            getMenuInflater().inflate(R.menu.toolbar_menu_guest, menu);
        } else {
            if(role.equals("ADMIN")){
                getMenuInflater().inflate(R.menu.toolbar_manu_admin_correct, menu);
            }
            else {
                getMenuInflater().inflate(R.menu.toolbar_menu_logged_in, menu);
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        TokenStorage ts = new TokenStorage(this);
        String token = ts.getToken();
        String role=ts.getRole();

        if (id == R.id.nav_login) {
            new LoginFragment().show(getSupportFragmentManager(), "loginDialog");
            return true;
        }

        if (id == R.id.nav_register) {
            navController.navigate(R.id.passengerRegistrationFragment);
            return true;
        }
        if (id == R.id.action_support_chat) {
            new SupportChatDialogFragment().show(getSupportFragmentManager(), "supportChat");
            return true;
        }

        if(id == R.id.adminSupportFragment){

        }

        if (id == R.id.action_track_ride) {
            if(role.equals("PASSENGER")) {
                navController.navigate(R.id.rideInProgressPassengerFragment);
                return true;
            }else if(role.equals("DRIVER")){
                navController.navigate(R.id.rideInProgressDriverFragment);
                            return true;
            }
        }

        if (id == R.id.action_notifications) {
            new ftn.mrs_team11_gorki.fragments.NotificationInboxDialogFragment()
                    .show(getSupportFragmentManager(), "notif_inbox");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void onAuthChanged() {
        refreshAuthStateFromStorage();
        invalidateOptionsMenu();
        applyDrawerMenuByRole();
        updateDrawerLock();
    }

    private void refreshAuthStateFromStorage() {
        TokenStorage ts = new TokenStorage(this);
        String token = ts.getToken();
        loggedIn = token != null && !token.isEmpty();
    }

    private void updateDrawerLock() {
        drawer.setDrawerLockMode(
                loggedIn ? DrawerLayout.LOCK_MODE_UNLOCKED : DrawerLayout.LOCK_MODE_LOCKED_CLOSED
        );
    }

    private void applyDrawerMenuByRole() {
        NavigationView navView = binding.navigationView;
        navView.getMenu().clear();

        TokenStorage ts = new TokenStorage(this);
        String token = ts.getToken();
        String role = ts.getRole(); // "ADMIN", "DRIVER", "PASSENGER"

        if (token == null || token.isEmpty()) return;

        if ("ADMIN".equals(role)) {
            navView.inflateMenu(R.menu.drawer_admin);
        } else if ("DRIVER".equals(role)) {
            navView.inflateMenu(R.menu.drawer_driver);
        } else {
            // passenger ili default
            if (menuExists(R.menu.drawer_passenger)) {
                navView.inflateMenu(R.menu.drawer_passenger);
            } else {
                // fallback (ako nemas passenger)
                navView.inflateMenu(R.menu.drawer_driver);
            }
        }
    }

    private boolean menuExists(int resId) {
        try {
            getResources().getResourceName(resId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    private void setupDrawerNavigationListener() {
        NavigationView navView = binding.navigationView;

        navView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_logout) {
                doLogout();
                return true;
            }

            boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
            if (handled) drawer.closeDrawers();
            return handled;
        });
    }
    private void doLogout() {
        new TokenStorage(this).clear();

        refreshAuthStateFromStorage();
        invalidateOptionsMenu();

        applyDrawerMenuByRole();
        updateDrawerLock();

        drawer.closeDrawers();

        NavOptions navOptions = new NavOptions.Builder()
                .setPopUpTo(R.id.unuserHomeFragment, true)
                .build();

        navController.navigate(R.id.unuserHomeFragment, null, navOptions);
    }
}
