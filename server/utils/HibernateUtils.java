package socket.server.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {
	
	private static SessionFactory sessionFactory;
	
	
	//static代码块在加载类时执行一次
	static {
		//创建config对象并读取配置文件
		Configuration conf = new Configuration().configure();
		
		//创建factory对象
		sessionFactory = conf.buildSessionFactory();
	}
	
	public static Session openSession() {
		Session session = sessionFactory.openSession();
		
		return session;
	}
	
	public static Session openCurrentSession()
	{
		Session session = sessionFactory.getCurrentSession();
		
		return session;
	}
	
	
}
