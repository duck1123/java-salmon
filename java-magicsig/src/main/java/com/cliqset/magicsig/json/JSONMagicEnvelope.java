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
package com.cliqset.magicsig.json;

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
