package vpn;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class MutualAuthentication {
	//Based on Stamp Textbook, pg 323 Figure 9.12

	public static BigInteger getNonce(String OddEven){
		BigInteger bi;
		BigInteger moduloResult = (OddEven.toLowerCase().equals("odd")) ? BigInteger.ONE : BigInteger.ZERO;
		
		System.out.println("moduloResult: " + moduloResult); //testing
		
		Boolean isCorrectNonce = false;
		
		int bitLength = 64; //based on http://security.stackexchange.com/questions/1952/how-long-should-a-random-nonce-be
		Random random = new Random();
		bi = BigInteger.probablePrime(bitLength, random);
		isCorrectNonce = bi.mod(new BigInteger("2")).equals(moduloResult);
		
		if(moduloResult.equals(BigInteger.ZERO) && (isCorrectNonce == false)){
			bi = bi.add(new BigInteger("1"));
		}
		
		System.out.println("bi: " + bi); //testing
		
		return bi;
	}
	
	public static BigInteger IncrementNonce(BigInteger nonce) {
		return nonce.add(new BigInteger("2")); //ensure nonce remains odd or even
	}
	
	public static String GetEncryptedMessage(String userIdentity, BigInteger nonce, coordinates computedG, String sharedKey){
		aes AES = new aes(sharedKey);
		return aes.encrypt(userIdentity + computedG.toString() + nonce.toString());
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
	
	public static void main(String[] args) throws Exception {
	//	BigInteger sharedKey = BigInteger.probablePrime(1024, new SecureRandom()); //temporary
		String sharedKey = "abcdefghij123456";
		System.out.println("testing Mutual Authentication"); //testing
		
		//1) "I'm Alice", R_a
		BigInteger nonce_A = getNonce("Odd");
		System.out.println("Nonce_A: " + nonce_A); //testing
		String initiated_message = GetChallenge(nonce_A, GetEncryptedMessage("Alice", nonce_A, ecdh.computePointsToSend(ecdh.generatePrivateKey()), sharedKey)); //this chunk to be sent to the other party
		System.out.println("Initiated_Message: " + initiated_message); //testing
		//decrypt
		System.out.println("Decryption: "+ DecryptChallenge(initiated_message, sharedKey));
		
		//2) R_b, E("Bob", R_a, B, K_AB)		
		BigInteger nonce_B = getNonce("Even");
		System.out.println("Nonce_B: " + nonce_B); //testing
		String challenge_B = GetChallenge(nonce_A, GetEncryptedMessage("Bob", nonce_B, ecdh.computePointsToSend(ecdh.generatePrivateKey()), sharedKey)); //this chunk to be sent to the other party
		System.out.println("challenge_B: " + challenge_B); //testing
		//decrypt
		System.out.println("Decryption: "+ DecryptChallenge(challenge_B, sharedKey));
		
		//3) E("Alice, R_B, A, K_AB)
		String challenge_A = GetChallenge(null, GetEncryptedMessage("Alice", nonce_B, ecdh.computePointsToSend(ecdh.generatePrivateKey()), sharedKey));
		System.out.println("challenge_A: " + challenge_A); //testing
		//decrypt
		System.out.println("Decryption: "+ DecryptChallenge(challenge_A, sharedKey));
	}

}
