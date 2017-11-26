package socket.server.service;

import java.util.StringTokenizer;


public class WebserverCommand {
	private String commandLine;
	private ClientsManager clientsManager;
	
	private static final String cmdADD = "add";
	private static final String cmdREMOVE = "remove";
	private static final String cmdQUERYSC = "sc";
	private static final String objSocketClientName = "sc";
	
	public WebserverCommand(String cmd, ClientsManager clientsManager) {
		commandLine = cmd;
		this.clientsManager = clientsManager;
	}
	
	public void setCommand(String line) {
		commandLine = line;
	}
	
	public void parse() {
		String[] command = {"cmd", "object", "value"};
		int i = 0;
		StringTokenizer st = new StringTokenizer(commandLine, " "); 
		
        while(st.hasMoreElements()) {  
        	command[i++] = st.nextElement().toString();
        	if (i >= 3)    break;  //只接受3个单词
        } 
        
		switch (command[0])
		{
			case  cmdADD:
				switch (command[1]) 
				{
					case objSocketClientName:
						if (!clientsManager.addClient(command[2]))
						{
							System.out.println("fail to add "+command[2]);
						}
						break;

					default:
						break;
				}
				break;
			
			case cmdREMOVE:
				switch (command[1]) 
				{
					case objSocketClientName:
						clientsManager.removeClient(command[2]);
						break;

					default:
						break;
				}					
				break;
			
			case cmdQUERYSC:
				switch (command[1])
				{
					case "???":
						//发送Clients状态
						//new TransToWebServerThread(socket, clientManager.toJsonString());
						break;
				}
				break;
		}
	}
}
