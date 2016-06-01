package com.set.sms;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.set.dao.RecipientError;

/**
 * A class with static methods to handle: sending SMS-messages, errors and status
 * @author Emil
 *
 */
public class SMSSender {

	private static final Properties PROPERTIES;
	private static final String SEND_URL;
	private static final String CHECK_CREDITS_URL;
	private static final String PROPERTIES_FILE = "smsservice.properties";
	private static final String ID;
	private static final String USER;
	private static final String PASS;
	
	/**
	 * Loading static properties
	 */
	static {
		PROPERTIES = new Properties();

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream propertiesFileStream = classLoader.getResourceAsStream(PROPERTIES_FILE);

		String sendUrl = null;
		String checkCreditsUrl = null;
		String id = null;
		String user = null;
		String pass = null;

		try {
			if (propertiesFileStream == null)
				throw new IOException("No properties file was found");

			PROPERTIES.load(propertiesFileStream);

			checkCreditsUrl = PROPERTIES.getProperty("smsservice.credit_url");
			id = PROPERTIES.getProperty("smsservice.id");
			user = PROPERTIES.getProperty("smsservice.user");
			pass = PROPERTIES.getProperty("smsservice.pass");

			sendUrl = PROPERTIES.getProperty("smsservice.send_url");
			checkCreditsUrl = PROPERTIES.getProperty("smsservice.credit_url");
			checkCreditsUrl = String.format("%s?id=%s&user=%s&pass=%s", checkCreditsUrl, id, user, pass);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			SEND_URL = sendUrl;
			CHECK_CREDITS_URL = checkCreditsUrl;
			ID = id;
			USER = user;
			PASS = pass;
		}

		System.out.format("SEND_URL: %s\nCHECK_CREDITS_URL: %s\nID: %s\nUSER: %s\nPASS: %s\n", SEND_URL,
				CHECK_CREDITS_URL, ID, USER, PASS);
	}

	private SMSSender() {}
	
	/**
	 * Checks how many SMS-credits are left
	 * @return The number of SMS-credits left, or null if not possible to get.
	 */
	public static Integer checkCredits() {
		HttpURLConnection connection = null;
		Integer creditsLeft = null;
		try {
			connection = getConnection(CHECK_CREDITS_URL);
			sendData(connection, "");
			String response = recieveStatus(connection);

			try {
				creditsLeft = Integer.parseInt(response);
			} catch (NumberFormatException | NullPointerException e) {
				creditsLeft = 0;
			}

			System.out.println("response: " + response);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}

		return creditsLeft;
	}

	/**
	 * Dummy method to use instead of 'SendSMS.send()', to avoid credit-loss
	 */
	public static String sendDummyMessage(String destNumber, String message) {
		System.out.println("Inside sendDummyMessage");
		System.out.format("Number: %s, Message: %s, Url: %s\n", destNumber, message, SEND_URL);

		String dummyXmlStatus = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><response><datetime>2016-06-01 02:01:26"
				+ "</datetime><count>1</count><smsleft>5</smsleft><sms><smsid>751945771</smsid></sms></response>";
		;
		return dummyXmlStatus;
	}

