package clients;

import service.Hello;
import java.util.Map;

public class HelloClientImpl implements Runnable {

	/** Field for greetingServices dependency */
	private Hello[] greetingServices;

	/** Bind Method for greetingServices dependency */
	public void bindGreetingServices(Hello hello, Map properties) {
		// TODO: Add your implementation code here
	}

	/** Unbind Method for greetingServices dependency */
	public void unbindGreetingServices(Hello hello, Map properties) {
		// TODO: Add your implementation code here
	}

	private void askProvidersToSayHello() {
        for (int i = 0; i < greetingServices.length; i++) {
            greetingServices[i].greetClient("zbi");
        }
    }
 
    /**
     * When m_end is false and the component is started, the component ask
     * providers to say hello on a regular basis. When m_end is true, the thread
     * is stopped
     */
    private boolean m_end = false;
 
    /** Component Lifecycle Method */
    public void start() {
        Thread t = new Thread(this);
        m_end = false;
        t.start();
    }
 
    /** Component Lifecycle Method */
    public void stop() {
        m_end = true;
    }
 
    public void run() {
        try {
            while (!m_end) {
                askProvidersToSayHello();
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            stop();
        }
    }

}
