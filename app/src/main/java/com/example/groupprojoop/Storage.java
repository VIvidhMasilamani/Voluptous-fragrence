package com.example.groupprojoop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Storage {

    protected HashMap<Integer, CrewMember> crewMap = new HashMap<>();

    public void addCrewMember(CrewMember crewMember){

        crewMap.put(crewMember.id, crewMember);
    }

    public void removeCrewMember(int id) {
        crewMap.remove(id);
    }

    public CrewMember getCrewMember(int id){
        return crewMap.get(id);
    }


    public List<CrewMember> listCrewMembers(){
        return new ArrayList<>(crewMap.values());
    }
}
