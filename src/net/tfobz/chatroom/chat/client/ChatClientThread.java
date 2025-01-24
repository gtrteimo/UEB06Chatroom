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
				System.out.println(line);
				if (line != null) {
					String text = textArea.getText();
					if (text!=null&&!text.isEmpty()) {
						text+="\n";
					}
					System.out.println(line);
					textArea.setText(text+line);
				}
//				if (textArea.getText() != null && !textArea.getText().isEmpty() && !textArea.getText().equals("null")) {
//					System.out.println(1);
//					final String text = textArea.getText() + "\n";
//					System.out.println("Hilfe1: "+ textArea.getText());
//					EventQueue.invokeLater(() -> {
//						textArea.setText(text + line);
//					});
//				} else if (textArea.getText() != null && !textArea.getText().equals("null")){
//					System.out.println(2);
//					System.out.println("Hilfe2: "+ textArea.getText());
//					EventQueue.invokeLater(() -> {
//						textArea.setText(line);
//					});
//				} else {
//					System.out.println(3);
//					System.out.println("Hilfe3: "+ textArea.getText()==null?"null":textArea.getText());
//					owner.setVisible(false);
//					owner.dispose();
//					System.exit(3);
//				}
				
				
			}
		} catch (SocketException e) {
			System.out.println("Connection to ChatSertry { server.close(); } catch (Exception e1) { ; }ver lost");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
