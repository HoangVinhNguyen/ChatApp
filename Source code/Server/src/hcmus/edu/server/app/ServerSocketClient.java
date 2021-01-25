package hcmus.edu.server.app;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import hcmus.edu.server.GUI.ServerGUI;
import hcmus.edu.server.data.Data;

public class ServerSocketClient implements Runnable {

	private ServerChat clients[];
	private ServerSocket socket;
	private Thread thread;
	private ServerGUI gui;
	private int port = 8888;
	private int IDUsed = 0;
	private Map<String, String> map;
	private Data data;

	public ServerSocketClient(ServerGUI ui) {
		gui = ui;
		clients = new ServerChat[10];
		try {
			this.port = 8888;

			socket = new ServerSocket(port);
			String info = "Server: PC/IP: " + InetAddress.getLocalHost() + " Port: " + socket.getLocalPort();
			gui.addInfoTxtArea(info);
			data = new Data();
			start();
		} catch (IOException e) {
			gui.addInfoTxtArea("Server Failed");
		}
	}
	
	public ServerSocketClient(ServerGUI ui, int port) {
		gui = ui;
		clients = new ServerChat[10];
		try {
			this.port = port;
			socket = new ServerSocket(port);
			String info = "Server: PC/IP: " + InetAddress.getLocalHost() + " Port: " + socket.getLocalPort();
			gui.addInfoTxtArea(info);
			data = new Data();
			start();
		} catch (IOException e) {
			gui.addInfoTxtArea("Server Failed");
		}
	}

	private void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	@Override
	public void run() {
		while (thread != null) {
			try {
				gui.addInfoTxtArea("Đang chờ kết nối!");
				addThread(socket.accept());
			} catch (Exception ioe) {
				gui.addInfoTxtArea("ERROR ACCEPT SERVER TO CLIENT");
			}
		}
	}

	private void addThread(Socket accept) {
		if (IDUsed < clients.length) {
			gui.addInfoTxtArea("Client accepted: " + socket);
			clients[IDUsed] = new ServerChat(ServerSocketClient.this, accept);
			try {
				clients[IDUsed].open();
				clients[IDUsed].start();
				IDUsed++;
			} catch (Exception e) {
				gui.addInfoTxtArea("Error opening thread: " + e);
			}
		} else {
			gui.addInfoTxtArea("Client refused: maximum " + clients.length + " reached.");
		}
	}

	public synchronized void handle(int id, String msg) {
		map = new HashMap<String, String>();
		map = decodeMessage(msg);
		String type = map.get("type");
		String user = map.get("user");
		String content = map.get("message");
		String userDes = map.get("userDes");
		switch (type) {
		case "message":
			if (userDes != null) {
				if (userDes.equals("ALLUSER")) {
					SendMessageAll(user, content);
				}
				else {
					findUserThread(userDes).send(msg);
					clients[findClient(id)].send(msg);
				}
			}
			break;
		case "register":
			if (findUserThread(user) == null) {
				if (!data.checkUserExist(user)) {
					data.Register(user, content);
					String register = encodeMessage(type, "MÁY CHỦ", "Đăng ký thành công!", user);
					clients[findClient(id)].send(register);
				}
				else {
					String register = encodeMessage(type, "MÁY CHỦ", "Đăng ký không thành công!", user);
					clients[findClient(id)].send(register);
				}
			}
			break;
		case "login":
			if (findUserThread(user) == null) {
				if (data.Login(user, content)) {
					clients[findClient(id)].setUsername(user);
					clients[findClient(id)].setLogin(true);
					clients[findClient(id)].send(encodeMessage("login", "MÁY CHỦ", "Đăng nhập thành công!", user));
					SendAllUserOnlineForAll1(user);
					SendAllUserOnlineForAll2(user);
				}
				else {
					clients[findClient(id)].send(encodeMessage("login", "MÁY CHỦ", "Đăng nhập không thành công!", user));
				}
			}
			else {
				clients[findClient(id)].send(encodeMessage("login", "MÁY CHỦ", "Đăng nhập không thành công!", user));
			}
			break;
		case "logout":
			try {
				AlertUserLeaveChat("logout", "SERVER", user);
				remove(id);
			} catch (IOException e) {
				System.out.println("ERROR LOGOUT");
			}
			break;
		case "sendFileRequest":
			if(userDes.equals("ALLUSER")){
                clients[findClient(id)].send(encodeMessage("message", "SERVER", "Chỉ có thể tải lên cho từng người!", user));
            }
            else{
                findUserThread(userDes).send(msg);
            }
			break;
		case "sendFileRespone":
			if(content.equals("NO")){
				findUserThread(userDes).send(encodeMessage("sendFileRespone", user, content, userDes));
            }
            else{
            	String IP = findUserThread(user).getSocket().getInetAddress().getHostAddress();
                findUserThread(userDes).send(encodeMessage("sendFileRespone", IP, content, userDes));
            }
			break;
		default:
			break;
		}
	}
	
	private void SendMessageAll(String user, String content) {
		for (int i = 0; i < IDUsed; i++) {
			if (clients[i].getUsername() != null) {
				String temp = encodeMessage("message", user, content, "ALLUSER");
				clients[i].send(temp);
			}
		}
	}

	private void SendAllUserOnlineForAll1(String userDes) {
		for (int i = 0; i < IDUsed; i++) {
			if (clients[i].getUsername() != null) {
				String temp = encodeMessage("listUser", "MÁY CHỦ", clients[i].getUsername(), "ALLUSER");
				findUserThread(userDes).send(temp);
			}
		}
	}

	private void SendAllUserOnlineForAll2(String userDes) {
		for (int i = 0; i < IDUsed; i++) {
			if (clients[i].getUsername() != null) {
				String temp = encodeMessage("listUser", "MÁY CHỦ", userDes, "ALLUSER");
				clients[i].send(temp);
			}
		}
	}
	
	public void AlertUserLeaveChat(String type, String sender, String content){
        String msg = encodeMessage(type, sender, content, "ALLUSER");
        for(int i = 0; i < IDUsed; i++){
            clients[i].send(msg);
        }
    }

	private int findClient(int ID) {
		for (int i = 0; i < IDUsed; i++) {
			if (clients[i].getID() == ID) {
				return i;
			}
		}
		return -1;
	}

	public ServerChat findUserThread(String usr) {
		for (int i = 0; i < IDUsed; i++) {
			if (usr.equals(clients[i].getUsername())) {
				return clients[i];
			}
		}
		return null;
	}
	
	@SuppressWarnings("deprecation")
	public synchronized void remove(int ID) throws IOException{  
	    int pos = findClient(ID);
	        if (pos >= 0){  
	            ServerChat clientChat = clients[pos];
	            gui.addInfoTxtArea("Đã xóa client thread " + ID + " tại vị trí " + pos);
		    if (pos < IDUsed - 1){
	                for (int i = pos+1; i < IDUsed; i++){
	                    clients[i-1] = clients[i];
		        }
		    }
		    IDUsed--;
		    clientChat.close();
		    clientChat.getSocket().close();
		}
	    }


	public ServerGUI GUI() {
		return gui;
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

	public int getIDUsed() {
		return IDUsed;
	}
}
