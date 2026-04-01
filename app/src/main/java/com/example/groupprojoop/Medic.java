package com.example.groupprojoop;

public class Medic extends CrewMember {
    protected int healingPower;
    protected int supportskill;
    public Medic(String name, int skill, int resillience, int energy) {
        super(name, skill, resillience, energy);
    }

    @Override
    public String getPassiveDescription() {
        return "if both parties are below 50% Energy heals Ally by 25% of Ally's max Energy";
    }

    @Override
    public <T> void signature(T target) {
        if (target instanceof CrewMember) {
            CrewMember crewMember = (CrewMember) target;
            int healAmount = crewMember.maxEnergy / 5;
            crewMember.energy=Math.min(crewMember.maxEnergy, crewMember.energy + healAmount);
        } else if (target instanceof Threat) {
            Threat threat = (Threat) target;
            int healAmount = threat.maxEnergy / 2;
            threat.heal(healAmount);
        }
    }
}
