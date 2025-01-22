package net.tfobz.chatroom.chat.client;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.SocketException;
import java.util.concurrent.Callable;

import javax.swing.JEditorPane;

public class ChatClientThread implements Callable<Integer> {
	
	private BufferedReader in = null;
	private JEditorPane textArea;
	
	public ChatClientThread(BufferedReader in, JEditorPane textArea) {
		this.in = in;
		this.textArea = textArea;
	}
	
	@Override
	public Integer call () throws Exception {
		try {
			while (true) {
				String line = in.readLine();
				EventQueue.invokeLater(() -> {
					textArea.setText("Hallo Welt");
					textArea.setText(textArea.getText() + "\n" + line);
					
				});
				System.out.println(line);
			}
		} catch (SocketException e) {
			System.out.println("Connection to ChatServer lost");
		} catch (IOException e) {
			System.out.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return 0;
	}
}
