package com.example.groupprojoop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CrewAdapter extends RecyclerView.Adapter<CrewAdapter.CrewViewHolder> {
    public enum Context { QUARTERS, MISSION, MEDBAY, SELECTION, SIMULATOR }

    private List<CrewMember> crewList;
    private Quarters quarters;
    private Missioncontrol missionControl;
    private Runnable onDataChanged;
    private Context context;
    
    // For selection mode
    private List<CrewMember> selectedCrew = new ArrayList<>();
    private OnSelectionChangedListener selectionListener;

    public interface OnSelectionChangedListener {
        void onSelectionChanged(int count);
    }

    public CrewAdapter(List<CrewMember> crewList, Quarters quarters, Missioncontrol missionControl, Context context, Runnable onDataChanged) {
        this.crewList = crewList;
        this.quarters = quarters;
        this.missionControl = missionControl;
        this.context = context;
        this.onDataChanged = onDataChanged;
    }

    public void setSelectionListener(OnSelectionChangedListener listener) {
        this.selectionListener = listener;
    }

    public List<CrewMember> getSelectedCrew() {
        return selectedCrew;
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

        // Reset UI
        holder.btnAttack.setVisibility(View.GONE);
        holder.btnSpecialAttack.setVisibility(View.GONE);
        holder.btnMoveToQuarters.setVisibility(View.GONE);
        holder.btnStats.setVisibility(View.VISIBLE);
        holder.checkSelect.setVisibility(View.GONE);

        if (context == Context.QUARTERS) {
            holder.btnMoveToQuarters.setVisibility(View.VISIBLE);
            holder.btnMoveToQuarters.setText("Rest");
        } else if (context == Context.MISSION || context == Context.SIMULATOR) {
            holder.btnAttack.setVisibility(View.VISIBLE);
            holder.btnSpecialAttack.setVisibility(View.VISIBLE);
        } else if (context == Context.MEDBAY) {
            holder.btnMoveToQuarters.setVisibility(View.VISIBLE);
            holder.btnMoveToQuarters.setText("Heal");
        } else if (context == Context.SELECTION) {
            holder.checkSelect.setVisibility(View.VISIBLE);
            holder.checkSelect.setChecked(selectedCrew.contains(crewMember));
            holder.checkSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    if (selectedCrew.size() < 2) {
                        selectedCrew.add(crewMember);
                    } else {
                        buttonView.setChecked(false);
                        Toast.makeText(buttonView.getContext(), "Max 2 members allowed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    selectedCrew.remove(crewMember);
                }
                if (selectionListener != null) selectionListener.onSelectionChanged(selectedCrew.size());
            });
        }

        holder.btnAttack.setOnClickListener(v -> {
            if (missionControl != null) {
                String result = missionControl.executeCombatTurn(crewMember, false);
                Toast.makeText(v.getContext(), result, Toast.LENGTH_SHORT).show();
                if (onDataChanged != null) onDataChanged.run();
            }
        });

        holder.btnSpecialAttack.setOnClickListener(v -> {
            if (missionControl != null) {
                String result = missionControl.executeCombatTurn(crewMember, true);
                Toast.makeText(v.getContext(), result, Toast.LENGTH_SHORT).show();
                if (onDataChanged != null) onDataChanged.run();
            }
        });

        holder.btnMoveToQuarters.setOnClickListener(v -> {
            if (context == Context.MEDBAY) {
                Medbay.restore(crewMember);
                if (onDataChanged != null) onDataChanged.run();
            } else if (quarters != null) {
                quarters.restoreEnergy(crewMember);
                if (onDataChanged != null) onDataChanged.run();
            }
        });

        holder.btnStats.setOnClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle(crewMember.name + " Stats")
                    .setMessage(crewMember.getStatsString())
                    .setPositiveButton("OK", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return crewList != null ? crewList.size() : 0;
    }

    public static class CrewViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, specialityText;
        ProgressBar energyProgress;
        Button btnAttack, btnSpecialAttack, btnMoveToQuarters, btnStats;
        CheckBox checkSelect;

        public CrewViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.text_crew_name);
            specialityText = itemView.findViewById(R.id.text_crew_speciality);
            energyProgress = itemView.findViewById(R.id.progress_energy);
            btnAttack = itemView.findViewById(R.id.btn_attack);
            btnSpecialAttack = itemView.findViewById(R.id.btn_special_attack);
            btnMoveToQuarters = itemView.findViewById(R.id.btn_move_to_quarters);
            btnStats = itemView.findViewById(R.id.btn_stats);
            checkSelect = itemView.findViewById(R.id.check_select);
        }
    }
}
