package com.example.groupprojoop;

public class Scientist extends CrewMember {
    protected int analysisSkill;
    protected int researchSkill;
    public Scientist(String name, int skill, int resillience, int energy) {
        super(name, skill, resillience, energy);
    }

    @Override
    public String getPassiveDescription() {
        return "For every 5 victories , the scientist increases their signature attack power by 10% against a familiar threat";
    }

    @Override
    public <T> void signature(T target) {
        if (target instanceof Threat) {
            int numVictories = ((CrewMember) target).victories;
            if (numVictories % 5 == 0) {
                int attackPowerIncrease = (int) (attack((Threat) target) * 0.1);
                ((Threat) target).takeDamage(attackPowerIncrease);
            }

        }
    }
}
