package com.example.groupprojoop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SelectCrewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CrewAdapter adapter;
    private TextView selectCountText;
    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_crew);

        recyclerView = findViewById(R.id.recycler_select_crew);
        selectCountText = findViewById(R.id.text_select_count);
        confirmButton = findViewById(R.id.btn_confirm_selection);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        List<CrewMember> availableCrew = Quarters.storage.listCrewMembers();
        adapter = new CrewAdapter(availableCrew, null, null, CrewAdapter.Context.SELECTION, null);
        
        adapter.setSelectionListener(count -> {
            selectCountText.setText("Selected: " + count + "/2");
        });

        recyclerView.setAdapter(adapter);

        confirmButton.setOnClickListener(v -> {
            List<CrewMember> selected = adapter.getSelectedCrew();
            if (selected.isEmpty()) {
                Toast.makeText(this, "Select at least 1 member", Toast.LENGTH_SHORT).show();
            } else {
                // Pass selected IDs to MissionActivity
                ArrayList<Integer> selectedIds = new ArrayList<>();
                for (CrewMember cm : selected) selectedIds.add(cm.id);
                
                Intent intent = new Intent(this, MissionActivity.class);
                intent.putIntegerArrayListExtra("selected_ids", selectedIds);
                startActivity(intent);
                finish();
            }
        });
    }
}
