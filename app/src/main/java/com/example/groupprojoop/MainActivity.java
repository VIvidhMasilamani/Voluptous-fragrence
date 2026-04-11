package com.example.groupprojoop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView textStatsPlayed;
    private TextView textStatsVictories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI
        textStatsPlayed = findViewById(R.id.text_stats_played);
        textStatsVictories = findViewById(R.id.text_stats_victories);

        findViewById(R.id.btn_mission).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SelectCrewActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btn_goto_quarters).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, QuartersActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btn_goto_medbay).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MedbayActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btn_goto_settings).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        // Simulator button removed from Hub UI as per request
        // It's still in the layout XML but we can hide it or ignore it.
        if (findViewById(R.id.btn_goto_simulator) != null) {
            findViewById(R.id.btn_goto_simulator).setVisibility(android.view.View.GONE);
        }

        updateStats();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStats();
    }

    private void updateStats() {
        textStatsPlayed.setText("Global Missions: " + StatisticsManager.globalMissionsPlayed);
        textStatsVictories.setText("Global Victories: " + StatisticsManager.globalVictories);
    }
}
