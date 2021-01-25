package hcmus.edu.client.GUI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import hcmus.edu.client.app.SocketClient;

public class ClientGUI {

	private SocketClient socketClient;
	private Thread thread;
	private String username;
	private JFrame mainFrame;
	private JTextArea txtArea;
	private JPanel right;
	private JPanel top;
	private JPanel left;
	private JPanel bot;
	private JPanel bot2;
	private JButton btnStart;
	private JButton btnSend;
	private JButton btnSendFile;
	private JButton btnOpenFile;
	private JButton btnLogin;
	private JButton btnLogout;
	private JButton btnRegister;
	private JLabel lbUsername;
	private JLabel lbPasswd;
	private JLabel lbAddress;
	private JLabel lbPort;
	private JTextField txtAddress;
	private JTextField txtPort;
	private JTextField txtUsername;
	private JTextField txtPathFile;
	private JPasswordField txtPasswd;
	private JTextArea txtMessage;
	private DefaultListModel<String> listUserChat;
	private JList<String> list;
	private File file;

	public void start() {
		mainFrame = new JFrame("Client");
		mainFrame.getContentPane().setLayout(new BoxLayout(mainFrame.getContentPane(), BoxLayout.X_AXIS));
		mainFrame.setSize(800, 500);
		mainFrame.setResizable(false);
		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		mainFrame.addWindowListener(new WindowAdapter() {
			@SuppressWarnings({ "deprecation", "static-access" })
			public void windowClosing(WindowEvent event) {
				try {
					String user = txtUsername.getText();
					String pass = String.valueOf(txtPasswd.getPassword());
					String temp = socketClient.encodeMessage("logout", user, pass, "SERVER");
					socketClient.sendMessage(temp);
					thread.currentThread().interrupt();
				} catch (Exception ex) {
					System.exit(0);
				}
				System.exit(0);
			}
		});

		// Hiển thị thông tin.
		right = new JPanel();
		right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
		JPanel horizalList = new JPanel();
		horizalList.setLayout(new BoxLayout(horizalList, BoxLayout.Y_AXIS));
		listUserChat = new DefaultListModel<>();
		listUserChat.addElement("Gửi tất cả");
		list = new JList<>(listUserChat);
//		list.setPreferredSize(new Dimension(50, 100));
//		list.setMaximumSize(list.getPreferredSize());
		JScrollPane scrollList = new JScrollPane();
		scrollList.add(list);
		scrollList.setViewportView(list);
		scrollList.setPreferredSize(new Dimension(200, 200));
		scrollList.setMaximumSize(scrollList.getPreferredSize());
		horizalList.add(scrollList);
		horizalList.setOpaque(true);
		horizalList.setBorder(BorderFactory.createTitledBorder("Chọn gửi cho:"));

		lbAddress = new JLabel();
		lbAddress.setText("Address: ");
		txtAddress = new JTextField();
		txtAddress.setPreferredSize(new Dimension(200, 20));
		txtAddress.setMaximumSize(txtAddress.getPreferredSize());
		txtAddress.setText("localhost");
		lbPort = new JLabel();
		lbPort.setText("Port: ");
		txtPort = new JTextField();
		txtPort.setPreferredSize(new Dimension(200, 20));
		txtPort.setMaximumSize(txtPort.getPreferredSize());
		txtPort.setText("8888");
		btnStart = new JButton();
		btnStart.setText("Kết nối đến máy chủ");
		lbUsername = new JLabel();
		lbUsername.setText("Username: ");
		lbPasswd = new JLabel();
		lbPasswd.setText("Password: ");
		txtUsername = new JTextField();
		txtUsername.setPreferredSize(new Dimension(200, 20));
		txtUsername.setMaximumSize(txtUsername.getPreferredSize());
		txtUsername.setText("vinh");
		txtPasswd = new JPasswordField();
		txtPasswd.setPreferredSize(new Dimension(200, 20));
		txtPasswd.setMaximumSize(txtPasswd.getPreferredSize());
		txtPasswd.setText("123");
		btnLogin = new JButton();
		btnLogin.setText("Đăng nhập");
		btnRegister = new JButton();
		btnRegister.setText("Đăng kí");
		btnLogout = new JButton();
		btnLogout.setText("Đăng xuất");
		btnLogout.setPreferredSize(
				new Dimension(btnLogin.getMaximumSize().width + btnRegister.getMaximumSize().width - 1,
						btnLogin.getMaximumSize().height));
		btnLogout.setMaximumSize(btnLogout.getPreferredSize());
		JPanel horizal = new JPanel();
		horizal.setLayout(new BoxLayout(horizal, BoxLayout.Y_AXIS));
		horizal.add(lbAddress);
		horizal.add(txtAddress);
		horizal.add(lbPort);
		horizal.add(txtPort);
		horizal.add(btnStart);
		horizal.setOpaque(true);
		horizal.setBorder(BorderFactory.createTitledBorder("Kết nối"));
		JPanel horizal1 = new JPanel();
		horizal1.setLayout(new BoxLayout(horizal1, BoxLayout.X_AXIS));
		horizal1.add(btnStart);
		horizal.add(horizal1);
		JPanel horizal2 = new JPanel();
		horizal2.setLayout(new BoxLayout(horizal2, BoxLayout.Y_AXIS));
		horizal2.add(lbUsername);
		horizal2.add(txtUsername);
		horizal2.add(lbPasswd);
		horizal2.add(txtPasswd);
//		horizal2.add(btnLogin);
//		horizal2.add(btnRegister);
		JPanel horizal3 = new JPanel();
		horizal3.setLayout(new BoxLayout(horizal3, BoxLayout.X_AXIS));
		horizal3.add(btnLogin);
		horizal3.add(btnRegister);
		horizal2.add(horizal3);
		JPanel horizal4 = new JPanel();
		horizal4.setLayout(new BoxLayout(horizal4, BoxLayout.X_AXIS));
		horizal4.add(btnLogout);
		horizal2.add(horizal4);
		horizal2.setOpaque(true);
		horizal2.setBorder(BorderFactory.createTitledBorder("Tài khoản"));
		right.add(horizal);
		right.add(horizal2);
		right.add(horizalList);

		top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));
		txtArea = new JTextArea();
		txtArea.setWrapStyleWord(true);
		txtArea.setLineWrap(true);
		txtArea.setFont(new Font(txtArea.getFont().getName(), txtArea.getFont().getStyle(), 20));
		txtArea.setEditable(false);
		JScrollPane srcollTxtArea = new JScrollPane(txtArea);
		srcollTxtArea.setPreferredSize(new Dimension(600, 450));
		srcollTxtArea.setMaximumSize(srcollTxtArea.getPreferredSize());
		top.add(srcollTxtArea);

		// Nút chạy Server.
		bot = new JPanel();
		bot.setLayout(new BoxLayout(bot, BoxLayout.X_AXIS));
		txtMessage = new JTextArea();
		txtMessage.setFont(new Font(txtMessage.getFont().getName(), txtMessage.getFont().getStyle(), 17));
		txtMessage.setWrapStyleWord(true);
		txtMessage.setLineWrap(true);
