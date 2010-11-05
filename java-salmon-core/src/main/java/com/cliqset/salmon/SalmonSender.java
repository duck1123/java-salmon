package com.cliqset.salmon;

import java.net.URL;

public interface SalmonSender {
	//TODO: return a response of at least the status code
	void send(URL destination, String contentType, byte[] data) throws SalmonException;
}
