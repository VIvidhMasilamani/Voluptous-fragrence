package com.example.groupprojoop;

public class Missioncontrol{
    protected Storage missioncontrolStorage;
    protected int missionCounter;

    public Missioncontrol(Storage storage) {
        this.missioncontrolStorage = storage;
        this.missionCounter = 1;
    }
    public Missioncontrol(Storage storage, int missionCounter) {

public void launchMission(CrewMember crewMember,CrewMember crewMember2){
        missioncontrolStorage.addCrewMember(crewMember);
        missioncontrolStorage.addCrewMember(crewMember2);
        missionCounter++;



}
public Threat generateThrea(){
        Threat threat = new Threat();
        threat.name = "Threat" + missionCounter;
        threat.skill = (int) (Math.random() * 10) + 1;
        threat.resillience = (int) (Math.random() * 10) + 1;
        threat.energy = (int) (Math.random() * 10) + 1;
        threat.maxEnergy = threat.energy;
        threat.level = (int) missionCounter;
        return threat;



        }

}
