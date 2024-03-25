package configurations;

public interface FollowMeConfiguration {
	
    public int getMaximumNumberOfLightsToTurnOn();
 
    public void setMaximumNumberOfLightsToTurnOn(int maximumNumberOfLightsToTurnOn);

    public double getMaximumAllowedEnergyPerRoom();

    public void setMaximumAllowedEnergyInRoom(double maximumEnergy);
    
}
