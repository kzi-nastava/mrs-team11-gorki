package ftn.mrs_team11_gorki;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DriverHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_driver_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_driver_history), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnMoreInfo = findViewById(R.id.login);

        btnMoreInfo.setOnClickListener(v -> {
                RideInfoFragment fragment = new RideInfoFragment();

                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragmentContainer, fragment)
                        .addToBackStack(null)
                        .commit();
            });
    }
}