package net.tfobz.chatroom.chat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.concurrent.Callable;

public class ChatServerThread implements Callable<Integer> {
	
	private static int ClientIDCounter = 0;
	
	public final int CLIENT_ID;
	
	private ChatServer owner;
	
	private Socket client;
	
	private BufferedReader in;
	private PrintStream out;
	
	private String name;
	
	public ChatServerThread(ChatServer owner, Socket client) throws IOException {
		CLIENT_ID = ClientIDCounter++;
		this.owner = owner;
		this.client = client;
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		out = new PrintStream(client.getOutputStream());
	}
	
	@Override
	public Integer call() throws Exception {
		try {			
			name = in.readLine();
			out.print(ChatServer.clientIDCounter++);
			if (name.trim().replaceAll("\n", "").isEmpty()) {
				name = "User-"+CLIENT_ID;
				out.print(name);
			}
			
			owner.outputStreams.add(out);
			
			System.out.println(name + " signed in. " + owner.outputStreams.size() + " users");
			
			for (PrintStream outs: owner.outputStreams) {
				outs.println(name + " signed in");
			}
			
			readInput();
			
			signOut();
			
		} catch (IOException e) {
//			System.out.println(e.getClass().getName() + ": " + e.getMessage());
			
//			if (out != null) {
//				owner.outputStreams.remove(out);
//			}
			System.out.println("IOException: "+ e.getMessage());
			signOut();
			
		} finally {
			try { client.close(); } catch (Exception e1) { ; }
		}		
		return 0;
	}
	
	private void readInput () throws IOException {
		String line = in.readLine();
		while (line != null && !line.isEmpty()) {
			System.out.println(name + ": " + line);
			if (line.charAt(0) != '/') {
				for (PrintStream outs: owner.outputStreams) {
					outs.println(name + ": " + line);
				}
			} else {
				command(line.substring(1));
			}
			line = in.readLine();
		}
	}
	
	private void signOut () {
		for (PrintStream outs: owner.outputStreams) {
			outs.println(name + " signed out");
		}
		owner.outputStreams.remove(out);
		System.out.println(name + " signed out. " + owner.outputStreams.size() + " users");
	}
	
	private void command (String commandString) {
		//TODO
	}
}
