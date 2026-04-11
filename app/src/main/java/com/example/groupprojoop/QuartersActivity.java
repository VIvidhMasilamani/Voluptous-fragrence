package com.example.groupprojoop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class QuartersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CrewAdapter adapter;
    private Quarters quarters;
    private Storage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quarters);

        storage = Quarters.storage;
        quarters = new Quarters(storage);

        recyclerView = findViewById(R.id.recycler_quarters_crew);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        updateUI();

        FloatingActionButton fabAdd = findViewById(R.id.fab_add_crew);
        fabAdd.setOnClickListener(v -> showCreateCrewDialog());

        findViewById(R.id.btn_back_quarters).setOnClickListener(v -> finish());
    }

    private void updateUI() {
        List<CrewMember> crewMembers = storage.listCrewMembers();
        adapter = new CrewAdapter(crewMembers, quarters, null, CrewAdapter.Context.QUARTERS, this::updateUI);
        recyclerView.setAdapter(adapter);
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
}
