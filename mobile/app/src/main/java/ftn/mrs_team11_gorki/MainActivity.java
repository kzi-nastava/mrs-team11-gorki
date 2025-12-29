package ftn.mrs_team11_gorki;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // --- TOOLBAR ---
        Toolbar toolbar = findViewById(R.id.navbar);  // instancira Toolbar preko ID-ja
        setSupportActionBar(toolbar);                 // postavlja ga kao ActionBar

        // --- EDGE TO EDGE padding ---
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Button register = findViewById(R.id.register);
        Button login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "Login kliknuto");
                // Toast za korisnika (opciono)
                // Toast.makeText(MainActivity.this, "Login kliknuto", Toast.LENGTH_SHORT).show();
            }
        });
    }




}