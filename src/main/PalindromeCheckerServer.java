package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ClassNotFoundException;
import java.net.ServerSocket;
import java.net.Socket;

import common.ProgramConstants;

public class PalindromeCheckerServer {
	
	private static ServerSocket server;
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
		String arguments = flattenStringArray(args);
		
		// determine if the port argument is present, if so, handle respectively
		if (containsPortOptional(arguments)) {
			server = new ServerSocket(getPortArgument(arguments));
		} else {
			server = new ServerSocket(1200);
		}
		
        //keep listens indefinitely until receives 'exit' call or program terminates
        while(true){
            System.out.println("Waiting for the client request");
            //creating socket and waiting for client connection
            Socket socket = server.accept();
            //read from socket to ObjectInputStream object
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            //convert ObjectInputStream object to String
            String message = (String)ois.readObject();
            System.out.println("Message Received: " + message);
            //create ObjectOutputStream object
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            //write object to Socket
            oos.writeObject("Hi Client "+message);
            //close resources
            ois.close();
            oos.close();
            socket.close();
            //terminate the server if client sends exit request
            if(message.equalsIgnoreCase("exit")) break;
        }
        System.out.println("Shutting down Socket server!!");
        //close the ServerSocket object
        server.close();
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