//		txtMessage.setPreferredSize(new Dimension(400, 20));
//		txtMessage.setMaximumSize(txtMessage.getPreferredSize());
		JScrollPane srcollTxtMessage = new JScrollPane(txtMessage);
		srcollTxtMessage.setPreferredSize(new Dimension(425, 50));
		srcollTxtMessage.setMaximumSize(srcollTxtMessage.getPreferredSize());
		btnSend = new JButton();
		btnSend.setText("Gửi");
		bot2 = new JPanel();
		bot2.setLayout(new BoxLayout(bot2, BoxLayout.X_AXIS));
		txtPathFile = new JTextField();
		txtPathFile.setPreferredSize(new Dimension(300, 20));
		txtPathFile.setMaximumSize(txtPathFile.getPreferredSize());
		btnOpenFile = new JButton();
		btnOpenFile.setText("Chọn tập tin");
		btnSendFile = new JButton();
		btnSendFile.setText("Gửi tập tin");
		bot2.add(txtPathFile);
		bot2.add(btnOpenFile);
		bot2.add(btnSendFile);
		bot.add(srcollTxtMessage);
		bot.add(btnSend);

		left = new JPanel();
		left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
		left.add(top);
		left.add(bot);
		left.add(bot2);

		mainFrame.add(left);
		mainFrame.add(right);
		mainFrame.setVisible(true);

		// Sự kiện nút bấm.
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				ConnectServerPerformed(event);
			}

		});
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				SendMessage();
			}
		});

		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				RegisterPerformed();
			}
		});

		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				LoginPerformed();
			}
		});

		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				LogoutPerformed();
			}
		});

		btnOpenFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				OpenFile(event);
			}
		});

		btnSendFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				SendFile();
			}
		});

		ControlButton(1);
	}

	// Event Button ------------------------------------------------
	private void SendMessage() {
		String message = getMessage();
		String nameUser = list.getSelectedValue();
		if (nameUser != null && message != null) {
			if (nameUser.equals("Gửi tất cả"))
				nameUser = "ALLUSER";
			String user = txtUsername.getText();
			String temp = socketClient.encodeMessage("message", user, message, nameUser);
			socketClient.sendMessage(temp);
		}
	}

	private void ConnectServerPerformed(ActionEvent event) {
		// Lấy IP và Port.

		// Kết nối mặc định.
		socketClient = new SocketClient(ClientGUI.this);
		thread = new Thread(socketClient);
		thread.start();
	}

	private void RegisterPerformed() {
		String user = txtUsername.getText();
		String pass = String.valueOf(txtPasswd.getPassword());
		if (user != null && pass != null) {
			if (!"".equals(user) && !"".equals(pass)) {
				String temp = socketClient.encodeMessage("register", user, pass, "SERVER");
				socketClient.sendMessage(temp);
			}
			else {
				addInfoTxtArea("[ ChatApp -> Tôi]: Vui lòng không bỏ trống username hay password!");
			}
		}
		else {
			addInfoTxtArea("[ ChatApp -> Tôi]: Vui lòng không bỏ trống username hay password!");
		}
	}

	private void LoginPerformed() {
		String user = txtUsername.getText();
		String pass = String.valueOf(txtPasswd.getPassword());
		if (user != null && pass != null) {
			if (!"".equals(user) && !"".equals(pass)) {
				String temp = socketClient.encodeMessage("login", user, pass, "SERVER");
				socketClient.sendMessage(temp);
			}
			else {
				addInfoTxtArea("[ ChatApp -> Tôi]: Vui lòng không bỏ trống username hay password!");
			}
		}
		else {
			addInfoTxtArea("[ ChatApp -> Tôi]: Vui lòng không bỏ trống username hay password!");
		}
	}

	private void LogoutPerformed() {
		String user = txtUsername.getText();
		String pass = String.valueOf(txtPasswd.getPassword());
		if (user != null && pass != null) {
			if (!"".equals(user) && !"".equals(pass)) {
				String temp = socketClient.encodeMessage("logout", user, pass, "SERVER");
				socketClient.sendMessage(temp);
			}
		}
		//this.thread.currentThread().interrupt();
	}

	private void OpenFile(ActionEvent e) {
		JFileChooser fc = new JFileChooser();
		int i = fc.showOpenDialog(btnOpenFile);
		if (i == JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile();
			txtPathFile.setText(file.getPath());
		}

	}

	private void SendFile() {
		long size = file.length();
		String nameUser = list.getSelectedValue();
		if (nameUser != null) {
			if (nameUser.equals("Gửi tất cả"))
				nameUser = "ALLUSER";
			String user = txtUsername.getText();
			if (size < 120 * 1024 * 1024) {
				String temp = socketClient.encodeMessage("sendFileRequest", user, file.getName(), nameUser);
				socketClient.sendMessage(temp);
			} else {
				addInfoTxtArea("[ ChatApp -> Tôi] : Kích thước tập tin quá lớn");
			}
		}

	}

	// --------------------------------------------------------------

	public String getAddress() {
		if (txtAddress.getText() != null) {
			return txtAddress.getText();
		}
		return null;
	}

	public int getPort() {
		if (txtPort.getText() != null) {
			try {
				return Integer.valueOf(txtPort.getText());
			} catch (Exception e) {
				System.out.println("Error cast Port in Server");
				return -1;
			}
		}
		return -1;
	}

	public String getMessage() {
		if (txtMessage.getText() != null) {
			return txtMessage.getText();
		}
		return null;
	}

	public void addInfoTxtArea(String text) {
		txtArea.append(text + '\n');
	}

	public JTextArea getTxtArea() {
		return txtArea;
	}

	public void addUserToListOnline(String username) {
		if (listUserChat != null) {
			if (listUserChat.indexOf(username) == -1)
				listUserChat.addElement(username);
		}
	}

	public String getUsername() {
		return username;
	}

	public File getFile() {
		return this.file;
	}

	public Thread getSocketClientThread() {
		return this.thread;
	}

	public void deleteUserInList(String user) {
		if (listUserChat != null) {
			listUserChat.removeElement(user);
		}
	}
	
	public void deleteAllUserInList() {
		if (listUserChat != null) {
			for (int i = 1; i < listUserChat.getSize(); i++)
				listUserChat.removeElementAt(i);
		}
	}
	
	public void CloseWindow() {
		mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
	}

	public void ControlButton(int step) {
		switch (step) {
		case 1:// Kết nối server.
			txtAddress.setEnabled(true);
			txtPort.setEnabled(true);
			txtMessage.setEnabled(false);
			txtUsername.setEnabled(false);
			txtPathFile.setEnabled(false);
			txtPasswd.setEnabled(false);
			list.setEnabled(false);
			btnStart.setEnabled(true);
			btnLogin.setEnabled(false);
			btnRegister.setEnabled(false);
			btnLogout.setEnabled(false);
			btnSend.setEnabled(false);
			btnSendFile.setEnabled(false);
			btnOpenFile.setEnabled(false);
			break;
		case 2:// Kết nối thành công chuyển sang phần tài khoản.
			txtAddress.setEnabled(false);
			txtPort.setEnabled(false);
			txtMessage.setEnabled(false);
			txtUsername.setEnabled(true);
			txtPathFile.setEnabled(false);
			txtPasswd.setEnabled(true);
			list.setEnabled(false);
			btnStart.setEnabled(false);
			btnLogin.setEnabled(true);
			btnRegister.setEnabled(false);
			btnLogout.setEnabled(false);
			btnSend.setEnabled(false);
			btnSendFile.setEnabled(false);
			btnOpenFile.setEnabled(false);
			break;
		case 3:// Đăng ký thành công.
			txtAddress.setEnabled(false);
			txtPort.setEnabled(false);
			txtMessage.setEnabled(false);
			txtUsername.setEnabled(true);
			txtPathFile.setEnabled(false);
			txtPasswd.setEnabled(true);
			list.setEnabled(false);
			btnStart.setEnabled(false);
			btnLogin.setEnabled(true);
			btnRegister.setEnabled(false);
			btnLogout.setEnabled(false);
			btnSend.setEnabled(false);
			btnSendFile.setEnabled(false);
			btnOpenFile.setEnabled(false);
			break;
		case 4:// Đăng nhập thành công.
			txtAddress.setEnabled(false);
			txtPort.setEnabled(false);
			txtMessage.setEnabled(true);
			txtUsername.setEnabled(false);
			txtPathFile.setEnabled(true);
			txtPasswd.setEnabled(false);
			list.setEnabled(true);
			btnStart.setEnabled(false);
			btnLogin.setEnabled(false);
			btnRegister.setEnabled(false);
			btnLogout.setEnabled(true);
			btnSend.setEnabled(true);
			btnSendFile.setEnabled(true);
			btnOpenFile.setEnabled(true);
			break;
		case 5:// Đăng xuất.
			txtAddress.setEnabled(false);
			txtPort.setEnabled(false);
			txtMessage.setEnabled(false);
			txtUsername.setEnabled(false);
			txtPathFile.setEnabled(false);
			txtPasswd.setEnabled(false);
			list.setEnabled(false);
			btnStart.setEnabled(true);
			btnLogin.setEnabled(false);
			btnRegister.setEnabled(false);
			btnLogout.setEnabled(false);
			btnSend.setEnabled(false);
			btnSendFile.setEnabled(false);
			btnOpenFile.setEnabled(false);
			break;
		default:
			break;
		}
	}
}
