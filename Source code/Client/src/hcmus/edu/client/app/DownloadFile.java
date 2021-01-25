package hcmus.edu.client.app;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JTextArea;

public class DownloadFile implements Runnable {

	private ServerSocket server;
	private Socket socket;
	private int port;
	private String saveToFile = "";
	private InputStream socketIN;
	private FileOutputStream socketOUT;
	private JTextArea guiTxtArea;

	public DownloadFile(String saveToFile, JTextArea guiTxtArea) {
		try {
			server = new ServerSocket(0);
			port = server.getLocalPort();
			this.saveToFile = saveToFile;
			this.guiTxtArea = guiTxtArea;
		} catch (IOException ex) {
			System.out.println("ERROR DOWNLOADING");
		}
	}

	@Override
	public void run() {
		try {
			socket = server.accept();
			socketIN = socket.getInputStream();
			socketOUT = new FileOutputStream(saveToFile);

			byte[] buffer = new byte[1024];
			int count;

			while ((count = socketIN.read(buffer)) >= 0) {
				socketOUT.write(buffer, 0, count);
			}
			socketOUT.flush();
			guiTxtArea.append("[ ChatApp -> Tôi] : Tải và lưu tập tin thành công\n");
			if (socketOUT != null) {
				socketOUT.close();
			}
			if (socketIN != null) {
				socketIN.close();
			}
			if (socket != null) {
				socket.close();
			}
		} catch (Exception ex) {
			System.out.println("ERROR DOWNLOADING 2");
		}
	}

	public int getPort() {
		return port;
	}

}
