package com.example.groupprojoop;

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

    public void launchMission(CrewMember crewMember, CrewMember crewMember2) {
        missioncontrolStorage.addCrewMember(crewMember);
        missioncontrolStorage.addCrewMember(crewMember2);
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

    /**
     * Executes a turn-based combat round.
     * 1. Crew Member attacks Threat.
     * 2. If Threat is not defeated, Threat attacks a random Crew Member.
     */
    public String executeCombatTurn(CrewMember attacker) {
        if (threat == null || threat.isDefeated()) return "No threat to attack!";

        StringBuilder result = new StringBuilder();

        // Crew Member Turn
        int crewDamage = attacker.attack(threat);
        threat.takeDamage(crewDamage);
        result.append(attacker.name).append(" attacked ").append(threat.name)
                .append(" for ").append(crewDamage).append(" damage.\n");

        if (threat.isDefeated()) {
            result.append(threat.name).append(" has been defeated!");
            return result.toString();
        }

        // Threat Turn
        List<CrewMember> targets = missioncontrolStorage.listCrewMembers();
        if (!targets.isEmpty()) {
            CrewMember target = targets.get((int) (Math.random() * targets.size()));
            int threatDamage = threat.attack(target);
            target.takeDamage(threatDamage);
            result.append(threat.name).append(" counter-attacked ").append(target.name)
                    .append(" for ").append(threatDamage).append(" damage.");
        }

        return result.toString();
    }

    public boolean endMission() {
        boolean win = threat != null && threat.isDefeated();

        for (CrewMember crewMember : missioncontrolStorage.listCrewMembers()) {
            if (win) {
                crewMember.victories++;
                crewMember.gainExperience(10 * threat.level);
            }
        }

        if (win) {
            successfulmissions++;
            StatisticsManager.recordMissionResult(true);
            missionCounter++;
            return true;
        } else {
            failedmissions++;
            StatisticsManager.recordMissionResult(false);
            return false;
        }
    }
}
