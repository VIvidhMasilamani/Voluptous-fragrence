package com.example.groupprojoop;

import java.util.List;

public class StatisticsManager {
    protected static int globalMissionsPlayed = 0;
    protected static int globalVictories = 0;
    protected int totalCrewRecruited = 0;

    public static void setGlobalMissionsPlayed(int missions) {
        globalMissionsPlayed++;
    }

    public static void recordMissionResult(boolean success) {
        globalMissionsPlayed++;
        if (success) {
            globalVictories++;
        }
    }

    public void getCrewStats(int id, Storage storage) {
        CrewMember crewMember = storage.getCrewMember(id);
        if (crewMember != null) {
            System.out.println("Name: " + crewMember.name);
            System.out.println("Skill: " + crewMember.skill);
            System.out.println("Resilience: " + crewMember.resillience);
            System.out.println("Energy: " + crewMember.energy);
            System.out.println("Experience: " + crewMember.exp);
            System.out.println("Victories: " + crewMember.victories);
            System.out.println("Training Sessions: " + crewMember.trainingSessions);
        } else {
            System.out.println("Crew member not found.");
        }
    }


    public String generateAnyChartData(Storage storage) {
        StringBuilder csvData = new StringBuilder();
        csvData.append("Name,Skill,Experience,Victories\n");

        List<CrewMember> allMembers = storage.listCrewMembers();
        for (CrewMember member : allMembers) {
            csvData.append(member.name).append(",")
                   .append(member.skill).append(",")
                   .append(member.exp).append(",")
                   .append(member.victories).append("\n");
        }

        return csvData.toString();
    }
}
