package vpn;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.util.Scanner;

import vpn.Authentication.InstanceType;

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
		
		Authentication auth = new Authentication(InstanceType.Server, "localhost", 1234, "sharedsecretabcdef");
		System.out.println("The symmetric key is: " + auth.calcSymmetricKey());
		KeyPair kp = auth.calcAsymmetricKey();
		System.out.println("The public key is: " + kp.getPublic().toString());
		System.out.println("The private key is: " + kp.getPrivate().toString());
		
		ServerSocket ss = new ServerSocket(1234);
		System.out.println("Waiting");
		Socket s = ss.accept();
		System.out.println("Connected");
		ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
		PrintStream ps = new PrintStream(os);
		String message = "Sup";
		BigInteger plaintext = new BigInteger(message.getBytes());
	    BigInteger ciphertext = rsa.encrypt(plaintext);
		String message2 = new String(ciphertext.toByteArray());
		
		System.out.println("Preparing to send data: " + "\"" + message + "\"");
		
		/* TEST */
		auth.setOutputStream(os);
		//auth.sendPublicKey();
		// encrypt data
		System.out.println("Encrypting " + "\"" + message + "\"...");
		byte[] encryptedData = auth.encryptText(message);
		System.out.println("Encrypted result is: " + encryptedData);
		
		// sign data
		System.out.println("Signing encrypted data...");
		byte[] signedEncryptedData = auth.sign(encryptedData);
		System.out.println("Signed result is: " + signedEncryptedData);
		
		// send data
		auth.sendMessage(signedEncryptedData);
		System.out.println("Sending signed encrypted data: " + signedEncryptedData);
		/* END TEST */
		
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
		
		Authentication auth = new Authentication(InstanceType.Client, "localhost", port, "sharedsecretabcdef");

		Socket s = new Socket("localhost", port);
		ObjectInputStream is = new ObjectInputStream(s.getInputStream());
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String ciphertext;
		while((ciphertext = br.readLine())!= null) {
			BigInteger plaintext = rsa.decrypt(new BigInteger(ciphertext.getBytes()));
		    String message = new String(plaintext.toByteArray());
			System.out.println(message);
		}
		
		/* TEST */
		auth.setInputStream(is);
		//auth.receivePublicKey();
		byte[] signedEncryptedData = auth.getReceivedBytes();
		byte[] encryptedData = new byte[0];
		String message = "";
		
		System.out.println("Preparing to unsign " + auth.getReceivedBytes());
		// unsign encrypted data
		System.out.println("Unsigning " + signedEncryptedData + "...");
		try {
			encryptedData = auth.unsign(signedEncryptedData);
			System.out.println("Unsigned result is: " + encryptedData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
		}
		// decrypt data
		System.out.println("Decrypting " + encryptedData + "...");
		try {
			encryptedData = auth.unsign(signedEncryptedData);
			message = auth.decryptText(encryptedData);
			System.out.println("Decrypting encrypted data to be: " + "\"" + message + "\"");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
		}
		/* END TEST */
		
		br.close();
		s.close();
		input.close();
	}

} 