package vpn;

import java.util.Scanner;


public class TwoWayVPN {
	private static Scanner input = new Scanner(System.in);
	private static String sharedKey;
	final static int SERVER = 1;
	final static int CLIENT = 2;

	public static void main(String args[]) throws Exception {
		int mode;
		
		System.out.println("Choose mode: 1 for Server, 2 for Client");
		mode = input.nextInt();
		
		System.out.print("Enter 16 byte shared secret value:");
		sharedKey = input.next();
		if (sharedKey.length() < 16) {
			System.out.println("Your key " + sharedKey + " is not 16 byte so we replaced for you :)");
			sharedKey = "anckformv0m2kdks";
		}
		chooseMode(mode, sharedKey);
		
		while(true){
		       
	        
        }
	}

	private static void chooseMode(int mode, String sharedKey) throws Exception {
		if (mode == SERVER) {
			Server.startServer(sharedKey);
		}
		else {
			Client.startClient(sharedKey);
		}
	}

} 