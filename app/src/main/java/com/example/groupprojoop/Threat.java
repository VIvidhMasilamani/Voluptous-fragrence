package com.example.groupprojoop;

public class Threat {
    String name;
    int skill;
    int resillience;
    int energy;
    int maxEnergy;
    int level;
    public void setName(String name) {
        this.name = name;
    }

    public void setSkill(int skill) {
        this.skill = skill;
    }
    public void setResillience(int resillience) {
        this.resillience = resillience;
    }
    public void setEnergy(int energy) {
        this.energy = energy;
    }
    public void setMaxEnergy(int maxEnergy) {
        this.maxEnergy = maxEnergy;
    }
    public void setLevel(int level) {
        this.level = level;
    }




    public int attack(CrewMember target){
        int damage = skill - target.resillience;
        return Math.max(0, damage);
    }

    public double signature(CrewMember target){
        if (this.energy < (this.maxEnergy / 2)) {
            return attack(target) * 2.0;
        } else {
            return attack(target) * 1.5;
        }
    }

    public void takeDamage(int damage){
        this.energy -= damage;
        if (this.energy <= 0){
            this.energy = 0;
        }
    }

    public void heal(int amount) {
        this.energy = Math.min(this.maxEnergy, this.energy + amount);
    }

    public boolean isDefeated(){
        return this.energy <= 0;
    }
}
