package vpn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
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

//modifications to server based on https://docs.oracle.com/javase/tutorial/networking/sockets/clientServer.html
	private static void startServer() throws Exception {
		RSA rsa = new RSA(1024);
		int portNumber = 1234;
		
		try {
//		try(	
			ServerSocket ss = new ServerSocket(portNumber);
			System.out.println("Waiting"); //comment out if using try-with-resources block
			Socket s = ss.accept();
			System.out.println("Connected"); //comment out if using try-with-resources block
			ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
			PrintStream ps = new PrintStream(os);
//		) {	
			
			String message = "Sup";
			
			//adding FPS Session key with mutual authentication using symmetric key
			//based on Lecture Slides 7 pg 46
			String sharedKey = "abcdefghij123456"; //temporary
			
			BigInteger nonce = MutualAuthentication.getNonce("odd");
			BigInteger privateKey = ecdh.generatePrivateKey();
			
			String challenge = MutualAuthentication.GetChallenge(nonce, MutualAuthentication.GetEncryptedMessage(message, nonce, ecdh.computePointsToSend(privateKey), sharedKey));
			ps.println(challenge);
		} catch(IOException e) {
			System.out.println("Exception caught when listening on port " + portNumber + ": " + e.getMessage());
		}

		
		
		//need to wait for client's response
		
		ps.close();
		ss.close();
		s.close();
		
		
//		//1) "I'm Alice", R_a
//		BigInteger nonce_A = MutualAuthentication.getNonce("Odd");
//		System.out.println("Nonce_A: " + nonce_A); //testing
//		String initiated_message = GetChallenge(nonce_A, GetEncryptedMessage("Alice", nonce_A, ecdh.computePointsToSend(ecdh.generatePrivateKey()), sharedKey)); //this chunk to be sent to the other party
//		System.out.println("Initiated_Message: " + initiated_message); //testing
//		//decrypt
//		System.out.println("Decryption: "+ DecryptChallenge(initiated_message, sharedKey));
//		
//		//2) R_b, E("Bob", R_a, B, K_AB)		
//		BigInteger nonce_B = getNonce("Even");
//		System.out.println("Nonce_B: " + nonce_B); //testing
//		String challenge_B = GetChallenge(nonce_A, GetEncryptedMessage("Bob", nonce_B, ecdh.computePointsToSend(ecdh.generatePrivateKey()), sharedKey)); //this chunk to be sent to the other party
//		System.out.println("challenge_B: " + challenge_B); //testing
//		//decrypt
//		System.out.println("Decryption: "+ DecryptChallenge(challenge_B, sharedKey));
//		
//		//3) E("Alice, R_B, A, K_AB)
//		String challenge_A = GetChallenge(null, GetEncryptedMessage("Alice", nonce_B, ecdh.computePointsToSend(ecdh.generatePrivateKey()), sharedKey));
//		System.out.println("challenge_A: " + challenge_A); //testing
//		//decrypt
//		System.out.println("Decryption: "+ DecryptChallenge(challenge_A, sharedKey));
		
		
		
		
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
		ObjectInputStream is = new ObjectInputStream(s.getInputStream());
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