package vpn;

import java.math.BigInteger;
import java.util.Random;

import javax.xml.bind.DatatypeConverter;

public class ecdh {
	//curve p-521 from http://csrc.nist.gov/groups/ST/toolkit/documents/dss/NISTReCur.pdf
	//pseudo-random curve E: y^2 = (x^3)-3x+b (mod p)
	
	//[currently unused:] E coefficient a:
	public static int E_a = -3;
	
	//[currently unused:] E coefficient b:
	public static int E_b = 1;
	
	//[currently unused:] cofactor f (video: h):
	public static int f = 1;
	
	//prime modulus p
	public static BigInteger p = new BigInteger("6864797660130609714981900799081393217269435300143305409394463459185543183397656052122559640661454554977296311391480858037121987999716643812574028291115057151");
	
	//order r (n in the video: https://www.youtube.com/watch?v=F3zzNa42-tQ)
	public static BigInteger r = new BigInteger("6864797660130609714981900799081393217269435300143305409394463459185543183397655394245057746333217197532963996371363321113864768612440380340372808892707005449");
	
	//[currently unused:] 160-bit input seed s to SHA-1 based algorithm
	public static byte[] s = toByteArray("d09e8800291cb85396cc6717393284aaa0da64ba");
	
	//[currently unused:] output c of SHA-1 based algorithm
	public static byte[] c = toByteArray("0b48bfa5f420a34949539d2bdfc264eeeeb077688e44fbf0ad8f6d0edb37bd6b533281000518e19f1b9ffbe0fe9ed8a3c2200b8f875e523868c70c1e5bf55bad637");
	
	//[currently unused:] coefficient b (satisfies (b^2)*c= -27 (mod p))
	public static byte[] b = toByteArray("051953eb9618e1c9a1f929a21a0b68540eea2da725b99b315f3b8b489918ef109e156193951ec7e937b1652c0bd3bb1bf073573df883d2c34f1ef451fd46b503f00");
	
	//base point G x-coordinate
	public static byte[] G_x = toByteArray("c6858e06b70404e9cd9e3ecb662395b4429c648139053fb521f828af606b4d3dbaa14b5e77efe75928fe1dc127a2ffa8de3348b3c1856a429bf97e7e31c2e5bd66");
	
	//base point G y-coordinate
	public static byte[] G_y = toByteArray("11839296a789a3bc0045c8a5fb42c7d1bd998f54449579b446817afbd17273e662c97ee72995ef42640c550b9013fad0761353c7086a272c24088be94769fd16650");
	
	public static byte[] toByteArray(String s) {
		s = ((s.length()%2) > 0) ? ("0" + s) : s; //added check to pad with leading 0 if uneven length
		//references: 
		//http://stackoverflow.com/questions/140131/convert-a-string-representation-of-a-hex-dump-to-a-byte-array-using-java\
		//and http://stackoverflow.com/questions/8890174/in-java-how-do-i-convert-a-hex-string-to-a-byte
		return DatatypeConverter.parseHexBinary(s);
	}
	
	public static BigInteger generatePrivateKey() {
		BigInteger bi;
		
		//re-generate private key if out of range (1 <= privateKey <= r-1)
		do {
			int bitLength = 521; //the length of binary expansion from above PDF
			Random random = new Random();
			bi = BigInteger.probablePrime(bitLength, random);
		} 
		while(!(bi.compareTo(new BigInteger("1")) >= 0 || bi.compareTo(r) <= 0)); 
		
		System.out.println("generated private key is within range"); //testing
		
		return bi;
	}
	
	public static coordinates computePointsToSend(BigInteger privateKey){
		coordinates results = new coordinates();
		BigInteger bi_G_x = new BigInteger(G_x);
		BigInteger bi_G_y = new BigInteger(G_y);
		results.x = privateKey.multiply(bi_G_x);
		results.y = privateKey.multiply(bi_G_y);
		
		return results;
	}
	
	public static coordinates computeSharedSessionKey(BigInteger privateKey, coordinates received) {
		coordinates results = new coordinates();
		results.x = privateKey.multiply(received.x);
		results.y = privateKey.multiply(received.y);
		
		return results;
	}
	
	//for testing purposes:
	public static void main(String args[]) throws Exception {
		BigInteger Bob_beta = generatePrivateKey();
		BigInteger Alice_alpha = generatePrivateKey();
		
//		//test if 1 <= privateKey <= r-1
//		System.out.println(Bob_beta);
//		if(Bob_beta.compareTo(new BigInteger("1")) >= 0 && Bob_beta.compareTo(r) <= 0){
//			System.out.println("Bob_beta is within range"); //testing
//		}
//		
//		System.out.println(Alice_alpha);
//		if(Alice_alpha.compareTo(new BigInteger("1")) >= 0 && Alice_alpha.compareTo(r) <= 0){
//			System.out.println("Alice_alpha is within range"); //testing
//		}
		
		//test the keys methods
		//Alice to Bob:
		coordinates toSend_A = computePointsToSend(Alice_alpha);
		coordinates testResult_BobComputes = computeSharedSessionKey(Bob_beta, toSend_A);
		
		//Bob to Alice:
		coordinates toSend_B = computePointsToSend(Bob_beta);
		coordinates testResult_AliceComputes = computeSharedSessionKey(Alice_alpha, toSend_B);

		//check if same computed results:
		if(testResult_BobComputes.x.compareTo(testResult_AliceComputes.x) == 0){
			System.out.println("x coordinates equal: " + testResult_BobComputes.x); //testing
		}
		
		if(testResult_BobComputes.y.compareTo(testResult_AliceComputes.y) == 0){
			System.out.println("y coordinates equal: " + testResult_BobComputes.y); //testing
		}
	}
	
}
