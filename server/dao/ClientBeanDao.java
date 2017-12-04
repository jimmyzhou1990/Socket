package socket.server.dao;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.jdbc.Expectations;

import socket.server.domain.ClientBean;
import socket.server.utils.HibernateUtils;

public class ClientBeanDao {
	
	//添加
	public static void add(ClientBean client) {
		
		Session session = HibernateUtils.openSession();
		Transaction tx = session.beginTransaction();
		
		try {
			session.save(client);
			tx.commit();
		}
		catch (Exception e) {
			System.out.println("fail to add <"+client.getIp()+"> to Database!");
			tx.rollback();
		}
		session.close();
	}
	
	//更新
	public static void update(ClientBean client) {
		
		Session session = HibernateUtils.openSession();
		Transaction tx = session.beginTransaction();
		
		session.update(client);
		
		tx.commit();
		session.close();
		
	}
	
	//按照IP查询
	public static ClientBean query(String clientIP) {
		Session session = HibernateUtils.openCurrentSession();
		ClientBean client = null;
		Query query = session.createQuery("from ClientBean where ip = :clietnIP");
		query.setParameter("clietnIP", clientIP);
		
		client = (ClientBean)query.uniqueResult();
		
		return client;
		
	}
	
	public static void remove(String clientIP) {
		Session session = HibernateUtils.openCurrentSession();
		ClientBean client = query(clientIP);
		
		if (client != null)
		{
			session.delete(client);
		}
	}

	//统一方法 save, 若不存在则插入, 若存在则修改
	public static void save(ClientBean client)
	{
		Session session = HibernateUtils.openCurrentSession();
		session.save(client);
	}
	
	public static void saveAll(LinkedList<ClientBean> clients) {
		ClientBean client;
		
		for(Iterator<ClientBean> iter = clients.iterator(); iter.hasNext();)
		{
			client = (ClientBean)iter.next();
			save(client);
		}
	}
	
	public static List<ClientBean> getAll() {
		Session session = HibernateUtils.openCurrentSession();
		
		Query query = session.createQuery("from ClientBean");
		
		@SuppressWarnings("unchecked")
		List<ClientBean> clientList = query.list();
		
		return clientList;
	}
	
}
