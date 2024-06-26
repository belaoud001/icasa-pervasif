package lights;

import alarm.service.AlarmService;
import constants.Constants;
import fr.liglab.adele.icasa.device.DeviceListener;
import fr.liglab.adele.icasa.device.GenericDevice;
import fr.liglab.adele.icasa.device.presence.PresenceSensor;
import fr.liglab.adele.icasa.device.light.BinaryLight;

import java.util.*;

import configurations.FollowMeConfiguration;
import fr.liglab.adele.icasa.device.light.DimmerLight;

import java.util.Map;

public class BinaryLightImpl implements DeviceListener, FollowMeConfiguration {

	private int maximumLightsToTurnOnPerRoom = 1;
	private double maximumEnergyConsumptionAllowedInARoom = 100.0d;

	private PresenceSensor[] presenceSensors;
	private BinaryLight[] binaryLights;
	private DimmerLight[] dimmerLights;

	private AlarmService alarmService;

	public BinaryLightImpl() {
		setMaximumAllowedEnergyInRoom(maximumEnergyConsumptionAllowedInARoom);
	}

	public void start() {
		System.out.println("Binary light component is starting ...");
		
		alarmService.fireAlarm();

		for (PresenceSensor sensor : presenceSensors)
			sensor.removeListener(this);
	}

	public void bindBinaryLight(BinaryLight binaryLight, Map properties) {
		System.out
				.println("Bind binary light " + binaryLight.getSerialNumber());
	}

	public void unbindBinaryLight(BinaryLight binaryLight, Map properties) {
		System.out.println("Unbind binary light " + binaryLight.getSerialNumber());

		if (binaryLight.getPowerStatus()) {
			turnOnLightsAtLocation(maximumLightsToTurnOnPerRoom,
					(String) binaryLight.getPropertyValue(Constants.LOCATION_PROPERTY_NAME));
		}
	}

	public void bindDimmerLights(DimmerLight dimmerLight, Map properties) {
		System.out
				.println("Bind dimmer light " + dimmerLight.getSerialNumber());
	}

	public void unbindDimmerLights(DimmerLight dimmerLight, Map properties) {
		System.out.println("Unbind dimmer light " + dimmerLight.getSerialNumber());
	}

	public void bindPresenceSensor(PresenceSensor presenceSensor, Map properties) {
		System.out.println("Bind presence sensor " + presenceSensor.getSerialNumber());
		presenceSensor.addListener(this);
	}

	public void unbindPresenceSensor(PresenceSensor presenceSensor, Map properties) {
		System.out.println("Unbind presence sensor " + presenceSensor.getSerialNumber());
		presenceSensor.removeListener(this);
	}

	public void deviceAdded(GenericDevice genericDevice) {
		System.err.println("This method is not implemented yet ...");
	}

	public void deviceEvent(GenericDevice genericDevice, Object object) {
		System.err.println("This method is not implemented yet ...");
	}

	public void devicePropertyAdded(GenericDevice genericDevice, String name) {
		System.err.println("This method is not implemented yet ...");
	}

	private synchronized List<BinaryLight> getAllBinaryLightsAtLocation(String location) {
		List<BinaryLight> binaryLightsLocation = new ArrayList<BinaryLight>();

		for (BinaryLight binaryLight : binaryLights)
			if (binaryLight.getPropertyValue(Constants.LOCATION_PROPERTY_NAME).equals(location))
				binaryLightsLocation.add(binaryLight);

		return binaryLightsLocation;
	}

	private synchronized List<DimmerLight> getAllDimmerLightsAtLocation(String location) {
		List<DimmerLight> dimmerLightsAtLocation = new ArrayList<DimmerLight>();

		for (DimmerLight dimmerLight : dimmerLights)
			if (dimmerLight.getPropertyValue(Constants.LOCATION_PROPERTY_NAME).equals(location))
				dimmerLightsAtLocation.add(dimmerLight);

		return dimmerLightsAtLocation;
	}

	private synchronized int getNumberOfBinaryLightsAtLocation(String location) {
		return getAllBinaryLightsAtLocation(location).size();
	}

	private synchronized int getNumberOfDimmerLightsAtLocation(String location) {
		return getAllDimmerLightsAtLocation(location).size();
	}

