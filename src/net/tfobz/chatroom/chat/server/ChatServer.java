package net.tfobz.chatroom.chat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.BindException;
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

import net.tfobz.chatroom.chat.client.ChatClient;

public class ChatServer {
	
	public Object LOCK = new Object();
	
	public static int serverIDCounter = 0;
		
	public static int clientIDCounter = 0;
	
	private static final int MAX_CLIENTS = 50;
	private final int SERVER_ID;
	
	public static final int DEFAULT_PORT = 10000;
	private int port = DEFAULT_PORT;
	
	private ChatClient admin;
	
	public String name = "Main Chatroom";
	public String password = null;
	
	private Scanner consoleIn;
	
	private ServerSocket server;
	
	ExecutorService executer;
	
	ArrayList<ChatServerThread> serverThreads = new ArrayList<ChatServerThread>();
	ArrayList<ChatServer> servers = new ArrayList<ChatServer>();
		
	public ChatServer () throws IOException, NumberFormatException {
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
				servers.add(this);
			} catch (BindException ex) {
				throw new IOException("Port already in use!");
			} catch (NoSuchElementException ex) {
				try {server.close();} catch (Exception ex2) {}
				throw new IOException(ex.getMessage());
			} catch (NumberFormatException ex) {
				throw new NumberFormatException("Port has to be an Integer!");
			} catch (IOException ex) {
				try {server.close();} catch (Exception ex2) {}
				throw new IOException(ex.getMessage());
			}
		}
		SERVER_ID = serverIDCounter++;
	}
	public ChatServer (String name, int port, ChatServerThread admin) {
		this.name = name;
		System.out.println(port);
		try {
			server = new ServerSocket(port);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(port);
		servers.add(this);
		SERVER_ID = serverIDCounter++;
	}
	
	void accept () {
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
		ChatServer s;
		try {
			s = new ChatServer();
			s.accept();
		} catch (NumberFormatException | IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
