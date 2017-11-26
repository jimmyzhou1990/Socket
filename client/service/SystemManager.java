package socket.client.service;

public class SystemManager {
	private String memory;
	private String threads;
	
    public SystemManager() {
    	SystemRunnable systemRunnable = new SystemRunnable(this);
    	new Thread(systemRunnable).start();
	}
	
	public String getMemory() {
		return memory;
	}
	public void setMemory(String memory) {
		this.memory = memory;
	}
	public String getThreads() {
		return threads;
	}
	public void setThreads(String threads) {
		this.threads = threads;
	}
}
