package com.example.groupprojoop;

import java.util.ArrayList;
import java.util.List;

public class Medbay {
    public static List<CrewMember> Defeatedunits = new ArrayList<>();

    public static void admit(CrewMember crewMember){
        if (!Defeatedunits.contains(crewMember)) {
            Defeatedunits.add(crewMember);
            if (Quarters.storage != null) {
                Quarters.storage.removeCrewMember(crewMember.id);
            }
        }
    }

    public static void restore(CrewMember crewMember){
        crewMember.restoreEnergy();
        Defeatedunits.remove(crewMember);
        if (Quarters.storage != null) {
            Quarters.storage.addCrewMember(crewMember);
        }
    }
}
