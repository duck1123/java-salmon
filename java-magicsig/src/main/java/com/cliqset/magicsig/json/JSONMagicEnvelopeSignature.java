package com.cliqset.magicsig.json;

public class JSONMagicEnvelopeSignature {

	private String value;
	
	private String key_id;

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setKey_id(String key_id) {
		this.key_id = key_id;
	}

	public String getKey_id() {
		return key_id;
	}
}
