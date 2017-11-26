package socket.server.service;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LocalIpAddress {
	private InetAddress localhostAddr;
	
	public InetAddress get() {
		return this.localhostAddr;
	}

	public LocalIpAddress() {
		localhostAddr = getLocalhostAddr();
	}
	
	//获取本机ip方法
	private InetAddress getLocalhostAddr() {
		InetAddress bindAddr;
		try {
			bindAddr = InetAddress.getLocalHost();
			return bindAddr;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("Error! cannot get localhost ip!");
			return null;
		}
		
	}
}
