package com.example.groupprojoop;

public class Pilot extends CrewMember {
    protected int flightSkill;
    protected int evasion;

    public Pilot(String name, int skill, int resillience, int energy) {
        super(name, skill, resillience, energy);
    }

    @Override
    public String getPassiveDescription() {
        return "If the selected map / environment is not on land evasion stat increased by 20%.";
    }

    @Override
    public <T> void signature(T target) {
        if (target instanceof Threat) {
            ((Threat) target).takeDamage((int) (attack((Threat) target)*1.5));
        }
    }
}
