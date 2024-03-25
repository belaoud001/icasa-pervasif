package enums;

public enum EnergyGoal {

    LOW   (100d),
    MEDIUM(200d),
    HIGH  (1000d);

    private double maximumEnergyInRoom;

    EnergyGoal(double powerInWatt) {
        maximumEnergyInRoom = powerInWatt;
    }

    public double getMaximumEnergyInRoom() {
        return maximumEnergyInRoom;
    }

    public static EnergyGoal getEnergyGoalByPower(double energyGoalPower) {
        for(EnergyGoal energyGoal : EnergyGoal.values())
            if (energyGoal.maximumEnergyInRoom == energyGoalPower)
                return energyGoal;

        return null;
    }

    public static EnergyGoal getEnergyGoalByTag(String energyGoalTag) {
        for (EnergyGoal energyGoal : EnergyGoal.values())
            if (energyGoal.toString().equals(energyGoalTag))
                return energyGoal;

        return null;
    }

}
