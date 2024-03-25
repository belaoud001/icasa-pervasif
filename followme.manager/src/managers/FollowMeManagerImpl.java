package managers;

import administrations.FollowMeAdministration;
import configurations.FollowMeConfiguration;
import enums.EnergyGoal;
import enums.IlluminanceGoal;

public class FollowMeManagerImpl implements FollowMeAdministration {

	private FollowMeConfiguration followMeConfiguration;

	public void start() {
		System.out.println("The follow me manager component is starting ...");
	}

	private int getCurrentMaxLight() {
		int numberOfTurnedOnLights = followMeConfiguration.getMaximumNumberOfLightsToTurnOn();

		System.out.println("The maximum number of lights is : " + numberOfTurnedOnLights);

		return numberOfTurnedOnLights;
	}

	private double getCurrentEnergyGoal() {
		double maximumAllowedEnergyPerRoom = followMeConfiguration.getMaximumAllowedEnergyPerRoom();

		System.out.println("The maximum energy allowed per room is : " + maximumAllowedEnergyPerRoom);

		return maximumAllowedEnergyPerRoom;
	}

	private void configureMaxLight(int numberOfLights) {
		followMeConfiguration.setMaximumNumberOfLightsToTurnOn(numberOfLights);
	}

	private void configureMaxAllowedEnergy(double energyAllowed) {
		followMeConfiguration.setMaximumAllowedEnergyInRoom(energyAllowed);
	}

	public void setIlluminancePreference(IlluminanceGoal illuminanceGoal) {
		configureMaxLight(illuminanceGoal.getNumberOfLightsToTurnOn());
	}

	public IlluminanceGoal getIlluminancePreference() {
		int currentIlluminanceGoal = getCurrentMaxLight();
		IlluminanceGoal illuminanceGoal = IlluminanceGoal.getIlluminanceGoalByIntensity(currentIlluminanceGoal);

		if(illuminanceGoal == null)
			System.err.println("ERROR : illuminance goal is null");

		return illuminanceGoal;
	}

	public void setEnergySavingGoal(EnergyGoal energyGoal) {
		configureMaxAllowedEnergy(energyGoal.getMaximumEnergyInRoom());
	}

	public EnergyGoal getEnergyGoal() {
		double currentEnergyGoal = getCurrentEnergyGoal();
		EnergyGoal energyGoal = EnergyGoal.getEnergyGoalByPower(currentEnergyGoal);

		if (energyGoal == null)
			System.err.println("ERROR : energy goal is null");

		return energyGoal;
	}

	public void stop() {
		System.out.println("The follow me manager component is stopping ...");
	}

}
