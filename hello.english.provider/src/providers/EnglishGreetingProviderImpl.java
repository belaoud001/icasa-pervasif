package providers;

import service.Hello;

public class EnglishGreetingProviderImpl implements Hello {

	public void greetClient(String name) {
		// TODO Auto-generated method stub
		System.out.println("Hello si zbi : " + name);
	}

	/** Component Lifecycle Method */
	public void stop() {
		// TODO: Add your implementation code here
		System.out.println("The english hello service is stopping");
	}

	/** Component Lifecycle Method */
	public void start() {
		// TODO: Add your implementation code here
		System.out.println("The english hello service is starting");
	}

}