	/**
	 * Sends an SMS to one recipient
	 * 
	 * @param destNumber
	 * @param message
	 * @return the status recieved from the connection
	 */
	public static String send(String destNumber, String message) {
		String destinationNumber = destNumber;
		String senderMessage = message;
		String status = null;

		// Build XML document
		String smsDocument = buildDocument(destinationNumber, senderMessage);

		HttpURLConnection connection = null;

		try {
			// Create connection
			connection = getConnection(SEND_URL);
			// Send XML document
			sendData(connection, smsDocument);
			// Get status
			status = recieveStatus(connection);
			System.out.println(status);
			if (status != null)
				status = status.trim();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null)
				connection.disconnect();
		}
		return status;
	}
	
	/**
	 * Creates an instance of the entity-class RecipientError for further processing
	 * @param status the status-message received from calling 'SendSMS.send()'
	 * @param recipient the recipient
	 * @return an instance of the class RecipientError
	 */
	public static RecipientError createRecipientError(String status, String recipient) {
		String errorMessages = extractErrorMessages(status);
		return new RecipientError(recipient, errorMessages);
	}
	
	/**
	 * Reads the unformatted status-message which is expected to be one or more error-messages 
	 * and then returns the formatted error messages as a string.
	 * @param status the unformatted status-message received by calling 'SendSMS.send()'
	 * @return the error message(s)
	 */
	public static String extractErrorMessages(String status) {

		if (status == null) {
			return null;
		}

		StringTokenizer st = new StringTokenizer(status, ";");
		StringBuilder errorMessages = new StringBuilder();

		while (st.hasMoreTokens()) {
			String error = st.nextToken();

			if (error.startsWith("0:")) {
				//////////////////// Known errors ////////////////////
				// Access Denied, Parse Error, Missing Tag, 
				// Empty Message, No SMS left, Invalid phonenumber(e164),
				// Invalid phonenumber, Number is blocked
				String message = status.substring(2);
				if (message.startsWith("Access denied")) {
					System.out.println("No customer of the service or wrong id, username or password.");
					errorMessages.append("No customer of the service or wrong id, username or password. ");
				} else if (message.startsWith("Parse error,")) {
					System.out.print("Parse error or no valid XML-data: \t");
					String reason = message.split(",")[1];
					System.out.println(reason);
					errorMessages.append("Parse error or no valid XML-data: \t" + reason + " ");
				} else if (message.startsWith("The required XML-tag")) {
					System.out.print("The required XML-tag ");
					String tag = message.split("<")[1].split(">")[0];
					System.out.println("<" + tag + "> is missing.");
					errorMessages.append("<" + tag + "> is missing. ");
				} else if (message.startsWith("Message could't be empty")) {
					System.out.println("Message couldn't be empty. ");
					errorMessages.append("Message couldn't be empty. ");
				} else if (message.startsWith("No SMS Left")) {
					System.out.println(
							"Messages are NOT queued for delivery, possible reason is out of SMS for the account.");
					errorMessages.append(
							"Messages are NOT queued for delivery, possible reason is out of SMS for the account. ");
				} else if (message.startsWith("Invalid phonenumber (e164)")) {
					System.out.println("The number is not correct (eg. <8 or >16 numbers incl. leading +)");
					errorMessages.append("Invalid phonenumber (e164) ");
				} else if (message.startsWith("Invalid phonenumber")) {
					System.out.println("The number range is not defined in E164.");
					errorMessages.append("The number range is not defined in E164. ");
				} else if (message.startsWith("Number is blocked")) {
					System.out.println("The number is blocked by SMS Teknik AB");
					errorMessages.append("The number is blocked by SMS Teknik AB. ");
				}
			}

		}
		return errorMessages.toString();
	}
	
	/**
	 * Extracts smsid from a statusMessage received by calling 'SendSMS.send()'

	 * @param xmlStatus The status-message written in following format: <br>
	 * <cite>"&lt;?xml version=\"1.0\" encoding=\"UTF-8\"?&gt;&lt;response&gt;&lt;datetime&gt;2016-06-01 02:01:26&lt;
	 * /datetime&gt;&lt;count&gt;1&lt;/count&gt;&lt;smsleft&gt;5&lt;/smsleft&gt;&lt;sms&gt;&lt;smsid&gt;
	 * <br>751945771&lt;/smsid&gt;&lt;/sms&gt;&lt;/response&gt;</cite>";
	 * @return the smsid as a String
	 */
	public static String extractSMSIDFromStatus(String xmlStatus) {
		String smsid = null;
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(new InputSource(new StringReader(xmlStatus)));			
			Element rootElement = document.getDocumentElement();
			smsid = getTagValue("smsid", rootElement);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
		System.out.println("smsid: " + smsid);
		return smsid;
	}
	
	/**
	 * Gets a HTML-tag value from an Element
	 * @param tagName
	 * @param element
	 * @return the value of the tag
	 */
	private static String getTagValue(String tagName, Element element) {
		NodeList nodeList = element.getElementsByTagName(tagName);
		if (nodeList != null && nodeList.getLength() > 0) {
			NodeList subList = nodeList.item(0).getChildNodes();

			if (subList != null && subList.getLength() > 0) {
				return subList.item(0).getNodeValue();
			}
		}
		return null;
	}

	private static HttpURLConnection getConnection(String url) throws IOException {
		HttpURLConnection connection;
		connection = (HttpURLConnection) (new URL(url)).openConnection();
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestMethod("POST");

		return connection;
	}

	private static void sendData(HttpURLConnection connection, String document) throws IOException {
		OutputStreamWriter writer;
		writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
		writer.write(document);
		writer.flush();
		writer.close();
	}
	
	/**
	 * Receives status, expected to be in XML-format error-format (e.g. '0:Access denied', '0:Parse error, [cause]', etc.)
	 * @param connection
	 * @return the status message
	 * @throws IOException
	 */
	private static String recieveStatus(HttpURLConnection connection) throws IOException {
		InputStream inputStream = connection.getInputStream();
		StringBuffer output = new StringBuffer();

		int ch;
		while ((ch = inputStream.read()) != -1)
			output.append((char) ch);
		inputStream.close();

		return output.toString();
	}

	private static String buildDocument(String num, String message) {
		String strXml;
		// You will find id, user and pass values in the pdf you got with your
		// account at SMS Teknik AB.
		// If this is missing or if you would like to open up a new account
		// please contact us:
		// info@smsteknik.se / www.smsteknik.se

		strXml = "<?xml version='1.0' encoding='UTF-8' ?>";
		strXml = strXml + "<smsteknik>";
		// strXml = strXml + "<id></id>"; // F�retag
		strXml = strXml + "<id>" + ID + "</id>";
		// strXml = strXml + "<user></user>"; // Anv�ndarnamn
		strXml = strXml + "<user>" + USER + "</user>";
		// strXml = strXml + "<pass></pass>"; // L�senord
		strXml = strXml + "<pass>" + PASS + "</pass>";
		// strXml = strXml + "<operationtype>0</operationtype>"; // 0=Text,
		// 1=Wap-push, 2=vCard, 3=vCalender, 4=Binary sms
		// strXml = strXml + "<flash>0</flash>"; // 0=Normal message, 1=Flash
		// message
		// strXml = strXml + "<multisms>0</multisms>"; // 0=avaktiverat (160
		// char), 1=aktiverat (up to 804 char)
		strXml = strXml + "<multisms>1</multisms>";
		strXml = strXml + "<maxmultisms>1</maxmultisms>"; // 0=avaktiverat, 1-6
		// strXml = strXml + "<compresstext>0</compresstext>"; // 0="En liten
		// text", 1="EnLitenText"
		// strXml = strXml + "<senddate>2007-10-10</senddate>"; // M�ste vara i
		// formatet yyyy-mm-dd
		// strXml = strXml + "<sendtime>13:30:00</sendtime>"; // M�ste vara i
		// formatet hh:mm:ss
		// strXml = strXml + "<text>" + "test message" + "</text>"; //
		// Meddelandetexten
		strXml = strXml + "<text>" + message + "</text>";
		// strXml = strXml + "<udh></udh>";
		// strXml = strXml + "<smssender>" + "smssender" + "</smssender>"; //
		// Avs�ndare senda
		strXml = strXml + "<smssender>" + "smssender" + "</smssender>";
		strXml = strXml + "<deliverystatustype></deliverystatustype>";
		// 0=Off, 1=E-mail, 2=HTTP GET, 3=HTTP POST, 4=XML
		// strXml = strXml + "<deliverystatusaddress></deliverystatusaddress>";
		// // URL eller E-postadress
		// strXml = strXml + "<usereplynumber></usereplynumber>"; //
		// 0=avaktiverad, 1=aktiverad
		// strXml = strXml + "<usereplyforwardtype></usereplyforwardtype>"; //
		// 0=Off, 1=E-mail, 2=HTTP GET, 3=HTTP POST, 4=XML
		// strXml = strXml + "<usereplyforwardurl></usereplyforwardurl>"; // URL
		// eller E-postadress
		// strXml = strXml + "<usereplycustomid></usereplycustomid>"; //
		// Kundrelaterad ID (max 100 tecken)
		strXml = strXml + "<recipients>";
		// strXml = strXml + "<nr>" + "+46700000000" + "</nr>"; // Mobilnummer
		strXml = strXml + "<nr>" + num + "</nr>";
		strXml = strXml + "</recipients>";
		strXml = strXml + "</smsteknik>";

		return strXml;
	}
}
