package socket.client.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class SocketClient {
	public static void main(String[] args)
	{	
		
		SystemManager systemManager = new SystemManager();
		
		while (true) {
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try {
				Socket socket = new Socket(args[0], 8888);
				socket.setSoTimeout(1000);

				PrintWriter printWriter = new PrintWriter(socket.getOutputStream(),true);
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
					
				while(true)
				{
					try {
						//System.out.println("test connect...");
						String tmp = bufferedReader.readLine();

						if (tmp == null) //连接已断开
						{
							System.out.println("Error: lose connect to <" + args[0] + "> !");
							printWriter.close();
					        bufferedReader.close();
					        socket.close();
							break;
						}
					}  
					catch (SocketTimeoutException e) {
						// 连接超时
						System.out.println("connect ok");
						String transToServer = "memory: "+systemManager.getMemory() + " threads: " + systemManager.getThreads();
						
						System.out.println(transToServer);
						printWriter.println(transToServer);
					}
					catch (IOException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
						System.out.println("Error: lose connect to <" + args[0] + "> !");
						printWriter.close();
				        bufferedReader.close();
				        socket.close();
				        break;
					}		
				}
				
			} 
			
			catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Error: Cannot setup connect to <" + args[0] + "> !");
			}	
		}
		
	}	
}
