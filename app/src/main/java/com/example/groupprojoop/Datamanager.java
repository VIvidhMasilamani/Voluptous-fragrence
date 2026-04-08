package com.example.groupprojoop;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Datamanager {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILE_NAME = "game_save.json";

    public static void saveFullGame(Context context, Quarters quarters, Missioncontrol missionControl, Medbay medbay) {
        GameState state = new GameState();
        
        if (Quarters.storage != null) {
            state.quartersCrew = Quarters.storage.listCrewMembers();
        } else {
            state.quartersCrew = new ArrayList<>();
        }
        
        if (missionControl != null && missionControl.missioncontrolStorage != null) {
            state.missionCrew = missionControl.missioncontrolStorage.listCrewMembers();
        } else {
            state.missionCrew = new ArrayList<>();
        }
        
        if (medbay != null && medbay.Defeatedunits != null) {
            state.defeatedUnits = medbay.Defeatedunits;
        } else {
            state.defeatedUnits = new ArrayList<>();
        }

        state.globalMissionsPlayed = StatisticsManager.globalMissionsPlayed;
        state.globalVictories = StatisticsManager.globalVictories;

        if (missionControl != null) {
            state.missionCounter = missionControl.missionCounter;
            state.successfulMissions = missionControl.successfulmissions;
            state.failedMissions = missionControl.failedmissions;
        }

        File file = new File(context.getFilesDir(), FILE_NAME);
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(state, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadFullGame(Context context, Quarters quarters, Missioncontrol missionControl, Medbay medbay) {
        File file = new File(context.getFilesDir(), FILE_NAME);
        if (!file.exists()) return;

        try (FileReader reader = new FileReader(file)) {
            GameState state = gson.fromJson(reader, GameState.class);
            if (state == null) return;

            if (Quarters.storage != null && state.quartersCrew != null) {
                Quarters.storage.crewMap.clear();
                for (CrewMember member : restoreTypes(state.quartersCrew)) {
                    Quarters.storage.addCrewMember(member);
                }
            }

            if (missionControl != null && missionControl.missioncontrolStorage != null && state.missionCrew != null) {
                missionControl.missioncontrolStorage.crewMap.clear();
                for (CrewMember member : restoreTypes(state.missionCrew)) {
                    missionControl.missioncontrolStorage.addCrewMember(member);
                }
            }

            if (medbay != null && state.defeatedUnits != null) {
                medbay.Defeatedunits.clear();
                medbay.Defeatedunits.addAll(restoreTypes(state.defeatedUnits));
            }

            StatisticsManager.globalMissionsPlayed = state.globalMissionsPlayed;
            StatisticsManager.globalVictories = state.globalVictories;

            if (missionControl != null) {
                missionControl.missionCounter = state.missionCounter;
                missionControl.successfulmissions = state.successfulMissions;
                missionControl.failedmissions = state.failedMissions;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<CrewMember> restoreTypes(List<CrewMember> list) {
        List<CrewMember> restored = new ArrayList<>();
        for (CrewMember cm : list) {
            CrewMember exact;
            if ("Soldier".equals(cm.type)) exact = new Soldier(cm.name, cm.skill, cm.resillience, cm.maxEnergy);
            else if ("Pilot".equals(cm.type)) exact = new Pilot(cm.name, cm.skill, cm.resillience, cm.maxEnergy);
            else if ("Engineer".equals(cm.type)) exact = new Engineer(cm.name, cm.skill, cm.resillience, cm.maxEnergy);
            else if ("Medic".equals(cm.type)) exact = new Medic(cm.name, cm.skill, cm.resillience, cm.maxEnergy);
            else if ("Scientist".equals(cm.type)) exact = new Scientist(cm.name, cm.skill, cm.resillience, cm.maxEnergy);
            else exact = new CrewMember(cm.name, cm.skill, cm.resillience, cm.maxEnergy);
            
            exact.id = cm.id;
            exact.energy = cm.energy;
            exact.exp = cm.exp;
            exact.victories = cm.victories;
            exact.trainingSessions = cm.trainingSessions;
            restored.add(exact);
        }
        return restored;
    }
}
