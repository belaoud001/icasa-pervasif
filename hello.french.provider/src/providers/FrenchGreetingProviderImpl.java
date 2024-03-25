package providers;

import service.Hello;

public class FrenchGreetingProviderImpl implements Hello {

	public void greetClient(String name) {
		// TODO Auto-generated method stub
		System.out.println("Rani dawi meak b fr hh a si " + name);

	}

	/** Component Lifecycle Method */
	public void stop() {
		// TODO: Add your implementation code here
		System.out.println("The french greeting provider is stopping");
	}

	/** Component Lifecycle Method */
	public void start() {
		// TODO: Add your implementation code here
		System.out.println("The french greeting provider is starting");
	}

}
