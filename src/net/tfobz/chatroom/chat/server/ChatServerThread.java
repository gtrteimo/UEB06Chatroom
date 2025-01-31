package net.tfobz.chatroom.chat.server;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.concurrent.Callable;

public class ChatServerThread implements Callable<Integer> {
	
	private static final String[] commands = {
		"/help", "/logout", "/newServer", "/msg", "/changeColour", "/listClients"
	};
	
	private static int ClientIDCounter = 0;
	
	public final int CLIENT_ID;
	
	private ChatServer owner;
	private Socket client;
	public BufferedReader in;
	public PrintStream out;
	private String username;
	private String colorUsername;
	
	public ChatServerThread(ChatServer owner, Socket client) throws IOException, IllegalArgumentException{
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
		        username = in.readLine().trim();
		        synchronized (owner.LOCK) {
		        	for (ChatServerThread t : owner.serverThreads) {
			            if (username.equals(t.username)) {
			            	valid = false;
			            }
			        }
				}		    	
		        if (valid) {
		        	out.println("valid");
		        } else {
		        	out.print("invalid");
		        }
	        } while (!valid);
	        System.out.println(CLIENT_ID);
	        out.println(CLIENT_ID);
	        
	        Color color = Color.RED;
	        String colour = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
	        colorUsername = "<span style=\"color: " + colour + ";\">" + username + "</span>";
	        
	        synchronized (owner.LOCK) {
		        owner.serverThreads.add(this);
			}

	        System.out.println(username + " signed in. " + owner.serverThreads.size() + " users");

	        for (ChatServerThread t : owner.serverThreads) {
	            t.out.println(colorUsername + " signed in");
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
	
	private void signOut () {
    	for (ChatServerThread t : owner.serverThreads) {
			t.out.println(colorUsername + " signed out");
		}
		owner.serverThreads.remove(this);
		System.out.println(username + " signed out. " + owner.serverThreads.size() + " users");
	}
	
	private void command (String commandString) {
		switch (commandString) {
		case "logout":
			try {
				client.close();
			} catch (IOException e) {}
			break;
		case "help":
			
		default:
			out.println("Unknown Command! Type /help to get a list of Commands");
		}
	}
}
