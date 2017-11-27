package socket.server.domain;

import java.net.Socket;


public class ClientBean {
	private String ip;
	private String connect;
	private String memory;
	private String threads;
	
	private transient ClientRunnable clientRunnable;
	
	
	public ClientBean(String ip) {
		super();
		this.ip = ip;
		this.connect = "offline";
		this.memory = "unknown";
		this.threads = "unknown";
		this.clientRunnable = null;
	}
	
	public void createThread(Socket socket) {
		clientRunnable = new ClientRunnable(socket, this);
		new Thread(clientRunnable).start();  //创建线程 
	}
	
	public void closeThread() {
		if (clientRunnable != null)
		{
			clientRunnable.close();
		}
	}
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getConnect() {
		return connect;
	}
	public void setConnect(String connect) {
		this.connect = connect;
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
