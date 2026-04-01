package com.example.groupprojoop;

public class CrewMember {
    protected int id;
    protected String name;
    protected int skill;
    protected int resillience;
    protected int exp;
    protected int energy;
    protected int maxEnergy;
    protected String color;
    protected boolean isDefeated;
    protected boolean isShiny;
    protected int missionsCompleted;
    protected int victories;
    protected int trainingSessions;

    public CrewMember(String name, int skill, int resillience, int energy) {
        this.name = name;
        this.skill = skill;
        this.resillience = resillience;
        this.energy = energy;
        this.maxEnergy = energy;
        this.exp = 0;
        this.isDefeated = false;
    }

    public int attack(Threat target){
        int damage = skill - target.resillience;
        return Math.max(0, damage);
    }

    public <T> void signature(T target){
        // To be overridden by subclasses
    }

    public String getPassiveDescription(){
        return "Standard crew member.";
    }

    public void takeDamage(int damage){
        this.energy -= damage;
        if (energy <= 0){
            this.energy = 0;
            this.isDefeated = true;
        }
    }

    public void gainExperience(int amount){
        this.exp += amount;
    }

    public void restoreEnergy(){
        this.energy = this.maxEnergy;
    }
}
