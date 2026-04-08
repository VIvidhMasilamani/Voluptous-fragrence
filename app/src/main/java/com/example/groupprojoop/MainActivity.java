package com.example.groupprojoop;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Storage storage;
    private Quarters quarters;
    private Missioncontrol missionControl;
    private Medbay medbay;
    private Simulator simulator;
    
    private RecyclerView recyclerView;
    private CrewAdapter adapter;
    private TextView textStatsPlayed;
    private TextView textStatsVictories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Game Logic
        storage = new Storage();
        quarters = new Quarters(storage);
        missionControl = new Missioncontrol(new Storage());
        medbay = new Medbay();
        simulator = new Simulator(new Storage());

        // Initialize UI
        textStatsPlayed = findViewById(R.id.text_stats_played);
        textStatsVictories = findViewById(R.id.text_stats_victories);
        recyclerView = findViewById(R.id.recycler_crew);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        updateUI();

        FloatingActionButton fabAdd = findViewById(R.id.fab_add_crew);
        fabAdd.setOnClickListener(v -> showCreateCrewDialog());

        findViewById(R.id.btn_save).setOnClickListener(v -> {
            Datamanager.saveFullGame(this, quarters, missionControl, medbay);
            Toast.makeText(this, "Game Saved", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.btn_load).setOnClickListener(v -> {
            Datamanager.loadFullGame(this, quarters, missionControl, medbay);
            updateUI();
            Toast.makeText(this, "Game Loaded", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.btn_mission).setOnClickListener(v -> startMission());
    }

    private void showCreateCrewDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_create_crew, null);
        EditText editName = view.findViewById(R.id.edit_crew_name);
        RadioGroup groupSpec = view.findViewById(R.id.radio_group_speciality);

        new AlertDialog.Builder(this)
                .setTitle("Create Crew Member")
                .setView(view)
                .setPositiveButton("Create", (dialog, which) -> {
                    String name = editName.getText().toString().trim();
                    if (name.isEmpty()) name = "Unknown";
                    
                    String spec = "Soldier";
                    int checkedId = groupSpec.getCheckedRadioButtonId();
                    if (checkedId == R.id.radio_pilot) spec = "Pilot";
                    else if (checkedId == R.id.radio_engineer) spec = "Engineer";
                    else if (checkedId == R.id.radio_medic) spec = "Medic";
                    else if (checkedId == R.id.radio_scientist) spec = "Scientist";

                    quarters.createCrewMember(name, spec);
                    updateUI();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void startMission() {
        if (storage.listCrewMembers().size() < 1) {
            Toast.makeText(this, "Need at least 1 crew member", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Change screens to MissionActivity
        Intent intent = new Intent(this, MissionActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI(); // Refresh when returning from mission
    }

    private void updateUI() {
        List<CrewMember> crewMembers = storage.listCrewMembers();
        adapter = new CrewAdapter(crewMembers, quarters, simulator, missionControl, CrewAdapter.Context.QUARTERS, this::updateUI);
        recyclerView.setAdapter(adapter);
        
        textStatsPlayed.setText("Missions: " + StatisticsManager.globalMissionsPlayed);
        textStatsVictories.setText("Victories: " + StatisticsManager.globalVictories);
    }
}
