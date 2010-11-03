package com.cliqset.magicsig.json;

import java.util.LinkedList;
import java.util.List;

public class JSONMagicEnvelope {

	private String data;
	
	private String data_type;
	
	private String encoding;
	
	private String alg;
	
	private List<JSONMagicEnvelopeSignature> sigs;

	public void setData(String data) {
		this.data = data;
	}

	public String getData() {
		return data;
	}

	public void setData_type(String data_type) {
		this.data_type = data_type;
	}

	public String getData_type() {
		return data_type;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setAlg(String alg) {
		this.alg = alg;
	}

	public String getAlg() {
		return alg;
	}

	public void setSigs(List<JSONMagicEnvelopeSignature> sigs) {
		this.sigs = sigs;
	}

	public List<JSONMagicEnvelopeSignature> getSigs() {
		return sigs;
	}
}
