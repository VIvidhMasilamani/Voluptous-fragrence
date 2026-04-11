package com.example.groupprojoop;

public class Scientist extends CrewMember {
    protected int analysisSkill;
    protected int researchSkill;
    public Scientist(String name, int skill, int resillience, int energy) {
        super(name, skill, resillience, energy);
    }

    @Override
    public String getPassiveDescription() {
        return "For every 5 victories, the scientist increases their signature attack power by 10% against a familiar threat";
    }

    @Override
    public <T> void signature(T target) {
        if (target instanceof Threat) {
            Threat threat = (Threat) target;
            int baseDamage = attack(threat);
            double multiplier = 1.0;
            
            // For every 5 victories, increase damage by 10%
            if (this.victories > 0) {
                multiplier += (this.victories / 5) * 0.1;
            }
            
            int finalDamage = (int) (baseDamage * multiplier);
            threat.takeDamage(finalDamage);
        }
    }
}
