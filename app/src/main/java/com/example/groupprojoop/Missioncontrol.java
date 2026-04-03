package com.example.groupprojoop;

public class Missioncontrol{
    protected Storage missioncontrolStorage;
    protected int missionCounter;
    protected static int successfulmissions;
    protected static int failedmissions;
    protected Threat threat;

    public Missioncontrol(Storage storage) {
        this.missioncontrolStorage = storage;
        this.missionCounter = 1;
    }
    public Missioncontrol(Storage storage, int missionCounter) {
        this.missioncontrolStorage = storage;
        this.missionCounter = missionCounter;
    }

public void launchMission(CrewMember crewMember,CrewMember crewMember2){
        missioncontrolStorage.addCrewMember(crewMember);
        missioncontrolStorage.addCrewMember(crewMember2);
        StatisticsManager.setGlobalMissionsPlayed(1);



}
public Threat generateThreat(){
        Threat threat = new Threat();
        threat.name = "Threat" + missionCounter;
        threat.skill = (int) (Math.random() * 10) + 1;
        threat.resillience = (int) (Math.random() * 10) + 1;
        threat.energy = (int) (Math.random() * 10) + 1;
        threat.maxEnergy = threat.energy;
        threat.level = (int) missionCounter;
        this.threat = threat;
        return threat;



        }
public void sendToMedbay(CrewMember crewMember){
        missioncontrolStorage.removeCrewMember(crewMember.id);
        Medbay.admit(crewMember);

}
public boolean endMission(){
        for (CrewMember crewMember : missioncontrolStorage.listCrewMembers()) {
            missioncontrolStorage.removeCrewMember(crewMember.id);
            if (this.threat.isDefeated()) {
                crewMember.victories++;
                crewMember.gainExperience(10 * this.threat.level);

            }

            }
        }


    if (this.threat.isDefeated()){

        successfulmissions++;
        StatisticsManager.recordMissionResult(true);
        return true;
    }else{
        failedmissions++;
        return false;
    }

        }
