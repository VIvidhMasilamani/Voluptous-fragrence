package com.example.groupprojoop;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Datamanager {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILE_NAME = "game_save.json";


    public static void saveFullGame(Context context, Quarters quarters, Missioncontrol missionControl, Medbay medbay) {
        GameState state = new GameState();
        
        // 1. Save Crew Lists from all locations
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

        // 2. Save Global Statistics
        state.globalMissionsPlayed = StatisticsManager.globalMissionsPlayed;
        state.globalVictories = StatisticsManager.globalVictories;

        // 3. Save Mission Progress
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
            GameState state;
            state = gson.fromJson(reader, GameState.class);
            if (state == null) return;

            // 1. Restore Quarters Storage
            if (Quarters.storage != null && state.quartersCrew != null) {
                Quarters.storage.crewMap.clear();
                for (CrewMember member : state.quartersCrew) {
                    Quarters.storage.addCrewMember(member);
                }
            }

            // 2. Restore Mission Control Storage
            if (missionControl != null && missionControl.missioncontrolStorage != null && state.missionCrew != null) {
                missionControl.missioncontrolStorage.crewMap.clear();
                for (CrewMember member : state.missionCrew) {
                    missionControl.missioncontrolStorage.addCrewMember(member);
                }
            }

            // 3. Restore Medbay Defeated Units
            if (medbay != null && state.defeatedUnits != null) {
                medbay.Defeatedunits.clear();
                medbay.Defeatedunits.addAll(state.defeatedUnits);
            }

            // 4. Restore Global Statistics
            StatisticsManager.globalMissionsPlayed = state.globalMissionsPlayed;
            StatisticsManager.globalVictories = state.globalVictories;

            // 5. Restore Mission Progress
            if (missionControl != null) {
                missionControl.missionCounter = state.missionCounter;
                missionControl.successfulmissions = state.successfulMissions;
                missionControl.failedmissions = state.failedMissions;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
