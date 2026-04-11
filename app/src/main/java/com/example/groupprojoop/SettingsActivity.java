package com.example.groupprojoop;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        findViewById(R.id.btn_save_game).setOnClickListener(v -> {
            // Need instances of quarters, missionControl, medbay. 
            // In a better design these would be singletons or in a GameState class.
            // For now, I'll use placeholders if I don't have global access.
            // Assuming Datamanager can handle the logic.
            Datamanager.saveFullGame(this, new Quarters(Quarters.storage), new Missioncontrol(new Storage()), new Medbay());
            Toast.makeText(this, "Game Saved", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.btn_load_game).setOnClickListener(v -> {
            Datamanager.loadFullGame(this, new Quarters(Quarters.storage), new Missioncontrol(new Storage()), new Medbay());
            Toast.makeText(this, "Game Loaded", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.btn_back_settings).setOnClickListener(v -> finish());
    }
}
