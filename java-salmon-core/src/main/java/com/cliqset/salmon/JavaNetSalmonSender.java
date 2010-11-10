/*
	Copyright 2010 Cliqset Inc.
	
	Licensed under the Apache License, Version 2.0 (the "License"); 
	you may not use this file except in compliance with the License. 
	You may obtain a copy of the License at 
	
		http://www.apache.org/licenses/LICENSE-2.0 
	
	Unless required by applicable law or agreed to in writing, software 
	distributed under the License is distributed on an "AS IS" BASIS, 
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
	See the License for the specific language governing permissions and 
	limitations under the License.
*/
package com.cliqset.salmon;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class JavaNetSalmonSender implements SalmonSender {

	public SalmonDeliveryResponse send(URL destination, String contentType, byte[] data) throws SalmonException {
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
			return new SalmonDeliveryResponse().withResponseCode(responseCode);
		} catch (IOException e) {
			throw new SalmonException("Unable to deliver salmon.", e);
		}
	}
}
