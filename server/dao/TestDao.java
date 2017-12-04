package socket.server.dao;


import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import socket.server.domain.ClientBean;
import socket.server.utils.HibernateUtils;

public class TestDao {
	@Test
	public void test_insert() 
	{
		Session session = HibernateUtils.openSession();
		
		Transaction tx = session.beginTransaction();
		
		ClientBean client = new ClientBean();
		
		client.setIp("192.168.1.2");
		
		session.save(client);
		
		tx.commit();
		session.close();
	}
	
	@Test
	public void test_update() {
		Session session = HibernateUtils.openSession();
		
		Transaction tx = session.beginTransaction();
		
		//ClientBean client = session.get(ClientBean.class, "192.168.1.2");
		ClientBean client = new ClientBean("192.168.1.2");
		client.setMemory("55%");
		client.setThreads("50");
		client.setConnect("offline");
		session.update(client);
		
		tx.commit();
		session.close();
	}
	
	@Test
	public void test_delete() {
		Session session = HibernateUtils.openSession();
		
		Transaction tx = session.beginTransaction();
		
		ClientBean client = session.get(ClientBean.class, "192.168.1.2");
		
		session.delete(client);
		
		//session.saveOrUpdate(client);
		
		tx.commit();
		session.close();		
	}
}
