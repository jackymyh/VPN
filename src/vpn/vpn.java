package vpn;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

class VPN {
	private static Scanner input = new Scanner(System.in);

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
		RSA rsa = new RSA(1024);
		
		ServerSocket ss = new ServerSocket(1234);
		System.out.println("Waiting");
		Socket s = ss.accept();
		System.out.println("Connected");
		OutputStream os = s.getOutputStream();
		PrintStream ps = new PrintStream(os);
		String message = "Sup";
		BigInteger plaintext = new BigInteger(message.getBytes());
	    BigInteger ciphertext = rsa.encrypt(plaintext);
		String message2 = new String(ciphertext.toByteArray());
		ps.println(message2);
		ps.close();
		ss.close();
		s.close();
	}

	private static void startClient() throws Exception {
		int port;
		RSA rsa = new RSA(1024);

		System.out.println("Enter a port");
		port = input.nextInt();

		Socket s = new Socket("localhost", port);
		InputStream is = s.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String ciphertext;
		while((ciphertext = br.readLine())!= null) {
			BigInteger plaintext = rsa.decrypt(new BigInteger(ciphertext.getBytes()));
		    String message = new String(plaintext.toByteArray());
			System.out.println(message);
		}
		br.close();
		s.close();
		input.close();
	}

} 