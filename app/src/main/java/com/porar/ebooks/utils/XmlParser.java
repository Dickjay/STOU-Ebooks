package com.porar.ebooks.utils;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.util.Log;

public class XmlParser {

//	public String getXmlFromUrl2(String url_profile) {
//		String xml = null;
//		InputStream is = null;
//
//		
//		try {
//			URL url = new URL(url_profile);
//			StringBuilder sb = new StringBuilder();
//			URLConnection connection = url.openConnection();
//			connection.setConnectTimeout(5000);
//			HttpURLConnection httpConnection = (HttpURLConnection) connection;
//			int responseCode = httpConnection.getResponseCode();
//
//			if (responseCode == HttpURLConnection.HTTP_OK) {
//				InputStream in = httpConnection.getInputStream();
//				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//				
//				String line = null;
//				while ((line = reader.readLine()) != null) {
//					sb.append(line);
//				}
//				in.close();
//				xml = sb.toString();
//			}
//		} catch (Exception e) {
//			
//		}
//		// return XML
//		return xml;
//	}

	public Document getDomElement(String xml) {
		Document doc = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {

			DocumentBuilder db = dbf.newDocumentBuilder();

			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = db.parse(is);

		} catch (ParserConfigurationException e) {
			Log.e("Error: ", e.getMessage());
			return null;
		} catch (SAXException e) {
			Log.e("Error: ", e.getMessage());
			return null;
		} catch (IOException e) {
			Log.e("Error: ", e.getMessage());
			return null;
		}
		// return DOM
		return doc;
	}

	public String getValue(Element item, String str) {
		NodeList n = item.getElementsByTagName(str);
		return this.getElementValue(n.item(0));
	}

	public final String getElementValue(Node elem) {
		Node child;
		if (elem != null) {
			if (elem.hasChildNodes()) {
				for (child = elem.getFirstChild(); child != null; child = child.getNextSibling()) {
					//if (child.getNodeType() == Node.TEXT_NODE) {
						return child.getNodeValue();
					//}
				}
			}
		}
		return "";
	}
}
