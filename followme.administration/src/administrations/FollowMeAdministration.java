package administrations;

import enums.EnergyGoal;
import enums.IlluminanceGoal;

public interface FollowMeAdministration {

    public void setIlluminancePreference(IlluminanceGoal illuminanceGoal);
 
    public IlluminanceGoal getIlluminancePreference();

    public void setEnergySavingGoal(EnergyGoal energyGoal);

    public EnergyGoal getEnergyGoal();
    
}
