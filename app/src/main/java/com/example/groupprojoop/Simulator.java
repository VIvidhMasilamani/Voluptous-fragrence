package com.example.groupprojoop;

public class Simulator {
    protected Storage simulatorStorage;

    public Simulator(Storage storage) {
        this.simulatorStorage = storage;
    }

    public void train(CrewMember crewMember) {
        // Add to simulator storage if not already there
        if (simulatorStorage != null && simulatorStorage.getCrewMember(crewMember.id) == null) {
            simulatorStorage.addCrewMember(crewMember);
        }
        
        crewMember.trainingSessions++;
        
        if (crewMember.trainingSessions % 10 == 0) {
            crewMember.gainExperience(20);
        } else if (crewMember.trainingSessions % 5 == 0) {
            crewMember.gainExperience(10);
        }
    }

    public void moveToQuarters(CrewMember crewMember, Quarters targetQuarters) {

        if (simulatorStorage != null) {
            simulatorStorage.removeCrewMember(crewMember.id);
        }

        targetQuarters.returnToQuarters(crewMember);
    }
}
