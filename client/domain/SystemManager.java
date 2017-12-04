package socket.client.domain;

import com.google.gson.Gson;

public class SystemManager {
	private String memory;
	private String threads;
    
    public void createThread() {
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
	
	public String toJsonString() {
		Gson gson = new Gson(); 
		
		String jsonString = gson.toJson(this);
		
		
		return jsonString;
	}
	
	//由JSON生成Bean, 会调用构造函数
	public static SystemManager fromJsonString(String jsonString)
	{
		Gson gson = new Gson(); 
		SystemManager systemManager = gson.fromJson(jsonString, SystemManager.class); 
		
		return systemManager;
	}
	
}
