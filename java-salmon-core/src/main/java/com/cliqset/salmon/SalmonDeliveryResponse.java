package com.cliqset.salmon;

public class SalmonDeliveryResponse {

	private int responseCode;

	public SalmonDeliveryResponse withResponseCode(int value) {
		this.responseCode = value;
		return this;
	}
	
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public int getResponseCode() {
		return responseCode;
	}
}
