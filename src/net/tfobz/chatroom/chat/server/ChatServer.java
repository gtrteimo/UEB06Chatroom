package net.tfobz.chatroom.chat.server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ChatServer {
	
	public int clientIDCounter = 0;
	
	private static final int MAX_CLIENTS = 50;
	
	public static final int DEFAULT_PORT = 65535;
	private int port = DEFAULT_PORT;
	
	private Scanner consoleIn;
	
	private ServerSocket server;
	
	protected static ArrayList<PrintStream> outputStreams =	new ArrayList();

	
	public ChatServer () {
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
			} catch (InputMismatchException e) {
				System.err.println("Port has to be an Integer!");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			
			System.out.println("Chat server started");
			while (true) {
				Socket client = server.accept();
				try {
					new ChatServerThread(client).start();
				} catch (IOException e) {
					System.out.println(e.getClass().getName() + ": " + e.getMessage());
				}
			}
		} catch (IOException e) {
			System.out.println(e.getClass().getName() + ": " + e.getMessage());
		} finally {
			try { server.close(); } catch (Exception e1) { ; }
		}
	}
	
		
	public static void main(String[] args) {
		ChatServer s = new ChatServer();
	}
}