	public void devicePropertyModified(GenericDevice device, String propertyName, Object oldValue, Object newValue) {
		System.err.println("presence modified " + maximumLightsToTurnOnPerRoom);

		if (device instanceof PresenceSensor
				&& propertyName.equals(PresenceSensor.PRESENCE_SENSOR_SENSED_PRESENCE)) {

			System.err.println("presence modified entering " + maximumLightsToTurnOnPerRoom);

			PresenceSensor presenceSensor = (PresenceSensor) device;
			String location = (String) presenceSensor.getPropertyValue(Constants.LOCATION_PROPERTY_NAME);
			

			if (!Constants.LOCATION_UNKNOWN.equals(location)) {
				if (presenceSensor.getSensedPresence()) {
					turnOnLightsAtLocation(maximumLightsToTurnOnPerRoom, location);
					System.err.println("Maximum allowed is lights are turned on : " + maximumLightsToTurnOnPerRoom);
				} else
					turnOffLightsAtLocation(maximumLightsToTurnOnPerRoom, location);
			}
		}
	}

	public void devicePropertyRemoved(GenericDevice genericDevice, String name) {
		System.err.println("This method is not implemented yet ...");
	}

	public void deviceRemoved(GenericDevice genericDevice) {
		System.err.println("This method is not implemented yet ...");
	}

	public int getMaximumNumberOfLightsToTurnOn() {
		return maximumLightsToTurnOnPerRoom;
	}

	public void setMaximumNumberOfLightsToTurnOn(int maximumNumberOfLightsToTurnOn) {
		maximumLightsToTurnOnPerRoom = maximumNumberOfLightsToTurnOn;
	}

	public double getMaximumAllowedEnergyPerRoom() {
		return maximumEnergyConsumptionAllowedInARoom;
	}

	private int computeLightsConsideringEnergyLimits() {
		return (int) (maximumEnergyConsumptionAllowedInARoom / Constants.BINARY_LIGHT_CONSUMPTION);
	}

	private synchronized List<BinaryLight> getTurnedOnBinaryLightsAtLocation(
			String location) {
		List<BinaryLight> binaryLightsAtLocation = new ArrayList<BinaryLight>();
		List<BinaryLight> binaryLights = getAllBinaryLightsAtLocation(location);

		for (BinaryLight binaryLight : binaryLights)
			if (binaryLight.getPowerStatus())
				binaryLightsAtLocation.add(binaryLight);

		return binaryLightsAtLocation;
	}

	private int getNumberOfTurnedOnBinaryLightsAtLocation(String location) {
		return getTurnedOnBinaryLightsAtLocation(location).size();
	}

	private synchronized List<DimmerLight> getTurnedOnDimmerLightsAtLocation(
			String location) {
		List<DimmerLight> dimmerLightsAtLocation = new ArrayList<DimmerLight>();
		List<DimmerLight> dimmerLights = getAllDimmerLightsAtLocation(location);

		for (DimmerLight dimmerLight : dimmerLights)
			if (dimmerLight.getPowerLevel() != 0d)
				dimmerLightsAtLocation.add(dimmerLight);

		return dimmerLightsAtLocation;
	}

	private int getNumberOfTurnedOnDimmerLightsAtLocation(String location) {
		return getTurnedOnDimmerLightsAtLocation(location).size();
	}

	private int getNumberOfTurnedOnLightsAtLocation(String location) {
		return getNumberOfTurnedOnBinaryLightsAtLocation(location) + getNumberOfTurnedOnDimmerLightsAtLocation(location);
	}

	private int getNumberOfLightsAtLocation(String location) {
		return getNumberOfBinaryLightsAtLocation(location) + getNumberOfDimmerLightsAtLocation(location);
	}

	private void turnOnLightsAtLocation(int numberOfLights, String location) {
		int numberOfBinaryLightsAtLocation = getNumberOfBinaryLightsAtLocation(location);
		int numberOfBinaryLights = Math.min(numberOfBinaryLightsAtLocation, numberOfLights);

		System.out.println("ghadi nche3lo : " + numberOfBinaryLights);

		setupBinaryLightsAtLocation(numberOfBinaryLights, location, true);

		if (numberOfBinaryLightsAtLocation < numberOfLights) {
			int numberOfDimmerLights = numberOfLights - numberOfBinaryLightsAtLocation;
			setupDimmerLightsAtLocation(numberOfDimmerLights, location, true);
		}
	}

