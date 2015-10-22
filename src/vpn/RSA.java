package vpn;

import java.math.BigInteger;
import java.security.SecureRandom;


class RSA {
  private BigInteger n, d, e;
  private int bitlen = 1024;

  public RSA(BigInteger modulus, BigInteger pubKey) {
    n = modulus;
    e = pubKey;
  }

  public RSA(int bits) {
    bitlen = bits;
    SecureRandom r = new SecureRandom();
    BigInteger p = new BigInteger(bitlen / 2, 100, r);
    BigInteger q = new BigInteger(bitlen / 2, 100, r);
    n = p.multiply(q);
    BigInteger m = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
    e = new BigInteger("3");
    while (m.gcd(e).intValue() > 1) {
      e = e.add(new BigInteger("2"));
    }
    d = e.modInverse(m);
  }

  public synchronized String encrypt(String message) {
    return (new BigInteger(message.getBytes())).modPow(e, n).toString();
  }

  public synchronized BigInteger encrypt(BigInteger message) {
    return message.modPow(e, n);
  }

  public synchronized String decrypt(String message) {
    return new String((new BigInteger(message)).modPow(d, n).toByteArray());
  }

  public synchronized BigInteger decrypt(BigInteger message) {
    return message.modPow(d, n);
  }

  public synchronized void generateKeys() {
    SecureRandom r = new SecureRandom();
    BigInteger p = new BigInteger(bitlen / 2, 100, r);
    BigInteger q = new BigInteger(bitlen / 2, 100, r);
    n = p.multiply(q);
    BigInteger m = (p.subtract(BigInteger.ONE)).multiply(q
        .subtract(BigInteger.ONE));
    e = new BigInteger("3");
    while (m.gcd(e).intValue() > 1) {
      e = e.add(new BigInteger("2"));
    }
    d = e.modInverse(m);
  }

  public synchronized void setMod(BigInteger modulus) {
    this.n = modulus;
  }

  public synchronized void setPubKey(BigInteger pubKey) {
	this.e = pubKey;
  }
  
  public synchronized BigInteger getMod() {
	  return this.n;
  }

  public synchronized BigInteger getPubKey() {
	  return this.e;
  }

  /** test */
  public static void main(String[] args) {
    RSA rsa = new RSA(1024);

    String text1 = "omg, this assignment is gay";
    System.out.println("Plaintext: " + text1);
    BigInteger plaintext = new BigInteger(text1.getBytes());

    BigInteger ciphertext = rsa.encrypt(plaintext);
    System.out.println("Ciphertext: " + ciphertext);
    plaintext = rsa.decrypt(ciphertext);

    String text2 = new String(plaintext.toByteArray());
    System.out.println("Plaintext: " + text2);
  }
}
