package net.tfobz.chatroom.chat.server;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.Callable;

public class ChatServerThread implements Callable<Integer> {

	private static final String[] commands = { "/help", "/logout", "/newServer", "/msg", "/changeColour",
			"/listClients" };

	private static int ClientIDCounter = 0;

	public final int CLIENT_ID;

	private ChatServer owner;
	private Socket client;
	public BufferedReader in;
	public PrintStream out;
	private Color color;
	private String username;
	private String colorUsername;

	public ChatServerThread(ChatServer owner, Socket client) throws IOException, IllegalArgumentException {
		this.owner = owner;
		this.client = client;
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		out = new PrintStream(client.getOutputStream());
		if (owner.password != null && !in.readLine().equals(owner.password)) {
			out.println("Wrong Password!");
			throw new IllegalArgumentException("Wrong Password!");
		}
		CLIENT_ID = ClientIDCounter++;
	}

	@Override
	public Integer call() throws Exception {
		try {

			boolean valid = true;
			do {
				valid = true;
				username = in.readLine().trim();
//	        	System.out.println("Test1");
				synchronized (owner.LOCK) {
					for (ChatServerThread t : owner.serverThreads) {
						if (username.equals(t.username)) {
							valid = false;
						}
					}
				}
//	        	System.out.println("Test2");
				if (valid) {
//		        	System.out.println("Test3");
					out.println("valid");
				} else {
//		        	System.out.println("Test4");
					out.println("invalid");
				}
//	        	System.out.println("Test5");
			} while (!valid);
//        	System.out.println("Test6");
			System.out.println(CLIENT_ID);
			out.println(CLIENT_ID);

			Random rand = new Random();
			color = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
			String colour = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
			colorUsername = "<span style=\"color: " + colour + ";\">" + username + "</span>";

			synchronized (owner.LOCK) {
				owner.serverThreads.add(this);
			}

			System.out.println(username + " signed in. " + owner.serverThreads.size() + " users");

			synchronized (owner.LOCK) {
				for (ChatServerThread t : owner.serverThreads) {
					t.out.println(colorUsername + " signed in");
				}
			}

			readInput();
			signOut();

		} catch (IOException e) {
			signOut();

		} finally {
			try {
				client.close();
			} catch (Exception e1) {
				;
			}
		}
		return 0;
	}

	private void readInput() throws IOException {
		boolean running = true;
		String line = in.readLine();
		while (running) {
			if (line != null && !line.isEmpty()) {
				System.out.println(username + ": " + line);
				if (line.charAt(0) != '/') {
					for (ChatServerThread t : owner.serverThreads) {
						t.out.println(colorUsername + ": " + line);
					}
				} else {
					command(line);
				}
				line = in.readLine();
			}
		}
	}

	private void signOut() {
		synchronized (owner.LOCK) {
			for (ChatServerThread t : owner.serverThreads) {

				t.out.println(colorUsername + " signed out");
			}
			owner.serverThreads.remove(this);
			System.out.println(colorUsername + " signed out. " + owner.serverThreads.size() + " users");
		}
	}

	private void createServer(String prompt) throws NumberFormatException, IOException {
		String[] s = prompt.split(" ");
		ChatServer c = new ChatServer(s[0], Integer.parseInt(s[1]), this);
		owner.servers.add(c);
		Thread t = new Thread(() -> {
			c.accept();
		});
		t.setDaemon(true);
		t.start();
	}

	private void command(String commandString) {
		try {
			if (commandString.equals("/logout")) {
				client.close();
			} else if (commandString.equals("/newServer")) {
				out.println("Creating a new server...");
			} else if (commandString.startsWith("/msg")) {
				synchronized (owner.LOCK) {
					for (ChatServerThread t : owner.serverThreads) {
						int firstSpace = commandString.indexOf(" ");
						if (firstSpace != -1) {
							int secondSpace = commandString.indexOf(" ", firstSpace + 1);
							if (secondSpace != -1) {
								if (t.username.equals(commandString.substring(firstSpace,secondSpace-1))) {
								t.out.println(commandString.substring(secondSpace + 1));
								}
							}
						}
					}
				}
			} else if (commandString.equals("/changeColour")) {
				Random rand = new Random();
				color = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
			} else if (commandString.equals("/listClients")) {
				synchronized (owner.LOCK) {
					for (ChatServerThread t : owner.serverThreads) {
						out.println(t.username);
					}
				}
			} else if (commandString.equals("/help")) {
				out.println("Available commands: /logout, /newServer, /msg, /changeColour, /listClients");
			} else {
				out.println("Unknown Command! Type /help to get a list of commands");
			}
		} catch (IOException e) {
			out.println("An error occurred while executing the command.");
		}
	}

}
