package socket.server.service;

import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;
//import java.util.concurrent.locks.Lock;
//import java.util.concurrent.locks.ReentrantLock;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.google.gson.Gson;

import socket.server.dao.ClientBeanDao;
import socket.server.domain.ClientBean;
import socket.server.utils.HibernateUtils;



public class ClientsManager {
	
	private LinkedList<ClientBean> Clients;
	//private Lock lock;
	
	public ClientsManager() {
		super();
		Clients = new LinkedList<ClientBean>();
		initfromDatabase();
		
		//ClientsManagerRunnable runnable = new ClientsManagerRunnable(this);
		//new Thread(runnable).start();
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
			ClientBean client = new ClientBean(clientIP);
			System.out.println("add client <" + clientIP + ">");
			Clients.add(client);
			client.saveToDatabase();
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
			client.removeFromDatabase();
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
	
	//从数据库初始化
	public void initfromDatabase() {
		Session session = HibernateUtils.openCurrentSession();
		Transaction tx = session.beginTransaction();
		
		List<ClientBean> clientList = ClientBeanDao.getAll();
		
		for(Iterator<ClientBean> iter = clientList.iterator(); iter.hasNext();)
		{
			ClientBean c = (ClientBean)iter.next();//从数据库读取的对象
			ClientBean client = new ClientBean(c.getIp()); //新建对象
			Clients.add(client);  //添加到管理
		}
		
		tx.commit();
	}
	
}
