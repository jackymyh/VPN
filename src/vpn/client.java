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
        aes AES = new aes(sharedKey);
        
        ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
        ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
        
        if (MutualAuthentication.muAuth(TwoWayVPN.CLIENT, out, in, AES)) {
        	while(true){
        		System.out.println("Data to send: (enter exit to quit)");
        		String send = input.next();
        		
        		if (send.equals("exit")) {
        			System.out.println("Client exit");
        			break;
        		}
        	
        		String encryptSend = AES.encrypt(send);
        		out.writeObject(encryptSend);
        		System.out.println("To Server>" + send);
        	}
        }
        in.close();
        out.close();
        clientSocket.close();
	}
} 