package main;

import java.net.*;
import java.io.*;



/**
 * 
 * @author Justus Brown
 *
 */

class PalindromeCheckerServer {
	
	private static String incomingResponse = " ";
	private static String outgoingResponse = " ";
	private static final String EMPTY_STRING = "";
	private static final String SERVER_SHUTDOWN_PROMPT = "Server is shutting down...";
	
	/**
	 * Main loop, starting point of a program
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
		ServerSocket ss;
		if (args != null && args.length > 0) {
			ss = new ServerSocket(extractPortValue(args[0]));
		} else {
			ss = new ServerSocket(1221);
		}
		Socket s = ss.accept();
		DataInputStream din = new DataInputStream(s.getInputStream());
		DataOutputStream dout = new DataOutputStream(s.getOutputStream());
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		// Main loop which checks that the string the user sent is a palindrome
		while (!incomingResponse.equals(EMPTY_STRING)) {
			incomingResponse = din.readUTF();
			// The client pressed enter, so close the server
			if (incomingResponse.equals(EMPTY_STRING)) { break; }
			System.out.println("client's input: " + incomingResponse);
			boolean isPalindrome = isPalindrome(incomingResponse);
			//outgoingResponse = br.readLine();
			outgoingResponse = stringFormatter(isPalindrome, incomingResponse);
			dout.writeUTF(outgoingResponse);
			dout.flush();
		}
		dout.writeUTF(SERVER_SHUTDOWN_PROMPT);
		dout.flush();
		
		din.close();
		s.close();
		ss.close();
	}
	
	/**
	 * Prints the format which the client will see upon sending the particular response
	 * @param truthVal the value determining if the stirng is a palindrome or not
	 * @param str the string that was sent
	 * @return the formatted string
	 */
	private static String stringFormatter(boolean truthVal, String str) {
		if (truthVal) {
			return str + " is a palindrome!";
		} else {
			return str + " is not a palindrome.";
		}
	}
	
	/**
	 * Given a string determines if the given string ia a palindrome or not
	 * @param str the string that might or might not be a palindrome.
	 * @return true if and only if the given string is a palindrome
	 */
	private static boolean isPalindrome(String str) {
		str = str.toLowerCase();
		int i1 = 0;
	    int i2 = str.length() - 1;
	    while (i2 > i1) {
	        if (str.charAt(i1) != str.charAt(i2)) { return false; }
	        ++i1;
	        --i2;
	    }
	    return true;
	}
	/**
	 * Given a string which is guarenteed to contain the port value,
	 * returns the port value as an integer
	 * @param str the string that contains the port value
	 * @return the integer which represents the port value
	 */
	public static int extractPortValue(String str) {
		if (str == null || str.length() == 0) { return -1; }
		String portValue = str.substring(str.indexOf("=") + 1);
		return Integer.parseInt(portValue);
	}
}