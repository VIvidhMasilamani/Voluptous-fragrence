package com.example.groupprojoop;

public class Engineer extends CrewMember {
    protected int EngineeringSkill;
    protected int MaintenanceSkill;

    public Engineer(String name, int skill, int resillience, int energy) {
        super(name, skill, resillience, energy);
    }

    @Override
    public String getPassiveDescription() {
        return "for every fallen crewmember the engineer will increase the Engineering skill which is a transferrable skill point to another crewmember";
    }

    @Override
    public <T> void signature(T target) {
        // Implementation for Engineer's signature move
    }
}
