package vpn;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

class Client {
	public static void startClient(String sharedKey) throws Exception {
		Scanner input = new Scanner(System.in);

        String host;
        System.out.println("Server IP:");
		host = input.next();
		
        Socket clientSocket = new Socket(host, 6789);
        
        ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
        ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
        
        if (MutualAuthentication.muAuth(TwoWayVPN.CLIENT, out, in)) {
        	//continue with message
        }

        //clientSocket.close();
	}
} 