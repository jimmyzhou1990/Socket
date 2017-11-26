package socket.client.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;

import com.sun.management.OperatingSystemMXBean;

public class SystemRunnable implements Runnable{
	
	private SystemManager myself;
	
	public SystemRunnable(SystemManager myself)
	{
		this.myself = myself;
	}
	
	private void updateMemoryRate()
	{
		int kb = 1024;   
		OperatingSystemMXBean osmxb = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);  
		
		long totalPhysicalMemory = osmxb.getTotalPhysicalMemorySize()/kb; // 总物理内存  
		long freePhysicalMemorySize = osmxb.getFreePhysicalMemorySize() / kb;   
        Double rate = (Double) (1 - freePhysicalMemorySize * 1.0 / totalPhysicalMemory) * 100;  
        String str = rate.intValue() + "%"; 
        
        myself.setMemory(str);
	}
	
	private void updateThreads() {
		String command = "tasklist";
		int totalThread = 0;
		try {
			Process proc = Runtime.getRuntime().exec(command);
			
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			
			while ((bufferedReader.readLine()) != null)  
			{
				totalThread++;
			}
			
			totalThread -= 3;
			
		} catch (IOException e) {
			System.out.println("fail to run "+command);
		} 
		
		myself.setThreads(""+totalThread);
	}
	
	@Override
	public void run() {
		while (true)
		{
			updateMemoryRate();
			updateThreads();
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
