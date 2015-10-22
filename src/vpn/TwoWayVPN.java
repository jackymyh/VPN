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
		while (mode != SERVER && mode != CLIENT) {
			System.out.println("Choose mode: 1 for Server, 2 for Client");
			mode = input.nextInt();
		}
		
		System.out.println("Enter 16 byte shared secret value:");
		sharedKey = input.next();
		while (sharedKey.length() < 16) {
			System.out.println("Your key is not 16 byte. Try again: ");
			sharedKey = input.next();
		}
		chooseMode(mode, sharedKey);
		
	}

	private static void chooseMode(int mode, String sharedKey) throws Exception {
		if (mode == SERVER) {
			System.out.println("Starting server...");
			Server.startServer(sharedKey);
		}
		else {
			System.out.println("Starting client...");
			Client.startClient(sharedKey);
		}
	}

} 