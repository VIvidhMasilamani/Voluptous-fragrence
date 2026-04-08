package com.example.groupprojoop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CrewAdapter extends RecyclerView.Adapter<CrewAdapter.CrewViewHolder> {
    public enum Context { QUARTERS, MISSION, SIMULATOR }

    private List<CrewMember> crewList;
    private Quarters quarters;
    private Simulator simulator;
    private Missioncontrol missionControl;
    private Runnable onDataChanged;
    private Context context;

    public CrewAdapter(List<CrewMember> crewList, Quarters quarters, Simulator simulator, Missioncontrol missionControl, Context context, Runnable onDataChanged) {
        this.crewList = crewList;
        this.quarters = quarters;
        this.simulator = simulator;
        this.missionControl = missionControl;
        this.context = context;
        this.onDataChanged = onDataChanged;
    }

    @NonNull
    @Override
    public CrewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_crew_member, parent, false);
        return new CrewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CrewViewHolder holder, int position) {
        CrewMember crewMember = crewList.get(position);
        holder.nameText.setText(crewMember.name);
        holder.specialityText.setText(crewMember.getClass().getSimpleName());
        holder.energyProgress.setProgress(crewMember.energy);
        holder.energyProgress.setMax(crewMember.maxEnergy);

        // Visibility logic based on context
        if (context == Context.QUARTERS) {
            holder.btnAttack.setVisibility(View.GONE);
            holder.btnMoveToQuarters.setVisibility(View.VISIBLE);
            holder.btnMoveToSimulation.setVisibility(View.VISIBLE);
            holder.btnMoveToQuarters.setText("Rest");
            holder.btnMoveToSimulation.setText("Train");
        } else if (context == Context.MISSION || context == Context.SIMULATOR) {
            holder.btnAttack.setVisibility(View.VISIBLE);
            holder.btnMoveToQuarters.setVisibility(View.GONE);
            holder.btnMoveToSimulation.setVisibility(View.GONE);
        }

        holder.btnAttack.setOnClickListener(v -> {
            if (missionControl != null && missionControl.threat != null && !missionControl.threat.isDefeated()) {
                String result = missionControl.executeCombatTurn(crewMember);
                Toast.makeText(v.getContext(), result, Toast.LENGTH_SHORT).show();
                if (onDataChanged != null) onDataChanged.run();
            } else if (context == Context.SIMULATOR) {
                // Simplified simulator combat
                Toast.makeText(v.getContext(), "Training Attack!", Toast.LENGTH_SHORT).show();
                if (onDataChanged != null) onDataChanged.run();
            }
        });

        holder.btnMoveToQuarters.setOnClickListener(v -> {
            quarters.restoreEnergy(crewMember);
            Toast.makeText(v.getContext(), crewMember.name + " is resting.", Toast.LENGTH_SHORT).show();
            if (onDataChanged != null) onDataChanged.run();
        });

        holder.btnMoveToSimulation.setOnClickListener(v -> {
            if (quarters != null && simulator != null) {
                quarters.moveTosimulation(crewMember, simulator, v.getContext());
            }
        });
    }

    @Override
    public int getItemCount() {
        return crewList != null ? crewList.size() : 0;
    }

    public static class CrewViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, specialityText;
        ProgressBar energyProgress;
        Button btnAttack, btnMoveToQuarters, btnMoveToSimulation;

        public CrewViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.text_crew_name);
            specialityText = itemView.findViewById(R.id.text_crew_speciality);
            energyProgress = itemView.findViewById(R.id.progress_energy);
            btnAttack = itemView.findViewById(R.id.btn_attack);
            btnMoveToQuarters = itemView.findViewById(R.id.btn_move_to_quarters);
            btnMoveToSimulation = itemView.findViewById(R.id.btn_move_to_simulation);
        }
    }
}
