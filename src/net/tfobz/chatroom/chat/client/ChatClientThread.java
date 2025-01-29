package net.tfobz.chatroom.chat.client;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.SocketException;
import java.util.concurrent.Callable;

import javax.swing.JEditorPane;

public class ChatClientThread implements Callable<Integer> {
	
	private ChatClient owner;
	private BufferedReader in = null;
	private JEditorPane textArea;
	
	public ChatClientThread(ChatClient owner, BufferedReader in, JEditorPane textArea) {
		this.owner = owner;
		this.in = in;
		this.textArea = textArea;
	}
	
	@Override
	public Integer call () throws Exception {
		try {
			String line = "";
			while (line!=null) {
				line = in.readLine();
				if (line != null) {
					String text = textArea.getText();
					if (text!=null&&!text.isEmpty()) {
						text+="\n";
					}
					System.out.println(line);
					textArea.setText(text+line);
				}				
				
			}
		} catch (SocketException e) {
			System.out.println("Connection to Chatserver lost");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
