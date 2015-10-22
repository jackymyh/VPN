package vpn;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

class Server {
	public static void startServer(String sharedKey) throws Exception {

        ServerSocket welcomeSocket = new ServerSocket(6789);

        Socket connectionSocket = welcomeSocket.accept();
      
        ObjectOutputStream out = new ObjectOutputStream(connectionSocket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(connectionSocket.getInputStream());
        
        if (MutualAuthentication.muAuth(TwoWayVPN.SERVER, out, in)) {
        	//continue with message
        }
	}
	
} 