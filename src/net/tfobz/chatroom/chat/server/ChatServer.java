package net.tfobz.chatroom.chat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {
	
	public static int serverIDCounter = 0;
		
	public static int clientIDCounter = 0;
	
	private static final int MAX_CLIENTS = 50;
	private final int SERVER_ID;
	
	public static final int DEFAULT_PORT = 10000;
	private int port = DEFAULT_PORT;
	
	public String name = "Main Chatroom";
	public String password = null;
	
	private Scanner consoleIn;
	
	private ServerSocket server;
	
	private ExecutorService executer;
	
	ArrayList<ChatServerThread> serverThreads = new ArrayList<ChatServerThread>();
	
	public ChatServer () {
		executer = Executors.newCachedThreadPool();
		consoleIn = new Scanner(System.in);
		boolean repeat = true;
		while (repeat) {
			try {
				int input = 0;
				while (input < 1025 || input > 65535) {
					System.out.print("Geben sie den Port an auf dem der Server laufen soll (1025-65535): ");
					input = Integer.parseInt(consoleIn.next().trim().replaceAll("\n", ""));
				}
				port = input;
				server = new ServerSocket(port);
				System.out.println("Chat server started");
				repeat = false;
			} catch (NoSuchElementException e) {
				e.printStackTrace();
				try {server.close();} catch (Exception e2) {}
				System.exit(0);
			} catch (NumberFormatException e) {
				System.out.println("Port has to be an Integer!"); 
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
				try {server.close();} catch (Exception e2) {}
			}
		}
		SERVER_ID = serverIDCounter++;
	}
	public ChatServer (String name) {
		
		SERVER_ID = serverIDCounter++;
	}
	
	private void accept () {
		try {
			while (true) {
				Socket client = server.accept();
				ChatServerThread t = new ChatServerThread(this, client);
				executer.submit(t);
			}
		} catch (IllegalArgumentException e) {
			
		} catch (IOException e) {
			try { server.close(); } catch (Exception e1) { ; }
			e.printStackTrace();
		} 
	}
	
	public static void main(String[] args) {
		ChatServer s = new ChatServer();
		s.accept();
	}
}
