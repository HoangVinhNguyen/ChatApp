package hcmus.edu.server.data;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Data {

	private String filePath = "./data/userDB.xml";
	
	public Data() {
		File file = new File(filePath);
		if (!file.exists()) {
			try {
				file.createNewFile();
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	            DocumentBuilder docBuilder;
				try {
					docBuilder = dbFactory.newDocumentBuilder();
					Document doc = docBuilder.newDocument();
					Element root = doc.createElement("users");
					doc.appendChild(root);
					TransformerFactory transformerFactory = TransformerFactory.newInstance();
		            Transformer transformer;
					try {
						transformer = transformerFactory.newTransformer();
						DOMSource source = new DOMSource(doc);
			            StreamResult result = new StreamResult(file);
			            try {
							transformer.transform(source, result);
						} catch (TransformerException e) {
							e.printStackTrace();
						}
					} catch (TransformerConfigurationException e) {
						e.printStackTrace();
					}
		            
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				}
	            
			} catch (IOException e) {
				System.out.println("Error create new file");
			}
		}
	}
	public boolean checkUserExist(String username) {
		try {
			File file = new File(filePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			Element root = doc.getDocumentElement();

			NodeList nListUser = root.getElementsByTagName("user");

			for (int i = 0; i < nListUser.getLength(); i++) {
				Node nNode = nListUser.item(i);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					if (eElement.getElementsByTagName("username").item(0).getTextContent().equals(username)) 
					{
						return true;
					}
				}
			}
			return false;
		} catch (Exception ex) {
			System.out.println("User " + username + "not exist");
			return false;
		}
	}

	
	public void Register(String username, String password) {
		try {
			File file = new File(filePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			Element root = doc.getDocumentElement();

			Element newRecord = doc.createElement("user");
			Element eWord = doc.createElement("username");
			eWord.setTextContent(username);
			Element eMeaning = doc.createElement("password");
			eMeaning.setTextContent(password);
			newRecord.appendChild(eWord);
			newRecord.appendChild(eMeaning);
			root.appendChild(newRecord);

			Transformer tFormer = TransformerFactory.newInstance().newTransformer();
			tFormer.setOutputProperty(OutputKeys.METHOD, "xml");
			Source source = new DOMSource(doc);
			Result result = new StreamResult(file);
			tFormer.transform(source, result);

		} catch (Exception ex) {
			System.out.println("Exceptionmodify xml");
		}
	}
	public boolean Login(String username, String password) {

		if (!checkUserExist(username)) {
			return false;
		}
		else {
			try {
				File file = new File(filePath);
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(file);
				doc.getDocumentElement().normalize();

				NodeList nListUser = doc.getElementsByTagName("user");

				for (int i = 0; i < nListUser.getLength(); i++) {
					Node nNode = nListUser.item(i);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						if (eElement.getElementsByTagName("username").item(0).getTextContent().equals(username)
								&& eElement.getElementsByTagName("password").item(0).getTextContent().equals(password)) 
						{
							return true;
						}
					}
				}
				return false;
			} catch (Exception ex) {
				System.out.println("Error invalid username or password");
				return false;
			}
		}
	}
}
