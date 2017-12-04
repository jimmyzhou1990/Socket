package socket.server.service;

import org.hibernate.Session;
import org.hibernate.Transaction;

import socket.server.utils.HibernateUtils;

public class ClientsManagerRunnable implements Runnable{

	ClientsManager myself;
	
	public ClientsManagerRunnable(ClientsManager myself)
	{
		this.myself = myself;
	}
	
	public void run() {
		while (true)
		{
			/*try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("updateDatabase...");
			//myself.updateDatabase();
			
			Session session = HibernateUtils.openCurrentSession();
			Transaction tx = session.beginTransaction();
			
			tx.commit();*/
		}
	}
}
