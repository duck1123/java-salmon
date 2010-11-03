package com.cliqset.magicsig;

public class Signature {

	private String value;
	
	private String keyId;
	
	public Signature withValue(String value) {
		this.setValue(value);
		return this;
	}
	
	public Signature withKeyId(String value) {
		this.setKeyId(value);
		return this;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	public String getKeyId() {
		return keyId;
	}
	
}
