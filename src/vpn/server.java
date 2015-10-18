package vpn;

import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

class Server {
	public static void main(String args[]) throws Exception {
		ServerSocket ss = new ServerSocket(1234);
		Socket s = ss.accept();
		System.out.println("Connected");
		OutputStream os = s.getOutputStream();
		PrintStream ps = new PrintStream(os);
		String message = "Sup";
		String message2 = "Peace out";
		ps.println(message);
		ps.println(message2);
		ps.close();
		ss.close();
		s.close();
	}
} 