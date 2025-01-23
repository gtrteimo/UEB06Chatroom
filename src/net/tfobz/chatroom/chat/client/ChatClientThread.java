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
				System.out.println("Client");
				String line = in.readLine();
				if (textArea.getText() != null && !textArea.getText().isEmpty()) {
					final String text = textArea.getText() + "\n";
					EventQueue.invokeLater(() -> {
						textArea.setText(text + line);
					});
				} else {
					EventQueue.invokeLater(() -> {
						textArea.setText(line);
					});
				}
				
				
				System.out.println(line);
			}
		} catch (SocketException e) {
			System.out.println("Connection to ChatSertry { server.close(); } catch (Exception e1) { ; }ver lost");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
//			System.out.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return 0;
	}
}
