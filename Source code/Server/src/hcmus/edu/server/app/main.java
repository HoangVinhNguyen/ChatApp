package hcmus.edu.server.app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import hcmus.edu.server.GUI.ServerGUI;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ServerGUI serverGUI = new ServerGUI();
		serverGUI.start();
//		try
//		{
//			ServerSocket s = new ServerSocket(8888);
//			
//			do
//			{
//				System.out.println("Waiting for a Client");
//			
//				Socket ss=s.accept(); //synchronous
//				
//			
//				
//				System.out.println("Talking to client");
//				System.out.println(ss.getPort());
//				
//				InputStream is=ss.getInputStream();
//				BufferedReader br=new BufferedReader(new InputStreamReader(is));
//				
//				OutputStream os=ss.getOutputStream();
//				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
//				
//				
//				String receivedMessage;
//				
////				do
////				{
//					receivedMessage=br.readLine();
//					System.out.println("Received : " + receivedMessage);
//					if (receivedMessage.equalsIgnoreCase("quit"))
//					{
//						System.out.println("Client has left !");
//						break;
//					}
////					else
////					{
////						DataInputStream din=new DataInputStream(System.in);
////						String k=din.readLine();
////						bw.write(k);
////						bw.newLine();
////						bw.flush();
////					}
////				}
////				while (true);
//				bw.close();
//				br.close();
//			}
//			while (true);
//			
//		}
//		catch(IOException e)
//		{
//			System.out.println("There're some error");
//		}
	}

}
