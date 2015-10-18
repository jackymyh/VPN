package vpn;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

class VPN {
	public static Scanner input = new Scanner(System.in);
		
	public static void main(String args[]) throws Exception {
		int mode;
		 
	    System.out.println("Choose mode: 1 for Server, 2 for Client");
	    mode = input.nextInt();

	    chooseMode(mode);
	}
	
	private static void chooseMode(int mode) throws Exception {
		if (mode == 1) {
			startServer();
		}
		else {
			startClient();
		}
	}
	
	private static void startServer() throws Exception {
		ServerSocket ss = new ServerSocket(1234);
		System.out.println("Waiting");
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
	
	private static void startClient() throws Exception {
		int port;
		
	    System.out.println("Enter a port");
	    port = input.nextInt();
		
		Socket s = new Socket("localhost", port);
		InputStream is = s.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String message;
		while((message = br.readLine())!= null) {
			System.out.println(message);
		}
		br.close();
		s.close();
		input.close();
	}
} 