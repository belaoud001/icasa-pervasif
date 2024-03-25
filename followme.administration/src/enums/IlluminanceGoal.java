package enums;

public enum IlluminanceGoal {
	
    SOFT  (1),
    MEDIUM(2),
    FULL  (3);

    private int numberOfLightsToTurnOn;

	IlluminanceGoal(int numberOfLightsToTurnOn) {
		this.numberOfLightsToTurnOn = numberOfLightsToTurnOn;
	}

    public int getNumberOfLightsToTurnOn() {
        return numberOfLightsToTurnOn;
    }
    
    public static IlluminanceGoal getIlluminanceGoalByIntensity(int illuminanceGoalIntensity) {
		for (IlluminanceGoal illuminanceGoalValue : IlluminanceGoal.values())
			if(illuminanceGoalValue.getNumberOfLightsToTurnOn() == illuminanceGoalIntensity)
				return illuminanceGoalValue;

		return null;
	}

	public static IlluminanceGoal getIlluminanceGoalByTag(String illuminanceGoalTag) {
		for (IlluminanceGoal illuminanceGoal : IlluminanceGoal.values())
			if (illuminanceGoal.toString().equals(illuminanceGoalTag))
				return illuminanceGoal;

		return null;
	}
    
}
