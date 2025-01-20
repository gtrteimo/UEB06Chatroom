package net.tfobz.chatroom.chat.client;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class ChatClient extends JFrame {	
	private final static Color colours[][] = {
        {
            new Color(50, 50, 75),
            new Color(171, 181, 216),
            new Color(225, 225, 225),
            new Color(11, 9, 10),
            new Color(111, 116, 146)
        }
    };
	protected int CLIENT_ID;
	
	public static final int DEFAULT_PORT = 65535;
	public static final String DEFAULT_IP = "localhost";
	public static final String DEFAULT_USERNAME = "User-";
	
	private int port = DEFAULT_PORT;
	private String ip = DEFAULT_IP;
	private String username;
	
	private ChatLogin login;

	Container contentPane;

	JScrollPane scrollPane1;
	JScrollPane scrollPane2;

	JEditorPane textArea;
	JEditorPane textField;
	JButton button;
	
	private Socket client;
	
	private BufferedReader in;
	private PrintStream out;
	
	private Scanner consoleIn;
	
	private ExecutorService executer;
	
	public ChatClient () {
		super();
		
		executer = Executors.newSingleThreadExecutor();
		
		setBounds(25, 25, 1080, 720);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Chatroom");

		contentPane = getContentPane();
		contentPane.setLayout(null);
		contentPane.setBackground(colours[0][1]);

		int w = getWidth() - 6;
		int h = getHeight() - 40;

		scrollPane1 = new JScrollPane();
		scrollPane2 = new JScrollPane();

		textArea = new JEditorPane() {
			@Override
			public boolean getScrollableTracksViewportWidth() {
				return true;
			}
		};
		textField = new JEditorPane() {
			@Override
			public boolean getScrollableTracksViewportWidth() {
				return true;
			}
		};
		button = new JButton();

		textArea.setEditable(false);
		textField.setEditable(true);

		button.setText("Send");

		scrollPane1.setBounds(10, 10, w - 20, h - 20 - 60);
		scrollPane2.setBounds(10, h - 60, w - 200, 50);
		button.setBounds(10 + w - 190, h - 60, 170, 50);

		textArea.setFont(new Font("Arial", Font.PLAIN, scrollPane2.getHeight() / 2 + 1));
		textField.setFont(new Font("Arial", Font.PLAIN, scrollPane2.getHeight() / 2 + 1));
		button.setFont(new Font("Arial", Font.PLAIN, button.getHeight() / 2 + 1));

		textArea.setBackground(colours[0][2]);
		textArea.setForeground(colours[0][3]);
		textField.setBackground(colours[0][2]);
		textField.setForeground(colours[0][3]);
		button.setBackground(colours[0][4]);
		button.setForeground(colours[0][2]);

		scrollPane1.setBorder(null);
		scrollPane2.setBorder(null);
		textArea.setBorder(null);
		textField.setBorder(null);
		button.setBorder(null);

		button.setFocusPainted(false);

		scrollPane1.setViewportView(textArea);
		scrollPane2.setViewportView(textField);

		scrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    
	    scrollPane2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    scrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		
	    button.addActionListener(e -> send());
	    
		contentPane.add(scrollPane1);
		contentPane.add(scrollPane2);
		contentPane.add(button);
		
		login = new ChatLogin(this);
		login.setVisible(true);
		
		try {
			CLIENT_ID = Integer.parseInt(in.readLine());
			if (username == null) {
				username = DEFAULT_USERNAME + CLIENT_ID;
			}
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void connect () throws UnknownHostException, IOException {
		client = null;
			client = new Socket(ip.replaceAll(".", ""), port);
			in = new BufferedReader( new InputStreamReader(client.getInputStream()));
			out = new PrintStream(client.getOutputStream());
			
			out.println(username);
			
			executer.submit(new ChatClientThread(in, textArea));
	}
	
	private void send () {
		try {
			String textNew = textField.getText().trim().replaceAll("\n", "");
			if (!textNew.isEmpty()) {
				String textOld = textArea.getText().trim().replaceAll("\n", "");
				if (!textOld.isEmpty()) {
					textOld += "\n";
				}
//				textArea.setText(textOld + username + ": " + textNew);
				out.println(textNew);
			}
			textField.setText("");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ChatClient c = new ChatClient();
		c.setVisible(true);
	}

	@SuppressWarnings("serial")
	private class ChatLogin extends JDialog {
		private Container contentPane;

		private JLabel labelIP;
		private JLabel labelPort;
		private JLabel labelUsername;

		private JTextField textFieldIP;
		private JTextField textFieldPort;
		private JTextField textFieldUsername;

		private JButton buttonConnect;

		public ChatLogin(ChatClient owner) {
			super(owner, "Log In", true);
			setResizable(false);
			setDefaultCloseOperation(HIDE_ON_CLOSE);
			setBounds(owner.getX(), owner.getY(), 1080, 720);

			contentPane = getContentPane();
			contentPane.setLayout(null);
			contentPane.setBackground(colours[0][1]);

			labelIP = new JLabel();
			labelIP.setHorizontalAlignment(SwingConstants.CENTER);
			labelIP.setFont(new Font("Arial", Font.PLAIN, 40));
			labelPort = new JLabel();
			labelPort.setHorizontalAlignment(SwingConstants.CENTER);
			labelPort.setFont(new Font("Arial", Font.PLAIN, 40));
			labelUsername = new JLabel();
			labelUsername.setHorizontalAlignment(SwingConstants.CENTER);
			labelUsername.setFont(new Font("Arial", Font.PLAIN, 40));

			textFieldIP = new JTextField();
			textFieldPort = new JTextField();
			textFieldUsername = new JTextField();

			buttonConnect = new JButton();
			buttonConnect.setFont(new Font("Arial", Font.PLAIN, 40));
			buttonConnect.setBackground(colours[0][4]);
			buttonConnect.setForeground(colours[0][2]);
			buttonConnect.setFocusPainted(false);
			buttonConnect.setBorderPainted(false);
			buttonConnect.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						ip = textFieldIP.getText().isEmpty()?ip:textFieldIP.getText();
						port = Integer.valueOf(textFieldPort.getText());
						username = textFieldUsername.getText();
						connect();
					} catch (UnknownHostException e1) {
						JOptionPane.showMessageDialog(null, "Wrong Values!", "Warning", JOptionPane.WARNING_MESSAGE);
					} catch (IOException e2) {
						JOptionPane.showMessageDialog(null, "Could't connect to server!", "Warning", JOptionPane.WARNING_MESSAGE);
						e2.printStackTrace();
					}finally {
						try {
							owner.client.close();
						} catch (IOException e1) {
							JOptionPane.showMessageDialog(null, "Critial Issue, software will now close!", "Warning", JOptionPane.WARNING_MESSAGE);
							System.exit(0);
						} catch (NullPointerException e3) {}
					}
				}
			});

			labelIP.setText("Enter IP: ");
			labelPort.setText("Enter Port: ");
			labelUsername.setText("Enter Username: ");

			buttonConnect.setText("Connect");

			labelIP.setBounds(0, 150, 314, 100);
			labelPort.setBounds(10, 300, 314, 100);
			labelUsername.setBounds(10, 450, 314, 100);

			textFieldUsername.setBounds(340, 450, 600, 100);
			textFieldUsername.setFont(new Font("Arial", Font.PLAIN, 40));
			textFieldUsername.setBorder(null);

			textFieldPort.setBounds(340, 300, 600, 100);
			textFieldPort.setFont(new Font("Arial", Font.PLAIN, 40));
			textFieldPort.setBorder(null);
			textFieldPort.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					JTextField c = (JTextField) getFocusOwner();
					if (!(e.getKeyChar() >= '0' && e.getKeyChar() <= '9' && textFieldPort.getText().length() < 5
							|| e.getKeyChar() == KeyEvent.VK_BACK_SPACE))
						c.setEditable(false);
				}

				public void keyReleased(KeyEvent e) {
					JTextField c = (JTextField) getFocusOwner();
					c.setEditable(true);
				}
			});

			textFieldIP.setBounds(340, 150, 600, 100);
			textFieldIP.setFont(new Font("Arial", Font.PLAIN, 40));
			textFieldIP.setBorder(null);
			textFieldIP.setText("localhost");
			textFieldIP.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					JTextField c = (JTextField) getFocusOwner();
					if (!(e.getKeyChar() >= '0' && e.getKeyChar() <= '9' && textFieldIP.getText().length() < 15
							|| e.getKeyChar() == KeyEvent.VK_PERIOD && textFieldIP.getText().length() < 15
							|| e.getKeyChar() == KeyEvent.VK_BACK_SPACE))
						c.setEditable(false);
				}

				public void keyReleased(KeyEvent e) {
					JTextField c = (JTextField) getFocusOwner();
					c.setEditable(true);
				}
			});

			buttonConnect.setBounds(10, 615, 1044, 55);

			contentPane.add(labelIP);
			contentPane.add(labelPort);
			contentPane.add(labelUsername);

			contentPane.add(textFieldIP);
			contentPane.add(textFieldPort);
			contentPane.add(textFieldUsername);

			contentPane.add(buttonConnect);

			JLabel titleLable = new JLabel("Login");
			titleLable.setHorizontalAlignment(SwingConstants.CENTER);
			titleLable.setFont(new Font("Arial", Font.PLAIN, 80));
			titleLable.setBounds(10, 11, 1044, 108);
			getContentPane().add(titleLable);
		}
	}
}
