package net.tfobz.chatroom.chat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.SocketException;
import java.util.concurrent.Callable;

public class ChatClientThread implements Callable<Integer> {
	private BufferedReader in = null;
	
	public ChatClientThread(BufferedReader in) {
		this.in = in;
	}
	
	@Override
	public Integer call () throws Exception {
		try {
			while (true) {
				String line = in.readLine();
				System.out.println(line);
			}
		} catch (SocketException e) {
			System.out.println("Connection to ChatServer lost, ignore exception");
		} catch (IOException e) {
			System.out.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return 0;
	}
}
