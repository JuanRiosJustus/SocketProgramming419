package main;

import java.net.*;
import java.io.*;

class PalindromeCheckerClient {
	
	private static Socket ser;
	private final static String DEFAULT_SERVER = "localhost";
	private final static int DEFAULT_PORT = 1200;
	
	private static String incomingResponse = ".";
	private static String outgoingResponse = ".";
	private static final String portIdentifier = "port";
	private static final String EMPTY_STRING = "";
	private static final String CLIENT_SHUTDOWN_PROMPT = "Client is shutting down...";
	private static final String USER_PROMPT = "Enter in words followed by enter to determine if it is a palindrome";
	
	/**
	 * Main loop, starting point of program
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
		if (args == null || args.length < 1) {
			ser = new Socket(DEFAULT_SERVER, DEFAULT_PORT);
		} else if (containsOption(args, portIdentifier) == false && args.length == 1) {
			// contains no port but contains the address
			ser = new Socket(args[0], DEFAULT_PORT);
		} else if (containsOption(args, portIdentifier) && args.length == 1) {
			// contains no address but contains port
			System.out.println("Just using the local host");
			ser = new Socket(DEFAULT_SERVER, extractPortValue(args[0]));
		} else {
			// contains bot address and the port, address must be before port
			// assume address if last, port is first
			ser = new Socket(args[1], extractPortValue(args[1]));
		}
		// boiler plate socket streams
		DataInputStream din = new DataInputStream(ser.getInputStream());
		DataOutputStream dout = new DataOutputStream(ser.getOutputStream());
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		// System prompts user to enter in a word to test if is a palindrome
		System.out.println(USER_PROMPT);
		// Continue the loop until the user enters stop
		while (!outgoingResponse.equals(EMPTY_STRING)) {
			outgoingResponse = br.readLine();
			dout.writeUTF(outgoingResponse);
			dout.flush();
			incomingResponse = din.readUTF();
			System.out.println(incomingResponse);
		}
		System.out.println(CLIENT_SHUTDOWN_PROMPT);
		dout.close();
		ser.close();
	}
	
	/**
	 * Checks thats the given array contains the option/string identifier
	 * @param params the array of parameters
	 * @param option the name of the options that may be in the string
	 * @return true if and only if the option was found within the array
	 */
	private static boolean containsOption(String[] params, String option) {
		if (params == null || option == null || params.length < 1) {
			return false;
		} else {
			for (int i = 0; i < params.length; i++) {
				if (params[i].contains(option)) { return true; }
			}
			return false;
		}
	}
	
	/**
	 * Given a string which is guarenteed to contain the port value,
	 * returns the port value as an integer
	 * @param str the string that contains the port value
	 * @return the integer which represents the port value
	 */
	private static int extractPortValue(String str) {
		if (str == null || str.length() == 0) { return -1; }
		String portValue = str.substring(str.indexOf("=") + 1);
		return Integer.parseInt(portValue);
	}
}