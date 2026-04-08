package com.example.groupprojoop;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MissionActivity extends AppCompatActivity {

    private TextView textThreatName, textThreatStats;
    private ProgressBar progressThreatEnergy;
    private RecyclerView recyclerView;
    private CrewAdapter adapter;
    
    private Missioncontrol missionControl;
    private List<CrewMember> missionCrew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission);

        // In a real app, we'd pass these via Intent or a Game Manager
        // For this prototype, we'll use the static/singleton references if available
        // or re-initialize for the demo purpose.
        
        setupMission();
        initUI();
    }

    private void setupMission() {
        // Initialize mission components
        missionControl = new Missioncontrol(new Storage());
        missionControl.generateThreat();
        
        // Get crew from Intent or static storage
        // For now, let's assume we're taking them from the main storage for the demo
        missionCrew = new ArrayList<>(Quarters.storage.listCrewMembers());
        if (missionCrew.size() > 2) {
            missionCrew = missionCrew.subList(0, 2);
        }
        
        for (CrewMember c : missionCrew) {
            missionControl.missioncontrolStorage.addCrewMember(c);
            Quarters.storage.removeCrewMember(c.id);
        }
    }

    private void initUI() {
        textThreatName = findViewById(R.id.text_threat_name);
        textThreatStats = findViewById(R.id.text_threat_stats);
        progressThreatEnergy = findViewById(R.id.progress_threat_energy);
        recyclerView = findViewById(R.id.recycler_mission_crew);
        
        textThreatName.setText(missionControl.threat.name);
        updateThreatUI();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CrewAdapter(missionCrew, null, null, missionControl, CrewAdapter.Context.MISSION, this::updateMissionState);
        recyclerView.setAdapter(adapter);

        findViewById(R.id.btn_retreat).setOnClickListener(v -> finish());
    }

    private void updateMissionState() {
        updateThreatUI();
        adapter.notifyDataSetChanged();

        if (missionControl.threat.isDefeated()) {
            Toast.makeText(this, "Victory!", Toast.LENGTH_LONG).show();
            missionControl.endMission();
            returnToQuarters();
            finish();
        } else {
            // Check if all crew defeated
            boolean allDefeated = true;
            for (CrewMember c : missionCrew) {
                if (c.energy > 0) {
                    allDefeated = false;
                    break;
                }
            }
            if (allDefeated) {
                Toast.makeText(this, "Mission Failed!", Toast.LENGTH_LONG).show();
                missionControl.endMission();
                returnToQuarters();
                finish();
            }
        }
    }

    private void updateThreatUI() {
        progressThreatEnergy.setMax(missionControl.threat.maxEnergy);
        progressThreatEnergy.setProgress(missionControl.threat.energy);
        textThreatStats.setText("HP: " + missionControl.threat.energy + "/" + missionControl.threat.maxEnergy);
    }

    private void returnToQuarters() {
        for (CrewMember c : missionCrew) {
            if (c.energy <= 0) {
                Medbay.admit(c);
            } else {
                Quarters.storage.addCrewMember(c);
            }
        }
    }
}
