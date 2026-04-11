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

        setupMission();
        initUI();
    }

    private void setupMission() {
        missionControl = new Missioncontrol(new Storage());
        missionControl.generateThreat();
        
        List<Integer> selectedIds = getIntent().getIntegerArrayListExtra("selected_ids");
        missionCrew = new ArrayList<>();
        
        if (selectedIds != null) {
            for (Integer id : selectedIds) {
                CrewMember c = Quarters.storage.getCrewMember(id);
                if (c != null) {
                    missionCrew.add(c);
                    missionControl.missioncontrolStorage.addCrewMember(c);
                    Quarters.storage.removeCrewMember(id);
                }
            }
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
        adapter = new CrewAdapter(missionCrew, null, missionControl, CrewAdapter.Context.MISSION, this::updateMissionState);
        recyclerView.setAdapter(adapter);

        findViewById(R.id.btn_retreat).setOnClickListener(v -> {
            returnToQuarters();
            finish();
        });
    }

    private void updateMissionState() {
        updateThreatUI();
        adapter.notifyDataSetChanged();

        if (missionControl.threat.isDefeated()) {
            List<String> notifications = missionControl.endMission();
            Toast.makeText(this, "Mission Successful!", Toast.LENGTH_SHORT).show();
            for (String note : notifications) {
                Toast.makeText(this, note, Toast.LENGTH_LONG).show();
            }
            returnToQuarters();
            finish();
        } else {
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
