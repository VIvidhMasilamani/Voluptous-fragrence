package com.example.groupprojoop;

import java.util.ArrayList;
import java.util.List;

public class Medbay {
    Protected Storage medbaystorage;
    protected List<CrewMember> Defeatedunits = new ArrayList<>();
    public void admit(CrewMember crewMember){
        Defeatedunits.add(crewMember);
        Quarters.storage.removeCrewMember(crewMember.id);
        }
    public List<CrewMember> listDefeatedunits(){
        return Defeatedunits;



    }
    public void restore(CrewMember crewMember){
        crewMember.restoreEnergy();
        Defeatedunits.remove(crewMember.id);
        Quarters.storage.addCrewMember(crewMember);
    }


}
