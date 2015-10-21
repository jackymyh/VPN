package vpn;

import java.math.BigInteger;

public class coordinates {
	public BigInteger x;
	public BigInteger y;
	
	public String toString() {
		System.out.println("coordinates: " + x.toString() + y.toString()); //testing
		return (x.toString() + y.toString());
	}
}