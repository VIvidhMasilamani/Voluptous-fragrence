package com.example.groupprojoop;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MedbayActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CrewAdapter adapter;
    private Medbay medbay;
    private TextView emptyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medbay);

        medbay = new Medbay();
        recyclerView = findViewById(R.id.recycler_medbay_crew);
        emptyText = findViewById(R.id.text_medbay_empty);
        
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        updateUI();

        findViewById(R.id.btn_back_medbay).setOnClickListener(v -> finish());
    }

    private void updateUI() {
        List<CrewMember> defeatedUnits = Medbay.Defeatedunits;
        if (defeatedUnits == null || defeatedUnits.isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new CrewAdapter(defeatedUnits, null, null, CrewAdapter.Context.MEDBAY, this::updateUI);
            recyclerView.setAdapter(adapter);
        }
    }
}
