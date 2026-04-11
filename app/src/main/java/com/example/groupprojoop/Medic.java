package com.example.groupprojoop;

public class Medic extends CrewMember {
    protected int healingPower = 10;
    protected int supportskill = 5;
    
    public Medic(String name, int skill, int resillience, int energy) {
        super(name, skill, resillience, energy);
    }

    @Override
    public String getPassiveDescription() {
        return "Special attack heals self and deals moderate damage to threats.";
    }

    @Override
    public <T> void signature(T target) {
        if (target instanceof CrewMember) {
            CrewMember crewMember = (CrewMember) target;
            int healAmount = crewMember.maxEnergy / 4;
            crewMember.energy = Math.min(crewMember.maxEnergy, crewMember.energy + healAmount);
        } else if (target instanceof Threat) {
            Threat threat = (Threat) target;
            // Deal damage instead of healing the threat!
            int damage = (int) (attack(threat) * 0.9);
            threat.takeDamage(damage);
            
            // Self-heal as a bonus
            int selfHeal = maxEnergy / 10;
            this.energy = Math.min(this.maxEnergy, this.energy + selfHeal);
        }
    }
}
