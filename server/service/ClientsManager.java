package socket.server.service;

import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.google.gson.Gson;

import socket.server.domain.ClientBean;



public class ClientsManager {
	
	private LinkedList<ClientBean> Clients;
	private Lock lock;
	
	public enum Command{
		isAdded,
		
	};
	
	public ClientsManager() {
		super();
		//Clients = (LinkedList<ClientBean>) Collections.synchronizedList(new LinkedList<ClientBean>());
		Clients = new LinkedList<ClientBean>();
		lock = new ReentrantLock();
	}
	
	/*
	 *  该类中所有操作都是互斥的
	 * */
	public void topEntry(Command cmd, String clientIP, String result) {
		lock.lock();
		
		switch (cmd) {
			case isAdded:
				//result = isClientAdded(clientIP);
				break;
				
			default:
				break;
		}
		
		lock.unlock();
	}

	//判断客户端是否被管理
	public boolean isClientAdded(String newIP)
	{
		//ip是否已存在
		String addedIP;
		
		for(Iterator<ClientBean> iter = Clients.iterator(); iter.hasNext();)
		{
			addedIP = ((ClientBean)iter.next()).getIp();
			
			System.out.println(addedIP + "    vs   " + newIP);
			if (newIP.equals(addedIP))
			{
				System.out.println("The ip <" + newIP + "> is exist!");
				return true;
			}
		}
		
		return false;	
	}
	
	//返回客户端在链表中的位置
	private int getPosition(String clientIP) {
		
		int position = 0;
		String addedIP;
		
		for(Iterator<ClientBean> iter = Clients.iterator(); iter.hasNext();)
		{
			addedIP = ((ClientBean)iter.next()).getIp();
			if (addedIP.equals(clientIP))
			{
				System.out.println("The ip <" + clientIP + "> is exist!");
				return position;
			}
			position++;
		}
		
		return -1;
	}
	
	//返回client对象
	private ClientBean getClient(String clientIP) {
		
		ClientBean client = null;
		
		
		for(Iterator<ClientBean> iter = Clients.iterator(); iter.hasNext();)
		{
			client = (ClientBean)iter.next();
			if (client.getIp().equals(clientIP))
			{
				break;
			}
		}
		
		return client;
	}
	
	
	//添加客户端,添加时设为OFFLINE,不能重复添加
	public boolean addClient(String clientIP)
	{
		if (isClientAdded(clientIP))
		{
			return false;
		}
		else
		{
			ClientBean c = new ClientBean(clientIP);
			System.out.println("add client <" + clientIP + ">");
			Clients.add(c);
			return true;
		}
	}
	
	public void createConnectThread(String clientIP, Socket socket)
	{
		ClientBean client = getClient(clientIP);
		
		if (client != null)
		{
			client.createThread(socket);
		}
	}
	
	//删除客户端,仅不再管理客户端,连接可能仍然存在
	public void removeClient(String clientIP)
	{
		//int position = getPosition(clientIP);
		ClientBean client = getClient(clientIP);
		
		if (client != null)
		{
			client.closeThread();     //关闭线程
			Clients.remove(client);   //删除管理
		}
		
		/*if (position >= 0)
		{
			Clients.remove(position);
		}*/
	}
	
	//若客户端正在被管理,则设置其状态
	public void setClientConnect(String clientIP, String status)
	{
		int position = getPosition(clientIP);
		
		System.out.println("<"+clientIP+"> position: " + position);
		
		if (position >= 0)
		{
			ClientBean c = Clients.get(position);
			
			c.setConnect(status);
			
			Clients.set(position, c);
		}
	}	
	
	
	public void print() 
	{
		ClientBean c;
		
		for(Iterator<ClientBean> iter = Clients.iterator(); iter.hasNext();)
		{
			c = (ClientBean)iter.next();
			System.out.println("<"+c.getIp()+">    "+"con: "+c.getConnect()+"    mem: "+c.getMemory());
		}
		
		System.out.println("Json String:");
		System.out.println(toJsonString());
	}
	
	//转化为Json字符串
	public String toJsonString() {
		String jsonString;
		
		Gson gson = new Gson(); 
		
		jsonString = gson.toJson(Clients);
		
		return jsonString;
	}
	
}
