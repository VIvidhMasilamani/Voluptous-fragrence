package com.example.groupprojoop;

public class CrewMember {
    protected int id;
    protected String name;
    protected int skill;
    protected int resillience;
    protected int exp;
    protected int level;
    protected int energy;
    protected int maxEnergy;
    protected String color;
    protected boolean isDefeated;
    protected boolean isShiny;
    protected int missionsCompleted;
    protected int victories;
    protected int trainingSessions;
    protected String type; // For polymorphic serialization

    public CrewMember(String name, int skill, int resillience, int energy) {
        this.name = name;
        this.skill = skill;
        this.resillience = resillience;
        this.energy = energy;
        this.maxEnergy = energy;
        this.exp = 0;
        this.level = 1;
        this.isDefeated = false;
        this.type = this.getClass().getSimpleName();
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

    public boolean gainExperience(int amount){
        this.exp += amount;
        int nextLevelExp = level * 100;
        if (this.exp >= nextLevelExp) {
            levelUp();
            return true;
        }
        return false;
    }

    protected void levelUp() {
        level++;
        exp = 0;
        skill += 2;
        resillience += 1;
        maxEnergy += 10;
        energy = maxEnergy;
    }

    public void restoreEnergy(){
        this.energy = this.maxEnergy;
        this.isDefeated = false;
    }

    public String getStatsString() {
        return "Level: " + level + "\n" +
               "Experience: " + exp + "/" + (level * 100) + "\n" +
               "Skill: " + skill + "\n" +
               "Resilience: " + resillience + "\n" +
               "Energy: " + energy + "/" + maxEnergy + "\n\n" +
               "Ability: " + getPassiveDescription();
    }
}
