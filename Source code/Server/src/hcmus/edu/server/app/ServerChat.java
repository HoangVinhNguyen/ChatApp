package hcmus.edu.server.app;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import hcmus.edu.server.GUI.ServerGUI;

public class ServerChat extends Thread {

	private Socket socket;
	private Thread threadClient;
	private ServerSocketClient serverSocketClient;
	private ServerGUI gui;
	private int id;
	private ObjectInputStream socketIN;
    private ObjectOutputStream socketOUT;
    private String username;
    private boolean isLogin = false;
    
	// public SocketServer socketServer;

	public ServerChat(ServerSocketClient serverSocketClient, Socket socket){  
    	super();
        this.serverSocketClient = serverSocketClient;
        this.socket = socket;
        this.id     = socket.getPort();
        this.gui = serverSocketClient.GUI();
    }

	public void start() {
		
		if (threadClient == null) {
			threadClient = new Thread(this);
			threadClient.start();
		}
	}
	
	public void send(String msg){
        try {
        	socketOUT.writeObject(msg);
            socketOUT.flush();
        } 
        catch (IOException ex) {
            System.out.println("Exception [SocketClient : send(...)]");
        }
    }
	@SuppressWarnings("deprecation")
	public void run() {
		gui.addInfoTxtArea("Server Thread " + serverSocketClient.getIDUsed() + " đang chạy.");
        while (true){  
    	    try{  
                String msg = (String) socketIN.readObject();
                serverSocketClient.handle(id, msg);
            }
            catch(Exception ioe){  
            	System.out.println(id + " ERROR reading: " + ioe.getMessage());
            	try {
					serverSocketClient.remove(id);
				} catch (IOException e) {
					System.out.println("ERROR REMOVE THREAD");
				}
//                stop();
//                close();
                this.currentThread().interrupt();
                break;
            }
        }
	}

	public void open() {
		try {
			socketOUT = new ObjectOutputStream(socket.getOutputStream());
			socketOUT.flush();
			socketIN = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {  
    	if (socket != null)
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
        if (socketIN != null)
			try {
				socketIN.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        if (socketOUT != null)
			try {
				socketOUT.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
    }

	public int getID(){  
	    return id;
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isLogin() {
		return isLogin;
	}

	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}
	
	public Socket getSocket() {
		return socket;
	}
}