	private void turnOffLightsAtLocation(int numberOfLights, String location) {
		int numberOfDimmerLightsAtLocation = getNumberOfTurnedOnDimmerLightsAtLocation(location);
		int numberOfDimmerLights = Math.min(numberOfDimmerLightsAtLocation, numberOfLights);

		setupDimmerLightsAtLocation(numberOfDimmerLights, location, false);

		if (numberOfDimmerLightsAtLocation < numberOfLights) {
			int numberOfBinaryLights = numberOfLights - numberOfDimmerLightsAtLocation;

			setupBinaryLightsAtLocation(numberOfBinaryLights, location, false);
		}
	}

	private void setupBinaryLightsAtLocation(int numberOfBinaryLights, String location, boolean turnOn) {
		List<BinaryLight> binaryLights = getAllBinaryLightsAtLocation(location);

		for (BinaryLight binaryLight : binaryLights) {
			if (numberOfBinaryLights-- == 0)
				break;

			if (turnOn && !binaryLight.getPowerStatus())
				binaryLight.turnOn();
			else if (!turnOn && binaryLight.getPowerStatus())
				binaryLight.turnOff();
		}
	}

	private void setupDimmerLightsAtLocation(int numberOfDimmerLights, String location, boolean turnOn) {
		List<DimmerLight> dimmerLights = getAllDimmerLightsAtLocation(location);

		for (DimmerLight dimmerLight : dimmerLights) {
			if (numberOfDimmerLights == 0)
				break;

			if (turnOn && (dimmerLight.getPowerLevel() == 0)) {
				dimmerLight.setPowerLevel(1.0d);
				numberOfDimmerLights--;
			} else if (!turnOn && !(dimmerLight.getPowerLevel() == 0)) {
				dimmerLight.setPowerLevel(0d);
				numberOfDimmerLights--;
			}

		}
	}

	private void updateLightsStateAtLocation(
			int numberOfTurnedOnLightsAllowedAtLocation, String location) {
		int numberOfLightsAtLocation = getNumberOfLightsAtLocation(location);
		int numberOfTurnedOnLightsAtLocation = getNumberOfTurnedOnLightsAtLocation(location);

		if (numberOfTurnedOnLightsAllowedAtLocation > numberOfLightsAtLocation)
			numberOfTurnedOnLightsAllowedAtLocation = numberOfLightsAtLocation;

		if (numberOfTurnedOnLightsAllowedAtLocation != numberOfTurnedOnLightsAtLocation) {
			int numberOfBinaryLights = Math
					.abs(numberOfTurnedOnLightsAllowedAtLocation
							- numberOfTurnedOnLightsAtLocation);
			boolean turnOn = numberOfTurnedOnLightsAllowedAtLocation > numberOfTurnedOnLightsAtLocation;

			if (turnOn)
				turnOnLightsAtLocation(numberOfBinaryLights, location);
			else
				turnOffLightsAtLocation(numberOfBinaryLights, location);
		}

	}

	public void setMaximumAllowedEnergyInRoom(double maximumEnergy) {
		maximumEnergyConsumptionAllowedInARoom = maximumEnergy;
		maximumLightsToTurnOnPerRoom = computeLightsConsideringEnergyLimits();

		System.out.println("Maximum allowed energy per room has been updated successfully to : "
						+ maximumEnergyConsumptionAllowedInARoom + " Watt.");
		System.out.println("Using the given energy we can use a maximum of "
				+ maximumLightsToTurnOnPerRoom + " per room.");

		for (PresenceSensor presenceSensor : presenceSensors)
			if (presenceSensor.getSensedPresence()) {
				String currentLocation = presenceSensor.getPropertyValue(
						Constants.LOCATION_PROPERTY_NAME).toString();

				updateLightsStateAtLocation(maximumLightsToTurnOnPerRoom,
						currentLocation);
			}
	}

	public void stop() {
		System.out.println("Binary light component is stopping ...");

		for (PresenceSensor sensor : presenceSensors)
			sensor.removeListener(this);
	}

}
