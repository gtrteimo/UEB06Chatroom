package net.tfobz.chatroom.chat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {
	
	public static int clientIDCounter = 0;
	
	private static final int MAX_CLIENTS = 50;
	
	public static final int DEFAULT_PORT = 65535;
	private int port = DEFAULT_PORT;
	
	private Scanner consoleIn;
	
	private ServerSocket server;
	
	private ExecutorService executer;
	
//	private ArrayList<Socket> clients = new ArrayList<Socket>();
//	private ArrayList<BufferedReader> inputStreams = new ArrayList<BufferedReader>();
	ArrayList<PrintStream> outputStreams = new ArrayList<PrintStream>();

	
	public ChatServer () {
		
		executer = Executors.newCachedThreadPool();
		consoleIn = new Scanner(System.in);
		boolean repeat = true;
		while (repeat) {
			try {
				int input = 0;
				while (input < 1025 || input > 65535) {
					System.out.print("Geben sie den Port an auf dem der Server laufen soll (1025-65535): ");
					input = consoleIn.nextInt();
				}
				server = new ServerSocket(port);
				System.out.println("Chat server started");
				repeat = false;
			} catch (InputMismatchException e) {
				System.err.println("Port has to be an Integer!");
			} catch (IOException e) {
				System.err.println(e.getMessage());
				try {server.close();} catch (IOException e2) {}
			}
		}
	}
	
	private void accept () {
		try {
			while (true) {
				Socket client = server.accept();
//				BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
				PrintStream out = new PrintStream(client.getOutputStream());
				
//				clients.add(client);
//				inputStreams.add(in);
				outputStreams.add(out);
				
				executer.submit(this, new ChatServerThread(client));
			}
		} catch (IOException e) {
			System.out.println(e.getClass().getName() + ": " + e.getMessage());
		} finally {
			try { server.close(); } catch (Exception e1) { ; }
		}
	}
	
	private void execute (Socket client) {
		BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

		
		String name = in.readLine();
		System.out.println(name + " signed in. " + ChatServer.outputStreams.size() + " users");
	}
	
	public static void main(String[] args) {
		ChatServer s = new ChatServer();
		s.accept();
	}
}
