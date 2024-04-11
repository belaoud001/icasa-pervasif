package commands;

import enums.EnergyGoal;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Requires;

import administrations.FollowMeAdministration;

import enums.IlluminanceGoal;
import fr.liglab.adele.icasa.command.handler.Command;
import fr.liglab.adele.icasa.command.handler.CommandProvider;

@Component
@Instantiate(name = "commands")
@CommandProvider(namespace = "commands")
public class FollowMeCommand {
	
    @Requires
    private FollowMeAdministration followMeAdministration;
    
    @Command
    public void getIlluminancePreference(){
        System.out.println("The illuminance goal is : " + followMeAdministration.getIlluminancePreference());
    }

    @Command
    public void setIlluminancePreference(String illuminancePreference) {
    	IlluminanceGoal illuminanceGoal = IlluminanceGoal.getIlluminanceGoalByTag(illuminancePreference);
        
    	if (illuminanceGoal == null)
        	System.err.println("Invalid value. Please choose one of the following: 'SOFT', 'MEDIUM', 'FULL'");
        else {
        	followMeAdministration.setIlluminancePreference(illuminanceGoal);
            System.out.println("Illuminance goal has been set successfully to '" + illuminancePreference + "'!");
        }
    }

    @Command
    public void getEnergyGoal(){
        System.out.println("The energy goal is : " + followMeAdministration.getEnergyGoal());
    }

    @Command
    public void setEnergySavingGoal(String energySavingGoal) {
        EnergyGoal energyGoal = EnergyGoal.getEnergyGoalByTag(energySavingGoal);

        if (energyGoal == null)
            System.err.println("Invalid value. Please choose one of the following: 'LOW', 'MEDIUM', 'HIGH'");
        else {
            followMeAdministration.setEnergySavingGoal(energyGoal);
            System.out.println("Energy goal has been set successfully to '" + energySavingGoal + "'!");
        }
    }
	
    @Command
    public void test(){
        System.out.println("teste");
    }
}
