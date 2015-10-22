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

import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.net.*;


public class TwoWayVPN {
	private static Scanner input = new Scanner(System.in);
	private static String sharedKey = "abcdefghij123456";

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
		String clientSentence;
        String capitalizedSentence;
        String serverSentence;
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(System.in));
        ServerSocket welcomeSocket = new ServerSocket(6789);
        MutualAuthentication mutualAuth = new MutualAuthentication();
        aes AES = new aes(sharedKey);
        
        Socket connectionSocket = welcomeSocket.accept();
        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
        ObjectOutputStream out = new ObjectOutputStream(connectionSocket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(connectionSocket.getInputStream());
        
        System.out.println("Start Mutual Authentication.");
        //1) get I'm Alice from Client
        clientSentence = (String) in.readObject();
        System.out.println("From Client> Im Alice? " + clientSentence);
       
        //2)
        // Send Challenge to Client: Encrypt Rb
  		BigInteger nonce_B = mutualAuth.getNonce("Even");
  		System.out.println("Nonce_B: " + nonce_B); //testing
  		out.writeObject(nonce_B);
  		//String challenge_B = GetChallenge(nonce_A, mutualAuth.GetEncryptedMessage("Bob", nonce_B, ecdh.computePointsToSend(ecdh.generatePrivateKey()), sharedKey)); //this chunk to be sent to the other party
  		//System.out.println("challenge_B: " + challenge_B); //testing
  		
  		//3) Get Ra from Client, Return Encrypt Ra
        String nonce_A = (String) in.readObject();
        System.out.println("From Client> nonce_A " + nonce_A);
        String encryptedNounce_A = AES.encrypt(nonce_A);
        out.writeObject(encryptedNounce_A);
        System.out.println("To Client> encryptedNounce_a " + encryptedNounce_A);
  		
        
        while(true) {
            //Socket connectionSocket = welcomeSocket.accept();
            //BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            //DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            
        	//clientSentence = inFromClient.readLine();
            //capitalizedSentence = clientSentence.toUpperCase() + '\n';
            //outToClient.writeBytes(capitalizedSentence);
            
            serverSentence = inFromServer.readLine();
            capitalizedSentence = serverSentence.toUpperCase();
            outToClient.writeBytes(capitalizedSentence + '\n');
            
            clientSentence = inFromClient.readLine();
            System.out.println("From Client: " + clientSentence);
        }
	}

	private static void startClient() throws Exception {
		String sentence;
        String modifiedSentence;
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        MutualAuthentication mutualAuth = new MutualAuthentication();
        aes AES = new aes(sharedKey);
        
        Socket clientSocket = new Socket("128.189.240.96", 6789);
        
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
        ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
        
        System.out.println("Start Mutual Authentication.");
       
        // 1) I'm Alice
        out.writeObject("I'm Alice");
        
        // 2) Get Rb from Server, Return Encrypt Rb
        String nonce_B = in.readObject().toString();
        System.out.println("From Server> nonce_b " + nonce_B);
        String encryptedNounce_B = AES.encrypt(nonce_B);
        out.writeObject(encryptedNounce_B);
        System.out.println("To Server> encryptedNounce_b " + encryptedNounce_B);
        
        // 3) Send Challenge to Server: Encrypt Ra
        BigInteger nonce_A = mutualAuth.getNonce("Odd");
		System.out.println("Nonce_A: " + nonce_A);
        out.writeObject(nonce_A);
        
        
        
        
		
		//String nonce_B = inFromServer.readLine();
       // System.out.println("From Server: " + nonce_B);
        //String challenge_B = inFromServer.readLine();
        //System.out.println("From Server: " + challenge_B);
		

        while(true){
	        //Socket clientSocket = new Socket("localhost", 6789);
	        
	        //DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
	        //BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	        
	        sentence = inFromUser.readLine();
	        outToServer.writeBytes(sentence);
	        
	        modifiedSentence = inFromServer.readLine();
	        System.out.println("From Server: " + modifiedSentence);
        
        }
        //clientSocket.close();
	}

} 