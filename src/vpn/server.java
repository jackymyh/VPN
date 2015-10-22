package vpn;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

class Server {
	public static void startServer(String sharedKey) throws Exception {

        ServerSocket welcomeSocket = new ServerSocket(6789);

        Socket connectionSocket = welcomeSocket.accept();
        aes AES = new aes(sharedKey);
      
        ObjectOutputStream out = new ObjectOutputStream(connectionSocket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(connectionSocket.getInputStream());
        
        if (MutualAuthentication.muAuth(TwoWayVPN.SERVER, out, in, AES)) {
        	System.out.println("Waiting for client message...");
        	while(true){
        		String encryptedIn = (String) in.readObject();
        		String decryptedIn = AES.decrypt(encryptedIn);
        		System.out.println("From Client> " + decryptedIn);
        		if (decryptedIn.equals("exit")) {
        			System.out.println("Client left");
        			System.out.println("Server closing");
        			break;
        		}
        	}
        }
        
        in.close();
        out.close();
        connectionSocket.close();
        welcomeSocket.close();
	}
	
} 