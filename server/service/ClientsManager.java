package socket.server.service;

import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;
//import java.util.concurrent.locks.Lock;
//import java.util.concurrent.locks.ReentrantLock;

import com.google.gson.Gson;

import socket.server.domain.ClientBean;



public class ClientsManager {
	
	private LinkedList<ClientBean> Clients;
	//private Lock lock;
	
	public ClientsManager() {
		super();
		Clients = new LinkedList<ClientBean>();
		//lock = new ReentrantLock();
	}

	//判断客户端是否被管理
	public boolean shouldAdd(String clientIP)
	{
		//ip是否已存在
		ClientBean client;
		
		client = getClient(clientIP);
		
		if (client != null)
		{
			if (client.getConnect() != "online")
			{
				return true;
			}
		}
		
		return false;	
	}
	
	//返回client对象
	private ClientBean getClient(String clientIP) {
		
		ClientBean client;
		
		
		for(Iterator<ClientBean> iter = Clients.iterator(); iter.hasNext();)
		{
			client = (ClientBean)iter.next();
			if (client.getIp().equals(clientIP))
			{
				return client;
			}
		}
		
		return null;
	}
	
	
	//添加客户端,添加时设为OFFLINE,不能重复添加
	public boolean addClient(String clientIP)
	{
		if (getClient(clientIP) != null)
		{
			return false;   //存在则直接返回
		}
		else
		{
			ClientBean c = new ClientBean(clientIP);
			System.out.println("add client <" + clientIP + ">");
			Clients.add(c);
			return true;
		}
	}
	
	//创建连接线程
	public void createConnectThread(String clientIP, Socket socket)
	{
		ClientBean client = getClient(clientIP);
		
		if (client != null)
		{
			client.createThread(socket);
		}
	}
	
	//删除客户端, 关闭线程
	public void removeClient(String clientIP)
	{
		ClientBean client = getClient(clientIP);
		
		if (client != null)
		{
			client.closeThread();     //关闭线程
			Clients.remove(client);   //删除管理
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
