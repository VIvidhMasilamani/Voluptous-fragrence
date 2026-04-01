package com.example.groupprojoop;

public class Soldier extends CrewMember {
    protected int combatSkill;
    protected int armorRating;

    public Soldier(String name, int skill, int resillience, int energy) {
        super(name, skill, resillience, energy);
    }

    @Override
    public int attack(Threat target){
        int damage = (combatSkill + skill) - target.resillience;
        return Math.max(0, damage);
    }

    @Override
    public void takeDamage(int damage){
        this.energy -= Math.max(0, damage - (this.resillience+this.armorRating);
        if (this.energy <= 0) {
            this.energy = 0;
            this.isDefeated = true;
        }
    }

    @Override
    public String getPassiveDescription() {
        return "Intimidates opponents causing skill to drop by 1.";
    }

    @Override
    public <T> void signature(T target) {
        if (target instanceof Threat) {
            Threat threat = (Threat) target;
            int signatureDamage = (int) (attack(threat) * 1.5);
            threat.takeDamage(signatureDamage);
        }
    }
}
