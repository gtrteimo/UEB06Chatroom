package net.tfobz.chatroom.chat.client;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
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

import com.sun.media.jfxmedia.events.NewFrameEvent;

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
	
	public static final int DEFAULT_PORT = 10000;
	public static final String DEFAULT_IP = "localhost";
	public static final String DEFAULT_USERNAME = "User";

	private int port = DEFAULT_PORT;
	private String ip = DEFAULT_IP;
	private String username = "";

	private ChatLogin login;

	private Container contentPane;

	private JScrollPane scrollPane1;
	private JScrollPane scrollPane2;

	private JButton newChatroom;
	private JButton logIn;
	private JButton exit;
	
	private JEditorPane textArea;
	private JEditorPane textField;
	private JButton button;

	private Socket client;

	private BufferedReader in;
	private PrintStream out;
	
	private ExecutorService executer;
	
	public ChatClient () {
		super();
				
		executer = Executors.newSingleThreadExecutor();
		
		setBounds(25, 25, 1080, 720);
		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setTitle("Main Chatroom");

		addWindowListener(new WindowListener() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					logOut();
					client.close();
				} catch (NullPointerException | IOException e1) {
					try {client.close();} catch (Exception e2) {}
				}
				setVisible(false);
				dispose();
				System.exit(2);
			}
			
			@Override
			public void windowOpened(WindowEvent e) {}
			
			@Override
			public void windowIconified(WindowEvent e) {}
			
			@Override
			public void windowDeiconified(WindowEvent e) {}
			
			@Override
			public void windowDeactivated(WindowEvent e) {}
			
			
			@Override
			public void windowClosed(WindowEvent e) {}
			
			@Override
			public void windowActivated(WindowEvent e) {}
		});
		
		contentPane = getContentPane();
		contentPane.setLayout(null);
		contentPane.setBackground(colours[0][1]);

		int w = getWidth() - 6;
		int h = getHeight() - 40;

		scrollPane1 = new JScrollPane();
		scrollPane2 = new JScrollPane();

		newChatroom = new JButton();
		logIn = new JButton();
		exit = new JButton();
		
		textArea = new JEditorPane() {
			@Override
			public boolean getScrollableTracksViewportWidth() {
				return true;
			}
		};
		textArea.setText("text/html");
		textField = new JEditorPane() {
			@Override
			public boolean getScrollableTracksViewportWidth() {
				return true;
			}
		};
		button = new JButton();

		newChatroom.setText("New Chatroom");;
		logIn = new JButton("Log in");
		exit = new JButton("Exit");
		
		textArea.setEditable(false);
		textField.setEditable(true);

		button.setText("Send");
		
		scrollPane1.setBounds(10, 10 + 60, w - 20, h - 20 - 60 - 60);
		scrollPane2.setBounds(10, h - 60, w - 200, 50);
		newChatroom.setBounds(10, 10, (w-20-20)/3, 50);
		logIn.setBounds(1*(w-20)/3 + 15 + 10, 10, (w-20)/3 - 20, 50);
		exit.setBounds( 2*(w-20)/3 + 10 + 20, 10, (w-20)/3 - 20, 50);
		button.setBounds(10 + w - 190, h - 60, 170, 50);

		textArea.setFont(new Font("Arial", Font.PLAIN, scrollPane2.getHeight() / 2 + 1));
		textField.setFont(new Font("Arial", Font.PLAIN, scrollPane2.getHeight() / 2 + 1));
		newChatroom.setFont(new Font("Arial", Font.PLAIN, newChatroom.getHeight() / 2 + 1));
		logIn.setFont(new Font("Arial", Font.PLAIN, logIn.getHeight() / 2 + 1));
		exit.setFont(new Font("Arial", Font.PLAIN, exit.getHeight() / 2 + 1));
		button.setFont(new Font("Arial", Font.PLAIN, button.getHeight() / 2 + 1));

		textArea.setBackground(colours[0][2]);
		textArea.setForeground(colours[0][3]);
		textField.setBackground(colours[0][2]);
		textField.setForeground(colours[0][3]);
		newChatroom.setBackground(colours[0][4]);
		newChatroom.setForeground(colours[0][2]);
		logIn.setBackground(colours[0][4]);
		logIn.setForeground(colours[0][2]);
		exit.setBackground(colours[0][4]);
		exit.setForeground(colours[0][2]);
		button.setBackground(colours[0][4]);
		button.setForeground(colours[0][2]);

		scrollPane1.setBorder(null);
		scrollPane2.setBorder(null);
		textArea.setBorder(null);
		textField.setBorder(null);
		newChatroom.setBorder(null);
		logIn.setBorder(null);
		exit.setBorder(null);
		button.setBorder(null);

		newChatroom.setFocusPainted(false);
		logIn.setFocusPainted(false);
		exit.setFocusPainted(false);
		button.setFocusPainted(false);

		scrollPane1.setViewportView(textArea);
		scrollPane2.setViewportView(textField);

		scrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		scrollPane2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					send();
				}
			}
		});
		
		newChatroom.addActionListener(e -> {
			NewChatroomDialog n = new NewChatroomDialog(this);
			n.setVisible(true);
		});
		
		logIn.addActionListener(e -> {
			try {
				logOut();
				client.close();
			} catch (NullPointerException | IOException e1) {
				try {client.close();} catch (Exception e2) {}
			}
			login.setVisible(true);
		});
		
		exit.addActionListener(e -> {
			try {
				logOut();
				client.close();
			} catch (NullPointerException | IOException e1) {
				try {client.close();} catch (Exception e2) {}
			}
			setVisible(false);
			dispose();
			System.exit(1);
		});
		
		button.addActionListener(e -> send());

		contentPane.add(scrollPane1);
		contentPane.add(scrollPane2);
		contentPane.add(newChatroom);
		contentPane.add(logIn);
		contentPane.add(exit);
		contentPane.add(button);
		
		login = new ChatLogin(this);
		login.setVisible(true);
	}

	public void connect() throws UnknownHostException, ConnectException, IOException {
//		System.out.println("IP: "+ip+", Port: "+port);
	    client = new Socket(ip, port);
	    
	    in = new BufferedReader(new InputStreamReader(client.getInputStream()));
	    out = new PrintStream(client.getOutputStream());

	    out.println(username);
	    
	    try {
	        CLIENT_ID = Integer.parseInt(in.readLine()); 
	    } catch (NumberFormatException e1) {
	        e1.printStackTrace();
	    } catch (IOException e1) {
	        e1.printStackTrace();
	    }
	    	    	    
	    executer.submit(new ChatClientThread(this, in, textArea));
	}

	private void send() {
		try {
			String textNew = textField.getText().trim().replaceAll("\n", "");
			if (!textNew.isEmpty()) {
				String textOld = textArea.getText().trim().replaceAll("\n", "");
				if (!textOld.isEmpty()) {
					textOld += "\n";
				}
				out.println(textNew);
			}
			textField.setText("");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void logOut () throws NullPointerException, IOException {
		out.println("/logout");
		in.readLine();
	}
	
	public static void main(String[] args) {
		ChatClient c = new ChatClient();
		c.setVisible(true);
	}

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
						String temp = textFieldIP.getText();
						if (!temp.trim().replaceAll("\n", "").replaceAll(".", "").isEmpty()) {						
							temp = temp.trim().replaceAll("\n", "");
						} 
						ip = temp;
						if (textFieldPort.getText().trim().replaceAll("\n", "").isEmpty()) {
							port = DEFAULT_PORT;
						} else {
							port = Integer.valueOf(textFieldPort.getText());
						}
						username = textFieldUsername.getText();
//						System.out.println("IP: "+ip+", Port: "+port+", user: "+username);
						connect();
						setVisible(false);
					} catch (ConnectException ex) {
						CustomWarningDialog.showWarning("Values where wrong! No Server found at ip \""+ip+"\" and port \""+port+"\"");
						//TODO funktioniert net
//						JOptionPane.showMessageDialog(ChatLogin.this, "Values where wrong! No Server found at ip \""+ip+"\" and port \""+port+"\"", "Error", JOptionPane.ERROR_MESSAGE);
					} catch (IOException ex) {
						CustomWarningDialog.showWarning("Couldn't connect to Server!");
						ex.printStackTrace();
					}
				}
			});

			labelIP.setText("Enter IP: ");
			labelPort.setText("Enter Port: ");
			labelUsername.setText("Enter Username: ");

			textFieldIP.setText(DEFAULT_IP);
			textFieldPort.setText(DEFAULT_PORT+"");
			textFieldUsername.setText(DEFAULT_USERNAME);
			
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
			textFieldPort.addFocusListener(new FocusListener() {
				@Override
				public void focusLost(FocusEvent e) {
					String text = textFieldPort.getText();
					text = text.replaceAll("[^0-9]", "");
					try {
						int por = Integer.parseInt(text);
						if (por < 1025 || por > 65535) {
							text = DEFAULT_PORT+"";
						}
					} catch (NumberFormatException ex) {
						text = DEFAULT_PORT+"";
					}
	                textFieldPort.setText(text);
				}
				
				@Override
				public void focusGained(FocusEvent e) {
					textFieldPort.selectAll();
				}
			});

			textFieldIP.setBounds(340, 150, 600, 100);
			textFieldIP.setFont(new Font("Arial", Font.PLAIN, 40));
			textFieldIP.setBorder(null);
			textFieldIP.addFocusListener(new FocusListener() {
				@Override
				public void focusLost(FocusEvent e) {
					String text = textFieldIP.getText();
					if (!text.matches("^(\\d{1,3}\\.){3}\\d{1,3}$")) {
						text = DEFAULT_IP;
				    }

				    String[] parts = text.split("\\.");
				    if (parts.length != 4) {
						text = DEFAULT_IP;
				    }

				    for (String part : parts) {
				        try {
				            int num = Integer.parseInt(part);
				            if (num < 0 || num > 255) {
								text = DEFAULT_IP;
								break;
				            }
				        } catch (NumberFormatException ex) {
							text = DEFAULT_IP;
							break;
				        }
			        }
					if (!text.equals("localhost")) {
						text = text.replaceAll("[^0-9.]", "");
				    }
							        
	                textFieldIP.setText(text);
				}
				
				@Override
				public void focusGained(FocusEvent e) {
					textFieldPort.selectAll();
				}
			});


			buttonConnect.setBounds(10, 615, getWidth()-20-6, 55);

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
	private class NewChatroomDialog extends JDialog {
		private Container contentPane;

		private JLabel nameLabel;
		private JLabel portLabel;
		private JLabel passwordLabel;
		
		private JTextField nameTextfield;
		private JTextField portTextfield;
		private JTextField passwordTextfield;
		
		private JButton create;
		private JButton close;

		private String name = null;
		private String password = null;

		
		public NewChatroomDialog(ChatClient owner) {
			super(owner, "New Chatroom", true);
			setResizable(false);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setBounds(owner.getX(), owner.getY(), 1080, 720);
			
			contentPane = getContentPane();
			contentPane.setLayout(null);
			contentPane.setBackground(colours[0][1]);

			nameLabel = new JLabel();
			nameLabel.setForeground(colours[0][3]);
			
			portLabel = new JLabel();
			portLabel.setForeground(colours[0][3]);
			
			passwordLabel = new JLabel();
			passwordLabel.setForeground(colours[0][3]);
			
			nameTextfield = new JTextField();
			nameTextfield.setBackground(colours[0][2]);
			nameTextfield.setForeground(colours[0][3]);
			nameTextfield.setBorder(null);
			
			portTextfield = new JTextField();
			portTextfield.setBackground(colours[0][2]);
			portTextfield.setForeground(colours[0][3]);
			portTextfield.setBorder(null);
			
			passwordTextfield = new JTextField();
			passwordTextfield.setBackground(colours[0][2]);
			passwordTextfield.setForeground(colours[0][3]);
			passwordTextfield.setBorder(null);			
			
			create = new JButton();
			create.setBackground(colours[0][4]);
			create.setForeground(colours[0][2]);
			create.setFocusPainted(false);
			create.setBorderPainted(false);
			
			close = new JButton();
			close.setBackground(colours[0][4]);
			close.setForeground(colours[0][2]);
			close.setFocusPainted(false);
			close.setBorderPainted(false);

			nameLabel.setText("Enter name of the new Server: ");
			portLabel.setText("Enter port of the new Server: ");
			passwordLabel.setText("Enter password of the new Server: ");

			nameTextfield.setText("");
			portTextfield.setText("");
			passwordTextfield.setText("");
			
			create.setText("Create");
			close.setText("Close");

			nameLabel.setBounds(10, 10, 2*getWidth()/5-6-20, 50);
			nameLabel.setFont(new Font("Arial", Font.PLAIN, nameLabel.getHeight()/2+1));
			
			portLabel.setBounds(10, 70, 2*getWidth()/5-6-20, 50);
			portLabel.setFont(new Font("Arial", Font.PLAIN, portLabel.getHeight()/2+1));
			
			passwordLabel.setBounds(10, 130, 2*getWidth()/5-6-20, 50);
			passwordLabel.setFont(new Font("Arial", Font.PLAIN, portLabel.getHeight()/2+1));
			
			nameTextfield.setBounds(10 + 2*getWidth()/5-6-10, 10, 3*getWidth()/5-6-20, 50);
			nameTextfield.setFont(new Font("Arial", Font.PLAIN, nameTextfield.getHeight()/2+1));
			nameTextfield.setBorder(null);
			
			portTextfield.setBounds(10 + 2*getWidth()/5-6-10, 70, 3*getWidth()/5-6-20, 50);
			portTextfield.setFont(new Font("Arial", Font.PLAIN, portTextfield.getHeight()/2+1));
			portTextfield.setBorder(null);
			
			passwordTextfield.setBounds(10 + 2*getWidth()/5-6-10, 130, 3*getWidth()/5-6-20, 50);
			passwordTextfield.setFont(new Font("Arial", Font.PLAIN, portTextfield.getHeight()/2+1));
			passwordTextfield.setBorder(null);
			

			create.setBounds(10, 615, ((getWidth()-6)/2)-10, 55);
			close.setBounds(((getWidth()-6)/2)+10, 615, ((getWidth()-6)/2)-10, 55);
			create.setFont(new Font("Arial", Font.PLAIN, create.getHeight()/2+1));
			close.setFont(new Font("Arial", Font.PLAIN, close.getHeight()/2+1));

			create.addActionListener(e -> create());
			
			close.addActionListener(e -> {
				setVisible(false);
				dispose();
			});
			
			contentPane.add(nameLabel);
			contentPane.add(portLabel);
			contentPane.add(passwordLabel);

			contentPane.add(nameTextfield);
			contentPane.add(portTextfield);
			contentPane.add(passwordTextfield);

			contentPane.add(create);
			contentPane.add(close);
		}
		private void create () {			
			name = nameTextfield.getText().trim().replaceAll("\n", "");
			if (portTextfield.getText().trim().replaceAll("\n", "").isEmpty()) {
				port = DEFAULT_PORT;
			} else {
				port = Integer.valueOf(portTextfield.getText());
			}
			password = passwordTextfield.getText().trim().replaceAll("\n", "");
			
			try {
				out.println("/Chatroom("+name+", "+password+", "+port+")");
			} catch (Exception e1) {
				try {client.close();} catch (Exception e2) {}
			}
			
			try {
				logOut();
				client.close();
			} catch (NullPointerException | IOException e1) {
				try {client.close();} catch (Exception e2) {}
			}
			
			try {
				connect();
			} catch (NullPointerException | IOException e1) {
				try {client.close();} catch (Exception e2) {}
			}
			
			setVisible(false);
			dispose();
		}
	}
}
