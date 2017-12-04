package socket.server.domain;

import java.net.Socket;

import org.hibernate.Session;
import org.hibernate.Transaction;

import socket.server.dao.ClientBeanDao;
import socket.server.utils.HibernateUtils;


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
	
	public ClientBean() {
		
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
	
	public void updateToDatabase() {
		Session session = HibernateUtils.openCurrentSession();
		Transaction tx = session.beginTransaction();
		
		ClientBean client;
		
		try {
			client = ClientBeanDao.query(ip);
			client.setMemory(memory);
			client.setThreads(threads);
			client.setConnect(connect);
		} catch (Exception e) {
			System.out.println("fail to save status to database: "+ip);
			tx.rollback();
		}
		
		tx.commit();
	}
	
	public void saveToDatabase() {
		Session session = HibernateUtils.openCurrentSession();
		Transaction tx = session.beginTransaction();
		
		ClientBean client = null;
		
		try {
			client = ClientBeanDao.query(ip);  //存在则保存
			client.setMemory(memory);
			client.setThreads(threads);
			client.setConnect(connect);
		} catch (Exception e) {
			System.out.println(ip + "is not exsit in database...try to insert it");
			//tx.rollback();
			
			ClientBeanDao.save(this);			
		}
		tx.commit();
		
		
	}
	
	public void removeFromDatabase() {
		/* 从数据库中删除 */
		Session session = HibernateUtils.openCurrentSession();
		Transaction tx = session.beginTransaction();
		
		try {
			ClientBeanDao.remove(ip);
		} catch (Exception e) {
			System.out.println("fail to remove from database: "+ip);
			tx.rollback();
		}
		
		tx.commit();
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
