package vpn;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
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
	public String computemac (String text, String key){

		try {
			Mac mac = Mac.getInstance("HmacMD5");
			SecretKeySpec secretkey = new SecretKeySpec(key.getBytes(),"AES");
			mac.init(secretkey);
			mac.update(new Base64().decode(text));
			String macmessage = new Base64().encodeAsString((mac.doFinal()));
			return macmessage;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
		String macmess = computemac(encrypted,encryptKey);
		return encrypted+macmess;
			
			
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
			String ciphertext = encrypted.substring(0, encrypted.length()-25);
			String ciphermac = encrypted.substring(encrypted.length()-24,encrypted.length());
			Key key = new SecretKeySpec(this.encryptKey.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			if(ciphermac.equals(computemac(ciphertext,encryptKey))){
				byte[] decrypted = cipher.doFinal(new Base64().decode(encrypted));
				String decrypt = new String(decrypted);
			return decrypt;
			}else {
				System.out.println("Message has been compromised, shutting down");
				return null;
			}

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
