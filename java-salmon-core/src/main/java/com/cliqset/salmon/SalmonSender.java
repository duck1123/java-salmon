package com.cliqset.salmon;

import java.net.URL;

public interface SalmonSender {
	
	SalmonDeliveryResponse send(URL destination, String contentType, byte[] data) throws SalmonException;
}
