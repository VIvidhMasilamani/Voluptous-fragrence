package com.example.groupprojoop;

import java.util.ArrayList;
import java.util.List;

public class Missioncontrol {
    protected Storage missioncontrolStorage;
    protected int missionCounter;
    protected static int successfulmissions;
    protected static int failedmissions;
    protected Threat threat;

    public Missioncontrol(Storage storage) {
        this.missioncontrolStorage = storage;
        this.missionCounter = 1;
    }

    public Missioncontrol(Storage storage, int missionCounter) {
        this.missioncontrolStorage = storage;
        this.missionCounter = missionCounter;
    }

    public void launchMission(List<CrewMember> selectedCrew) {
        for (CrewMember c : selectedCrew) {
            missioncontrolStorage.addCrewMember(c);
        }
        StatisticsManager.setGlobalMissionsPlayed(1);
    }

    public Threat generateThreat() {
        Threat threat = new Threat();
        threat.name = "Threat " + missionCounter;
        threat.skill = (int) (Math.random() * 10) + 5 + missionCounter;
        threat.resillience = (int) (Math.random() * 5) + missionCounter;
        threat.energy = (int) (Math.random() * 50) + 20 + (missionCounter * 5);
        threat.maxEnergy = threat.energy;
        threat.level = missionCounter;
        this.threat = threat;
        return threat;
    }

    public String executeCombatTurn(CrewMember attacker, boolean isSpecial) {
        if (threat == null || threat.isDefeated()) return "No threat to attack!";

        StringBuilder result = new StringBuilder();

        // Crew Member Turn
        if (isSpecial) {
            attacker.signature(threat);
            result.append(attacker.name).append(" used SPECIAL ATTACK on ").append(threat.name).append("!\n");
        } else {
            int crewDamage = attacker.attack(threat);
            threat.takeDamage(crewDamage);
            result.append(attacker.name).append(" attacked ").append(threat.name)
                    .append(" for ").append(crewDamage).append(" damage.\n");
        }

        if (threat.isDefeated()) {
            result.append(threat.name).append(" has been defeated!");
            return result.toString();
        }

        // Threat Turn: Response attack
        List<CrewMember> targets = missioncontrolStorage.listCrewMembers();
        List<CrewMember> aliveTargets = new ArrayList<>();
        for (CrewMember c : targets) {
            if (c.energy > 0) aliveTargets.add(c);
        }

        if (!aliveTargets.isEmpty()) {
            CrewMember target = aliveTargets.get((int) (Math.random() * aliveTargets.size()));
            int threatDamage = threat.attack(target);
            target.takeDamage(threatDamage);
            result.append(threat.name).append(" counter-attacked ").append(target.name)
                    .append(" for ").append(threatDamage).append(" damage.");
        }

        return result.toString();
    }

    public List<String> endMission() {
        boolean win = threat != null && threat.isDefeated();
        List<String> notifications = new ArrayList<>();

        for (CrewMember crewMember : missioncontrolStorage.listCrewMembers()) {
            if (win) {
                crewMember.victories++;
                boolean leveledUp = crewMember.gainExperience(30 * threat.level);
                if (leveledUp) {
                    notifications.add(crewMember.name + " LEVELED UP to Level " + crewMember.level + "!");
                }
            }
        }

        if (win) {
            successfulmissions++;
            StatisticsManager.recordMissionResult(true);
            missionCounter++;
        } else {
            failedmissions++;
            StatisticsManager.recordMissionResult(false);
        }
        return notifications;
    }
}
