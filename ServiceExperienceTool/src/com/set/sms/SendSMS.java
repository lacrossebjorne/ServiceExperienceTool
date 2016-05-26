package com.set.sms;

import java.io.*;
import java.net.*;

public class SendSMS {


		private static HttpURLConnection getConnection(String url) throws IOException {
			HttpURLConnection connection ;
			connection = (HttpURLConnection)(new URL(url)).openConnection();
		    connection.setDoOutput(true);
		    connection.setDoInput(true);
		    connection.setRequestMethod("POST");

		    return connection;
		}
		
		public static void sendDocument(HttpURLConnection connection, String document) throws IOException {

		    OutputStreamWriter writer;
		    writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
		    writer.write(document);
		    writer.flush();
		    writer.close();	
		}

		private static String recieveStatus(HttpURLConnection connection) throws IOException {
		
		    InputStream inputStream = connection.getInputStream();
		    StringBuffer output = new StringBuffer();
		
		    int ch;
		    while ((ch = inputStream.read()) != -1) output.append((char)ch);
		    inputStream.close();
		
		    return output.toString();

		}
		
		/**
		 * Dummy method for SendSMS.send();
		 */
		public static void sendDummyMessage(String url, String destNumber, String message) throws SMSException, SMSTeknikSenderException {
			System.out.println("Inside sendDummyMessage");
			System.out.format("Number: %s, Message: %s, Url: %s", destNumber, message, url);
		}
		
		public static void send(String url, String destNumber, String message) throws SMSException, SMSTeknikSenderException {
			String destinationNumber = destNumber;
			String senderMessage = message;
			
		    // Build XML document
		    String smsDocument = buildDocument(destinationNumber,senderMessage);
			

		    String status = null;
		    HttpURLConnection connection = null;

		    try {

		      // Create connection 
		      connection = getConnection(url);

		      // Send XML document
		      sendDocument(connection, smsDocument);

		      // Get status 
		      status = recieveStatus(connection);
		      System.out.println(status);
		      if(status != null) status = status.trim();

		    }
		    catch (IOException e) {

		    }
		    finally {
		      if (connection != null)
		        connection.disconnect();
		    }	
		}
	

		public  static String buildDocument(String num, String message) {				
		//String  nr;
				//String message;
		String strXml;
		// You will find id, user and pass values in the pdf you got with your account at SMS Teknik AB.
		// If this is missing or if you would like to open up a new account please contact us:
		// info@smsteknik.se / www.smsteknik.se

		strXml = "<?xml version='1.0' encoding='UTF-8' ?>";
		strXml = strXml + "<smsteknik>";
		//strXml = strXml + "<id></id>"; // F�retag
		strXml = strXml + "<id>Jiegbefumen</id>";
		//strXml = strXml + "<user></user>"; // Anv�ndarnamn
		strXml = strXml + "<user>smsZY9@8L</user>";
		//strXml = strXml + "<pass></pass>"; // L�senord
		
		strXml = strXml + "<pass>VJ9G8v</pass>";
		// strXml = strXml + "<operationtype>0</operationtype>"; // 0=Text, 1=Wap-push, 2=vCard, 3=vCalender, 4=Binary sms
		// strXml = strXml + "<flash>0</flash>"; // 0=Normal message, 1=Flash message
		//strXml = strXml + "<multisms>0</multisms>"; // 0=avaktiverat (160 char), 1=aktiverat (up to 804 char)
		strXml = strXml + "<multisms>1</multisms>";
		strXml = strXml + "<maxmultisms>1</maxmultisms>"; // 0=avaktiverat, 1-6 SMS count
		// strXml = strXml + "<compresstext>0</compresstext>"; // 0="En liten text", 1="EnLitenText"
		//strXml = strXml + "<senddate>2007-10-10</senddate>"; // M�ste vara i formatet yyyy-mm-dd
		//strXml = strXml + "<sendtime>13:30:00</sendtime>"; // M�ste vara i formatet hh:mm:ss
		//strXml = strXml + "<text>" + "test message" + "</text>"; // Meddelandetexten
		strXml = strXml + "<text>" + message + "</text>";
		//strXml = strXml + "<udh></udh>";		
		//strXml = strXml + "<smssender>" + "smssender" + "</smssender>"; // Avs�ndare senda
		strXml = strXml + "<smssender>" + "smssender" + "</smssender>";
		 strXml = strXml + "<deliverystatustype></deliverystatustype>"; // 0=Off, 1=E-mail, 2=HTTP GET, 3=HTTP POST, 4=XML
		// strXml = strXml + "<deliverystatusaddress></deliverystatusaddress>"; // URL eller E-postadress
		// strXml = strXml + "<usereplynumber></usereplynumber>"; // 0=avaktiverad, 1=aktiverad
		// strXml = strXml + "<usereplyforwardtype></usereplyforwardtype>"; // 0=Off, 1=E-mail, 2=HTTP GET, 3=HTTP POST, 4=XML
		// strXml = strXml + "<usereplyforwardurl></usereplyforwardurl>"; // URL eller E-postadress
		// strXml = strXml + "<usereplycustomid></usereplycustomid>"; // Kundrelaterad ID (max 100 tecken)
		strXml = strXml + "<recipients>";
		//strXml = strXml + "<nr>" + "+46700000000" + "</nr>"; // Mobilnummer
		strXml = strXml + "<nr>" +  num + "</nr>";
		strXml = strXml + "</recipients>";
		strXml = strXml + "</smsteknik>";    
	    
		return strXml; 
	}
	
}  

