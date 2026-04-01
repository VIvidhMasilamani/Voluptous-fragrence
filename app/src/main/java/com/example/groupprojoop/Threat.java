package com.example.groupprojoop;

public class Threat {
    String name;
    int skill;
    int resillience;
    int energy;
    int maxEnergy;
    int level;

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
