package hcmus.edu.server.GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import hcmus.edu.server.app.ServerSocketClient;

public class ServerGUI {

	private ServerSocketClient svSocketClient;
	private JFrame mainFrame;
	private JTextArea txtArea;
	private JPanel top;
	private JPanel bot;
	private JButton btnStart;
	private JLabel lbAddress;
	private JLabel lbPort;
	private JTextField txtAddress;
	private JTextField txtPort;

	public void start() {
		mainFrame = new JFrame("Server");
		mainFrame.getContentPane().setLayout(new BoxLayout(mainFrame.getContentPane(), BoxLayout.Y_AXIS));
		mainFrame.setSize(1000, 600);
		mainFrame.setResizable(false);
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				System.exit(0);
			}
		});

		// Hiển thị thông tin.
		top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));
		txtArea = new JTextArea();
		txtArea.setWrapStyleWord(true);
		txtArea.setLineWrap(true);
		txtArea.setFont(new Font(txtArea.getFont().getName(), txtArea.getFont().getStyle(), 20));
		txtArea.setBackground(Color.lightGray);
		txtArea.setEditable(false);
		JScrollPane srcollTxtArea = new JScrollPane(txtArea);
		top.add(srcollTxtArea);

		// Nút chạy Server.
		bot = new JPanel();
		bot.setLayout(new BoxLayout(bot, BoxLayout.X_AXIS));
		lbAddress = new JLabel();
		lbAddress.setText("Address: ");
		lbAddress.setEnabled(false);
		txtAddress = new JTextField();
		txtAddress.setPreferredSize(new Dimension(100, 20));
		txtAddress.setMaximumSize(txtAddress.getPreferredSize());
		txtAddress.setEnabled(false);
		lbPort = new JLabel();
		lbPort.setText("Port: ");
		txtPort = new JTextField();
		txtPort.setText("8888");
		txtPort.setPreferredSize(new Dimension(100, 20));
		txtPort.setMaximumSize(txtPort.getPreferredSize());
		btnStart = new JButton();
		btnStart.setText("Khởi động máy chủ");
		bot.add(lbAddress);
		bot.add(txtAddress);
		bot.add(lbPort);
		bot.add(txtPort);
		bot.add(btnStart);

		mainFrame.add(top);
		mainFrame.add(bot);
		mainFrame.setVisible(true);

		// Sự kiện nút bấm.
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StartServerPerformed();
			}
		});
	}

	// Event Button--------------------------------------------
	private void StartServerPerformed() {
		if ((txtPort.getText() != null) && (txtPort.getText() != "") && (txtPort.getText().length() > 0)) {
			try {
				int portTemp = Integer.valueOf(txtPort.getText());
				svSocketClient = new ServerSocketClient(ServerGUI.this, portTemp);
			}catch(Exception e) {
				System.out.println("ERROR CONVERT PORT");
			}
			
		}
		else {
			svSocketClient = new ServerSocketClient(ServerGUI.this);
		}
	}

	// ----------------------------------------------------------

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

	public void addInfoTxtArea(String text) {
		txtArea.append(text + '\n');
	}
}
