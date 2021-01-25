package hcmus.edu.client.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JTextArea;

public class UploadFile implements Runnable {

	public Socket socket;
	public FileInputStream socketIN;
	public OutputStream socketOUT;
	public File file;
	private JTextArea guiTxtArea;

	public UploadFile(String address, int port, File filePath, JTextArea guiTxtArea) {
		super();
		try {
			this.file = filePath;
			this.guiTxtArea = guiTxtArea;
			socket = new Socket(InetAddress.getByName(address), port);
			socketOUT = socket.getOutputStream();
			socketIN = new FileInputStream(filePath);
		} catch (Exception e) {
			System.out.println("ERROR UPLOADING");
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			byte[] buffer = new byte[1024];
			int count;

			while ((count = socketIN.read(buffer)) >= 0) {
				socketOUT.write(buffer, 0, count);
			}
			socketOUT.flush();

			guiTxtArea.append("[ ChatApp -> Tôi] : Tải lên tập tin thành công\n");

			if (socketIN != null) {
				socketIN.close();
			}
			if (socketOUT != null) {
				socketOUT.close();
			}
			if (socket != null) {
				socket.close();
			}
		} catch (Exception e) {
			System.out.println("ERROR UPLOADING 2");
			e.printStackTrace();
		}
	}

}
