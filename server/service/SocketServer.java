package socket.server.service;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class SocketServer {
	
	public static void main(String[] args){
		
		ClientsManager clientsManager = new ClientsManager();
		LocalIpAddress localIpAddress = new LocalIpAddress();
		
		if (localIpAddress.get() != null)
		{
			/* 连接webserver */
			new Thread(new WebServerRunnable(args[0], clientsManager)).start();
			
			
			/* 作为Socket服务器接受Client的连接 */
			ServerSocket serverSocket;
			try {
				
				serverSocket = new ServerSocket(8888, 30, localIpAddress.get());
				
				while(true)
				{
					//开始监听
					Socket socket = serverSocket.accept();
					String clientIP = socket.getInetAddress().toString().substring(1);
					
					//如果client正在被管理则创建,否则拒绝连接
					if (clientsManager.isClientAdded(clientIP))
					{
						clientsManager.createConnectThread(clientIP, socket);
					}
					else
					{
						System.out.println("<" + clientIP + "> is not added, refuse to connect!");
						socket.close();
					}	
				}
				
			} catch (IOException e) {
				System.out.println("Cannot create Socket Server!");
			}
		}	
		else
		{
			System.out.println("leave main!");
		}
	}	
}
