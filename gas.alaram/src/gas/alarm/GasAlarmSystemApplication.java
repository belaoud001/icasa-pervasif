package gas.alarm;

import fr.liglab.adele.icasa.command.handler.Command;
import fr.liglab.adele.icasa.command.handler.CommandProvider;
import fr.liglab.adele.icasa.device.DeviceListener;
import fr.liglab.adele.icasa.device.GenericDevice;
import fr.liglab.adele.icasa.device.gasSensor.CarbonDioxydeSensor;
import fr.liglab.adele.icasa.device.light.BinaryLight;
import fr.liglab.adele.icasa.device.light.DimmerLight;
import mail.services.MailSender;
import fr.liglab.adele.icasa.service.scheduler.PeriodicRunnable;
import org.apache.felix.ipojo.annotations.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import alarm.service.AlarmService;

@Component
public class GasAlarmSystemApplication implements DeviceListener,
		PeriodicRunnable, GasAlarmService {

	private double _CO2Threshold = 3.8;

	private boolean state = false;

	private final Object m_lock;

	private boolean alarmRunning = false;

	private boolean mailSent = false;

	private Set<GasAlarmListener> setOfListener = new HashSet<GasAlarmListener>();

	private BinaryLight[] binaryLights;

	private DimmerLight[] dimmerLights;

	private CarbonDioxydeSensor[] carbonDioxydeSensors;

	/** Field for alarmService dependency */
	private AlarmService alarmService;

	/** Field for mailSender dependency */
	private MailSender mailSender;

	public GasAlarmSystemApplication() {
		m_lock = new Object();
	}

	@Invalidate
	public void stop() {
		System.out.println(" Gas alarm component stop ... ");
		for (CarbonDioxydeSensor sensor : carbonDioxydeSensors) {
			sensor.removeListener(this);
		}
	}

	@Validate
	public void start() {
		System.out.println(" Gas alarm component start ... ");
	}

	public void bindCarbonDioxydeSensor(CarbonDioxydeSensor carbonDioxydeSensor, Map properties) {
		carbonDioxydeSensor.addListener(this);
	}

	public void unbindCarbonDioxydeSensor(CarbonDioxydeSensor carbonDioxydeSensor, Map properties) {
		String message;

		if (carbonDioxydeSensors.length > 0) {
			message = "Fortunately, there is another device that is currently in service and working properly.";
		} else {
			message = "Unfortunately, there are no other alternatives available. We recommend immediate intervention to resolve the problem.";
		}

		mailSender.sendEmail(
				"abdelhalimbelaoud@gmail.com",
				"[iCasa][Important] CO2 Sensor Out of Service",
				"Hello, Mr. Belaoud,\n\n" +
						"The CO2 sensor with the following serial number '" + carbonDioxydeSensor.getSerialNumber() + "' has been detected as out of service in " +
						carbonDioxydeSensor.getPropertyValue("Location") + ".\n\n " + message
		);

		carbonDioxydeSensor.removeListener(this);
	}

	public void deviceAdded(GenericDevice arg0) {
		// do nothing
	}

	public void devicePropertyAdded(GenericDevice arg0, String arg1) {
		// do nothing
	}

	public void devicePropertyModified(GenericDevice device,
			String propertyName, Object oldValue, Object newValue) {

	}

	public boolean checkCo2() {
		for (CarbonDioxydeSensor sensor : carbonDioxydeSensors) {
			synchronized (m_lock) {
				if (sensor.getCO2Concentration() > _CO2Threshold) {
					return true;
				}
			}
		}
		return false;
	}

	public void devicePropertyRemoved(GenericDevice arg0, String arg1) {
		// do nothing
	}

	public void deviceEvent(GenericDevice device, Object data) {

	}

	public void deviceRemoved(GenericDevice arg0) {
		// do nothing
	}

	public long getPeriod() {
		return 30;

	}

	public TimeUnit getUnit() {
		return TimeUnit.SECONDS;
	}

	public void run() {
		if (checkCo2()) {
			if (!state) {
				for (BinaryLight light : binaryLights) {
					light.turnOn();
				}
				for (DimmerLight light : dimmerLights) {
					light.setPowerLevel(1);
				}
				state = true;
			} else {
				for (BinaryLight light : binaryLights) {
					light.turnOff();
				}
				for (DimmerLight light : dimmerLights) {
					light.setPowerLevel(0);
				}
				state = false;
			}
			if (alarmRunning == false) {
				for (GasAlarmListener listener : setOfListener) {
					listener.thresholdCrossUp();
				}
				
				alarmService.fireAlarm();
				
				alarmRunning = true;
			}
			if (!mailSent) {
				mailSender.sendEmail("abdelhalimbelaoud@gmail.com", "[iCasa][Important] High levels of C02 has been detected in the office", 
						"Hello sir Belaoud, high CO2 percentage has been detected !");
				mailSent = true;
			}
		} else {
			if (alarmRunning == true) {

				for (GasAlarmListener listener : setOfListener) {
					listener.thresholdCrossUp();
				}
				
				alarmService.stopAlarm();
				
				alarmRunning = false;
			}
			
			if (mailSent) {
				mailSender.sendEmail("abdelhalimbelaoud@gmail.com", "[iCasa][Important] Update CO2 levels back to normal", 
						"Hello sir Belaoud, CO2 has been stabalized check the app to know details !");
			}
			
			mailSent = false;
		}
	}

	public synchronized void addListener(GasAlarmListener listener) {
		setOfListener.add(listener);
	}

	public synchronized void removeListener(GasAlarmListener listener) {
		setOfListener.remove(listener);
	}

	/** Bind Method for dimmerLights dependency */
	public void bindDimmerLight(DimmerLight dimmerLight, Map properties) {
		// TODO: Add your implementation code here
	}

	/** Unbind Method for dimmerLights dependency */
	public void unbindDimmerLight(DimmerLight dimmerLight, Map properties) {
		// TODO: Add your implementation code here
	}

	/** Bind Method for binaryLights dependency */
	public void bindBinaryLight(BinaryLight binaryLight, Map properties) {
		// TODO: Add your implementation code here
	}

	/** Unbind Method for binaryLights dependency */
	public void unbindBinaryLight(BinaryLight binaryLight, Map properties) {
		// TODO: Add your implementation code here
	}
}