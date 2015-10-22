package vpn;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class aes {

	private static String encryptKey;
	/*public static void main(String args[]) {
	    aes t = new aes("password92343456");
	    String encrypt = t.encrypt("mypasswor42342dftgyhu");
	    System.out.println("decrypted value:" + t.decrypt(encrypt));
	}*/
	public aes(String key){
		this.encryptKey = key;
		
	}
	public String encrypt(String text){
		
		try {
		byte[] key = encryptKey.getBytes();
	//	System.out.println("This is the plaintext:" + text);

		Cipher cipher = Cipher.getInstance("AES");
		SecretKeySpec secretkey = new SecretKeySpec(key,"AES");
		cipher.init(Cipher.ENCRYPT_MODE, secretkey);
		String encrypted = new Base64().encodeAsString(cipher.doFinal(text.getBytes()));
	//	System.out.println("This is the encryption:" + encrypted);
		return encrypted;
			
			
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String decrypt(String encrypted){
		try {
			//System.out.println("This is the test:" + encrypted);;
			Key key = new SecretKeySpec(this.encryptKey.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] decrypted = cipher.doFinal(new Base64().decode(encrypted));
			String decrypt = new String(decrypted);
			return decrypt;
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
}
