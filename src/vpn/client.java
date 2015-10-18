package vpn;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

class Client {
	public static void main(String args[]) throws Exception {
		int port;
		 
	    Scanner in = new Scanner(System.in);
	 
	    System.out.println("Enter a port");
	    port = in.nextInt();
		
		Socket s = new Socket("localhost", port);
		InputStream is = s.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String message;
		while((message = br.readLine())!= null) {
			System.out.println(message);
		}
		br.close();
		s.close();
		in.close();
	}
} 