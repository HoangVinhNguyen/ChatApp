package hcmus.edu.client.app;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import hcmus.edu.client.GUI.ClientGUI;

public class SocketClient implements Runnable {

	private Socket socket;
	private ClientGUI gui;
	private String address;
	private int port;
	private boolean alwayRunning = true;
	private ObjectInputStream socketIN;
	private ObjectOutputStream socketOUT;
	private Map<String, String> map;
	private String showUI;
	private String username = "";

	public SocketClient(ClientGUI gui) {
		this.gui = gui;

		try {
			if (gui.getPort() != -1 && gui.getAddress() != null) {
				this.address = gui.getAddress();
				this.port = gui.getPort();
			} else {
				this.address = "localhost";
				this.port = 8888;
			}
			socket = new Socket(InetAddress.getByName(address), port);
			if (socket.isConnected()) {
				socketOUT = new ObjectOutputStream(socket.getOutputStream());
				socketOUT.flush();
				socketIN = new ObjectInputStream(socket.getInputStream());
				gui.ControlButton(2);
			}
		} catch (IOException e) {
			System.out.println("SocketClient 1");
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		while (alwayRunning) {
			try {
				map = decodeMessage((String) socketIN.readObject());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				System.out.println("Doc null 1");
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Doc null 2");
			}
			String type = map.get("type");
			String user = map.get("user");
			String content = map.get("message");
			String userDes = map.get("userDes");
			switch (type) {

			case "message":
				if (userDes != null) {
					if (userDes.equals("ALLUSER") && !user.equals(username)) {
						showUI = "[" + user + " -> Tất cả" + "]: " + content;
						gui.addInfoTxtArea(showUI);
					}
					if (userDes.equals("ALLUSER") && user.equals(username)) {
						showUI = "[ Tôi" + " -> Tất cả" + "]: " + content;
						gui.addInfoTxtArea(showUI);
					} else if (userDes.equals(username)) {
						showUI = "[" + user + " -> Tôi" + "]: " + content;
						gui.addInfoTxtArea(showUI);
					} else if (!userDes.equals("ALLUSER")) {
						showUI = "[ Tôi" + " -> " + userDes + "]: " + content;
						gui.addInfoTxtArea(showUI);
					}

				}
				break;
			case "register":
				showUI = "[" + user + " -> Tôi" + "]: " + content;
				gui.addInfoTxtArea(showUI);
				if ("Đăng ký thành công!".equals(content)) {
					gui.ControlButton(3);
				}
				break;
			case "login":
				showUI = "[" + user + " -> Tôi" + "]: " + content;
				gui.addInfoTxtArea(showUI);
				if ("Đăng nhập thành công!".equals(content)) {
					username = userDes;
					gui.ControlButton(4);
				}
				break;
			case "logout":
				if (content.equals(username)) {
					showUI = "[" + user + " -> Tôi" + "]: Đăng xuất thành công!";
					gui.addInfoTxtArea(showUI);
					gui.deleteAllUserInList();
					alwayRunning = false;
					gui.ControlButton(5);
					gui.getSocketClientThread().stop();
					gui.CloseWindow();
				} else {
					showUI = "[" + user + " -> Tôi" + "]: " + content + " đã đăng xuất!";
					gui.addInfoTxtArea(showUI);
					gui.deleteUserInList(content);
				}
				break;
			case "listUser":
				if (!content.equals(username))
					gui.addUserToListOnline(content);
				break;
			case "sendFileRequest":
				if (JOptionPane.showConfirmDialog(gui.getTxtArea(),
						("Bạn có chấp nhận tập tin " + content + " từ " + user + " ?")) == 0) {

					JFileChooser jchoose = new JFileChooser();
					jchoose.setSelectedFile(new File(content));
					int returnValueChoose = jchoose.showSaveDialog(gui.getTxtArea());

					String saveToFile = jchoose.getSelectedFile().getPath();
					if (saveToFile != null && returnValueChoose == JFileChooser.APPROVE_OPTION) {
						DownloadFile dwload = new DownloadFile(saveToFile, gui.getTxtArea());
						Thread t = new Thread(dwload);
						t.start();
						String temp = encodeMessage("sendFileRespone", username, String.valueOf(dwload.getPort()),
								user);
						sendMessage(temp);
					} else {
						String temp = encodeMessage("sendFileRespone", username, "NO", user);
						sendMessage(temp);
					}
				} else {
					String temp = encodeMessage("sendFileRespone", username, "NO", user);
					sendMessage(temp);
				}
				break;
			case "sendFileRespone":
				if (content.equals("NO")) {
					gui.addInfoTxtArea("[ MÁY CHỦ -> Tôi] : " + user + " từ chối yêu cầu tập tin này");
				} else {
					int port = Integer.parseInt(content);
					String addr = user;
					UploadFile upload = new UploadFile(addr, port, gui.getFile(), gui.getTxtArea());
					Thread thread = new Thread(upload);
					thread.start();
				}
				break;
			default:
				break;
			}
		}
		
		this.gui.getSocketClientThread().currentThread().interrupt();
		try {
			this.socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendMessage(String mess) {

		try {
			socketOUT.writeObject(mess);
		} catch (IOException e) {
			System.out.println("ERROR SEND MESSAGE CLIENT");
		}
	}

	public String encodeMessage(String type, String user, String content, String userDes) {
		return String.join("`;`", type, user, content, userDes);
	}

	public Map<String, String> decodeMessage(String msg) {
		Map<String, String> map = new HashMap<String, String>();
		String[] temp = msg.split("`;`");
		map.put("type", temp[0] == null ? "" : temp[0]);
		map.put("user", temp[1] == null ? "" : temp[1]);
		map.put("message", temp[2] == null ? "" : temp[2]);
		map.put("userDes", temp[3] == null ? "" : temp[3]);
		return map;
	}
}
