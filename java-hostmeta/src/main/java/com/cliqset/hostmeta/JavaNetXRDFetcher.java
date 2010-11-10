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
package com.cliqset.hostmeta;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cliqset.xrd.XRD;
import com.cliqset.xrd.XRDConstants;
import com.cliqset.xrd.XRDException;

public class JavaNetXRDFetcher implements XRDFetcher {

	private static Logger logger = LoggerFactory.getLogger(JavaNetXRDFetcher.class);
	
	public XRD fetchXRD(URL url) throws HostMetaException {
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection)url.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.connect();
			if (null == conn.getContentType() || !conn.getContentType().startsWith(XRDConstants.XRD_MEDIA_TYPE)) {
				logger.warn("URL: " + url.toString() + " returned an unexpected content-type:" + conn.getContentType() + " when fetching XRD.");
			}
			if (conn.getResponseCode() >= 400) {
				throw new HostMetaException("Unsuccessful XRD request, HTTP response code:" + conn.getResponseCode());
			}
			return XRD.fromStream(conn.getInputStream());
		} catch (IOException ioe) {
			throw new HostMetaException("Unable to request XRD.", ioe);
		} catch (XRDException xe) {
			throw new HostMetaException("Unable to parse XRD.", xe);
		} finally {
			try { conn.getInputStream().close(); } catch (Exception e) {}
		}
	}
}
