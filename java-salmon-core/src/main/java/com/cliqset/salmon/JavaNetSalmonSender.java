package com.cliqset.salmon;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class JavaNetSalmonSender implements SalmonSender {

	public void send(URL destination, String contentType, byte[] data) throws SalmonException {
		try {
			HttpURLConnection connection = (HttpURLConnection) destination.openConnection();
	        connection.setDoOutput(true);
	        connection.setRequestMethod("POST");
	        connection.setRequestProperty("Content-Type", contentType);
	
	        OutputStream os = connection.getOutputStream();
	        os.write(data);
	        os.close();
	         int responseCode = connection.getResponseCode();
			if (!(responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_ACCEPTED)) {
				throw new SalmonException("Unsuccessful response code: " + responseCode + " received when delivering salmon.");
			}
		} catch (IOException e) {
			throw new SalmonException("Unable to deliver salmon.", e);
		}
	}
}
