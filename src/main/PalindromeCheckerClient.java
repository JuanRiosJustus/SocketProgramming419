package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import common.ProgramConstants;

public class PalindromeCheckerClient {

	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException {
		
		InetAddress host = InetAddress.getLocalHost();
        Socket socket = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        
        String arguments = flattenStringArray(args);
		
		// determine if the port argument is present, if so, handle respectively
		if (containsPortOptional(arguments)) {
			socket = new ServerSocket(getPortArgument(arguments));
		} else {
			socket = new ServerSocket(1200);
		}
		
        
        for(int i=0; i<5;i++){
            //establish socket connection to server
            socket = new Socket(host.getHostName(), 9876);
            //write to socket using ObjectOutputStream
            oos = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Sending request to Socket Server");
            if(i==4)oos.writeObject("exit");
            else oos.writeObject(""+i);
            //read the server response message
            ois = new ObjectInputStream(socket.getInputStream());
            String message = (String) ois.readObject();
            System.out.println("Message: " + message);
            //close resources
            ois.close();
            oos.close();
            Thread.sleep(100);
        }
	}
	
	/**
	 * Given and argument that is guaranteed to have a port number optional
	 * returns the value given to the optional.
	 * @param args argument guaranteed to have a port optional
	 * @return the number representing the port that was chosen
	 */
	public static int getPortArgument(String args) {
		String str = args.substring(args.indexOf(ProgramConstants.PORT_ARGUMENT_IDENTIFIER));
		Integer portValue = Integer.valueOf(str.substring(0, str.indexOf(" ")));
		return portValue.intValue();
	}
	
	/**
	 * Given a string array, returns all strings from the given array into 
	 * one string, respective to the order it was seen in the array
	 * @param args the array containing all the strings
	 * @return the string representing the contents of the array
	 */
	public static String flattenStringArray(String[] args) {
		if (args == null || args.length < 1) { return ""; }
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < args.length; i++) {
			sb.append(args[i] + " ");
		}
		return sb.toString();
	}
	/**
	 * Determines if the given string contains the port optional
	 * @param str the strong that is assumed to have a port optional
	 * @return true if and only if the string contains a port optional
	 */
	public static boolean containsPortOptional(String str) {
		if (str == null || str.length() < 1) { return false; }
		return str.contains(ProgramConstants.PORT_ARGUMENT_IDENTIFIER);
	}
}
