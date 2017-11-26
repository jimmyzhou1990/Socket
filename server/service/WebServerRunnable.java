package socket.server.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class WebServerRunnable implements Runnable{
	
	private String WebServerAddr;
	private ClientsManager clientsManager;
	public WebServerRunnable(String webserverIP, ClientsManager clientsManager) {
		WebServerAddr = webserverIP;
		this.clientsManager = clientsManager;
	}
	
	public void run() {
		
		while (true) 
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				System.out.println("Error in WebServerRunnable: fail to sleep!");
			}
			
			try 
			{
				Socket socket = new Socket(WebServerAddr, 8887);
				socket.setSoTimeout(2000);
					
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter printWriter = new PrintWriter(socket.getOutputStream(),true);
				String result ="";
				WebserverCommand command = new WebserverCommand(result, clientsManager);
				while(true)
				{
					try 
					{
						result = bufferedReader.readLine();
						System.out.println("WebServer say : " + result);
							
						if (result != null)
						{
							command.setCommand(result);
							command.parse();
						}
						else  //连接已断开
						{
							System.out.println("lose connect to webserver!");
						    bufferedReader.close();
						    printWriter.close();
						    socket.close();
						    break;
						}
					}  
					catch (SocketTimeoutException e) {
						// 连接超时
						System.out.println("read form webserver <" + WebServerAddr + "> time out!");
							
						//发送clients状态
						//new TransToWebServerThread(socket, clientsManager.toJsonString());
						String transBuf = clientsManager.toJsonString();
						System.out.println("Send \"" + transBuf + "\"");
						
						printWriter.println(transBuf);
					}
					catch (IOException e) 
					{
						System.out.println("IOException: read from webserver!");
						break;
					}
						
				}
					
			} 
			catch (Exception e) 
			{
					// TODO Auto-generated catch block
					//e1.printStackTrace();
					System.out.println("Cannot connect to <" + WebServerAddr + ">! ");
					System.out.println("  1. The socketserver is added into management ?");
					System.out.println("  2. The the server ip is right?");
					System.out.println("  3. The the server is online?");
			}
	
		}
	}
}
