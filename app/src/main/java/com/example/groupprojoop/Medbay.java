package com.example.groupprojoop;

import java.util.ArrayList;
import java.util.List;

public class Medbay {
    protected Storage medbaystorage;
    protected static List<CrewMember> Defeatedunits = new ArrayList<>();

    public static void admit(CrewMember crewMember){
        Defeatedunits.add(crewMember);

        Quarters.storage.removeCrewMember(crewMember.id);
    }

    public List<CrewMember> listDefeatedunits(){
        return Defeatedunits;
    }

    public void restore(CrewMember crewMember){
        crewMember.restoreEnergy();
        Defeatedunits.remove(crewMember);
        Quarters.storage.addCrewMember(crewMember);
    }
}
