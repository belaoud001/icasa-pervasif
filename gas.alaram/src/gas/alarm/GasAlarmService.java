package gas.alarm;

public interface GasAlarmService {
	
	public void addListener(GasAlarmListener listener);
	
	public void removeListener(GasAlarmListener listener);
	
}
