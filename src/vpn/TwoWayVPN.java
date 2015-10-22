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
	private static String sharedKey;
	final static int SERVER = 1;
	final static int CLIENT = 2;

	public static void main(String args[]) throws Exception {
		int mode;
		
		System.out.println("Choose mode: 1 for Server, 2 for Client");
		mode = input.nextInt();
		
		System.out.print("Enter shared secret value:");
		sharedKey = input.next();
		
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