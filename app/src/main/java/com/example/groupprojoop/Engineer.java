package com.example.groupprojoop;

public class Engineer extends CrewMember {
    protected int EngineeringSkill;
    protected int MaintenanceSkill;

    public Engineer(String name, int skill, int resillience, int energy) {
        super(name, skill, resillience, energy);
    }

    @Override
    public String getPassiveDescription() {
        return "Special attack deals 1.3x damage and consumes less energy.";
    }

    @Override
    public <T> void signature(T target) {
        if (target instanceof Threat) {
            Threat threat = (Threat) target;
            int damage = (int) (attack(threat) * 1.3);
            threat.takeDamage(damage);
        }
    }
}
