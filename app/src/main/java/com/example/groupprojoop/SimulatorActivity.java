package com.example.groupprojoop;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SimulatorActivity extends AppCompatActivity {

    private ProgressBar progressHologram;
    private RecyclerView recyclerView;
    private CrewAdapter adapter;
    private Threat hologram;
    private List<CrewMember> trainingCrew;
    private Missioncontrol simControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulator);

        int crewId = getIntent().getIntExtra("crew_id", -1);
        CrewMember trainee = Quarters.storage.getCrewMember(crewId);

        if (trainee == null) {
            finish();
            return;
        }

        // Setup Hologram (Threat)
        hologram = new Threat();
        hologram.name = "Hologram Training Bot";
        hologram.maxEnergy = 50;
        hologram.energy = 50;
        hologram.skill = 5;
        hologram.resillience = 2;

        simControl = new Missioncontrol(new Storage());
        simControl.threat = hologram;
        
        trainingCrew = new ArrayList<>();
        trainingCrew.add(trainee);
        simControl.missioncontrolStorage.addCrewMember(trainee);

        initUI();
    }

    private void initUI() {
        progressHologram = findViewById(R.id.progress_hologram_energy);
        recyclerView = findViewById(R.id.recycler_training_crew);
        
        updateHologramUI();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Fixed: Removed the extra null argument to match CrewAdapter constructor
        adapter = new CrewAdapter(trainingCrew, null, simControl, CrewAdapter.Context.SIMULATOR, this::updateSimState);
        recyclerView.setAdapter(adapter);

        findViewById(R.id.btn_exit_sim).setOnClickListener(v -> finish());
    }

    private void updateSimState() {
        updateHologramUI();
        adapter.notifyDataSetChanged();

        if (hologram.isDefeated()) {
            Toast.makeText(this, "Training Complete! XP Gained.", Toast.LENGTH_LONG).show();
            for (CrewMember cm : trainingCrew) {
                cm.gainExperience(20);
                cm.trainingSessions++;
            }
            finish();
        } else if (trainingCrew.get(0).energy <= 0) {
            Toast.makeText(this, "Training Failed! Member Exhausted.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void updateHologramUI() {
        progressHologram.setMax(hologram.maxEnergy);
        progressHologram.setProgress(hologram.energy);
    }
}
