package vpn;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;

public class MutualAuthentication {
	//Based on Stamp Textbook, pg 323 Figure 9.12

	public static BigInteger getNonce(String OddEven){
		BigInteger bi;
		BigInteger moduloResult = (OddEven.toLowerCase().equals("odd")) ? BigInteger.ONE : BigInteger.ZERO;
		
		//System.out.println("moduloResult: " + moduloResult); //testing
		
		Boolean isCorrectNonce = false;
		
		int bitLength = 64; //based on http://security.stackexchange.com/questions/1952/how-long-should-a-random-nonce-be
		Random random = new Random();
		bi = BigInteger.probablePrime(bitLength, random);
		isCorrectNonce = bi.mod(new BigInteger("2")).equals(moduloResult);
		
		if(moduloResult.equals(BigInteger.ZERO) && (isCorrectNonce == false)){
			bi = bi.add(new BigInteger("1"));
		}
		
		//System.out.println("bi: " + bi); //testing
		
		return bi;
	}
	
	public static BigInteger IncrementNonce(BigInteger nonce) {
		return nonce.add(new BigInteger("2")); //ensure nonce remains odd or even
	}
	
	public static String GetEncryptedMessage(String userIdentity, BigInteger nonce, coordinates computedG, String sharedKey){
		aes AES = new aes(sharedKey);
		return AES.encrypt(userIdentity + computedG.toString() + nonce.toString());
	}
	public static String DecryptChallenge(String challenge, String sharedKey){
		aes AES = new aes(sharedKey);
		System.out.println(challenge);
		String message = AES.decrypt(challenge);
		return message;
	}
	
	public static String GetChallenge(BigInteger nonce, String message){
		return (message);
	}
	
	public static boolean muAuth(int type, ObjectOutputStream out, ObjectInputStream in, aes AES) throws ClassNotFoundException, IOException{
		if (type == TwoWayVPN.SERVER){
	        
			String clientSentence;
			boolean auth = false;

			System.out.println("Start Mutual Authentication.");
			
	        //1) get I'm Alice from Client
	        clientSentence = (String) in.readObject();
	        System.out.println("From Client> " + clientSentence);
	       
	        //2) Send Challenge to Client: Encrypt Rb
	  		BigInteger nonce_B = getNonce("Even");
//	  		System.out.println("Nonce_B: " + nonce_B); //testing
	  		out.writeObject(nonce_B);
	  		
	  		//3) Receive encrypted Rb and verify 
	  		String encryptedNonce_B = (String) in.readObject();
//	  		System.out.println("From Client> EncryptedNonce_B: " + encryptedNonce_B);
	  		String answer = AES.decrypt(encryptedNonce_B);

	  		if (!answer.equals(nonce_B.toString())) {
	  			System.out.println("Client failed authentication");
	  			return auth;
	  		}
	  		
	  		//4) Get Ra from Client, Return Encrypt Ra
	        String nonce_A = in.readObject().toString();
//	        System.out.println("From Client> Nonce_A: " + nonce_A);
	        String encryptedNonce_A = AES.encrypt(nonce_A);
	        out.writeObject(encryptedNonce_A);
//	        System.out.println("To Client> EncryptedNonce_A: " + encryptedNonce_A);
	        
	        auth = true;
	        System.out.println("Authentication Success");
	        return auth;
	        
		}
		else {

			boolean auth = false;
			
			System.out.println("Start Mutual Authentication.");
		       
	        // 1) I'm Alice
	        out.writeObject("I'm Alice");
	        
	        // 2) Get Rb from Server, Return Encrypt Rb
	        String nonce_B = in.readObject().toString();
	        //System.out.println("From Server> Nonce_B: " + nonce_B);
	        String encryptedNounce_B = AES.encrypt(nonce_B);
	        out.writeObject(encryptedNounce_B);
	        //System.out.println("To Server> EncryptedNounce_B: " + encryptedNounce_B);
	        
	        // 3) Send Challenge to Server: Encrypt Ra
	        BigInteger nonce_A = getNonce("Odd");
			//System.out.println("Nonce_A: " + nonce_A);
	        out.writeObject(nonce_A);
	        
	        //4) Receive encrypted Ra and verify 
	  		String encryptedNounce_A = (String) in.readObject();
	  		//System.out.println("From Server> EncryptedNounce_A: " + encryptedNounce_A);
	  		String answer = AES.decrypt(encryptedNounce_A);

	  		if (!answer.equals(nonce_A.toString())) {
	  			System.out.println("Server failed authentication");
	  			return auth;
	  		}
	        
	        auth = true;
	        System.out.println("Authentication Success");
	        return auth;
		}
	}

}
