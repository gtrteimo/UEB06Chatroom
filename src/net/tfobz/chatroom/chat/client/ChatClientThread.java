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
					String text = "";
					synchronized (textArea) {
					     text = textArea.getText();
					}

				    int bodyStart = text.indexOf("<body>") + 6;
				    int bodyEnd = text.indexOf("</body>");

				    if (bodyStart > 6 && bodyEnd > bodyStart) {
				        String bodyContent = text.substring(bodyStart, bodyEnd).trim();

				        bodyContent = bodyContent.replaceAll("(?i)<p[^>]*>\\s*</p>", "");
				        
				        if (!bodyContent.isEmpty() && !bodyContent.endsWith("<br>")) {
				            bodyContent += "<br>";
				        }

				        bodyContent += line;

				        text = text.substring(0, bodyStart) + bodyContent + text.substring(bodyEnd);
				    }
					synchronized (textArea) {
						textArea.setText(text);
					}
				}	
			}
		} catch (SocketException e) {
			String text = "";
			synchronized (textArea) {
			     text = textArea.getText();
			}

		    int bodyStart = text.indexOf("<body>") + 6;
		    int bodyEnd = text.indexOf("</body>");

		    if (bodyStart > 6 && bodyEnd > bodyStart) {
		        String bodyContent = text.substring(bodyStart, bodyEnd).trim();

		        bodyContent = bodyContent.replaceAll("(?i)<p[^>]*>\\s*</p>", "");
		        
		        if (!bodyContent.isEmpty() && !bodyContent.endsWith("<br>")) {
		            bodyContent += "<br>";
		        }

		        bodyContent += "Connection to Chatserver lost";

		        text = text.substring(0, bodyStart) + bodyContent + text.substring(bodyEnd);
		    }
			synchronized (textArea) {
				textArea.setText(text);
			}
			System.out.println("Connection to Chatserver lost");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
