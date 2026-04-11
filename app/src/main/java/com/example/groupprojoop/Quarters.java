package com.example.groupprojoop;

import android.content.Context;
import android.content.Intent;

import java.util.List;

public class Quarters {
    protected static Storage storage;

    public Quarters(Storage storage) {
        this.storage = storage;
    }

    public CrewMember createCrewMember(String name, String speciality) {
        // Auto-naming logic
        if (name == null || name.trim().isEmpty()) {
            int count = 1;
            if (storage != null) {
                List<CrewMember> all = storage.listCrewMembers();
                for (CrewMember cm : all) {
                    if (cm.getClass().getSimpleName().equals(speciality)) {
                        count++;
                    }
                }
            }
            name = speciality + " " + count;
        }

        CrewMember crewMember;
        switch (speciality) {
            case "Soldier":
                crewMember = new Soldier(name, 12, 7, 20);
                break;
            case "Pilot":
                crewMember = new Pilot(name, 10, 3, 25);
                break;
            case "Engineer":
                crewMember = new Engineer(name, 11, 4, 22);
                break;
            case "Medic":
                crewMember = new Medic(name, 5, 5, 40);
                break;
            case "Scientist":
                crewMember = new Scientist(name, 7, 5, 15);
                break;
            default:
                crewMember = new CrewMember(name, 5, 5, 20);
                break;
        }

        int potentialId = 1;
        if (storage != null) {
            while (storage.getCrewMember(potentialId) != null) {
                potentialId++;
            }
        }
        crewMember.id = potentialId;

        if (storage != null) {
            storage.addCrewMember(crewMember);
        }
        return crewMember;
    }

    public void restoreEnergy(CrewMember crewMember) {
        crewMember.restoreEnergy();
    }

    public void moveTosimulation(CrewMember crewMember, Simulator simulator, Context context) {
        // Simulator removed as per user request, but keeping method signature for compatibility if needed elsewhere
    }

    public void returnToQuarters(CrewMember crewMember) {
        if (storage != null) {
            storage.addCrewMember(crewMember);
        }
    }
}
