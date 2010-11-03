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

package com.cliqset.magicsig.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name="sig", namespace=XMLMagicEnvelope.NS_MAGIC_ENVELOPE)
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLMagicEnvelopeSignature {

	@XmlValue
	private String value;
	
	@XmlAttribute(name="key_id")
	private String keyId;

	public XMLMagicEnvelopeSignature() {}
	
	public XMLMagicEnvelopeSignature(String keyid, String signature) {
		this.keyId = keyid;
		this.value = signature;
	}
	
	public XMLMagicEnvelopeSignature withKeyId(String value) {
		this.keyId = value;
		return this;
	}
	
	public XMLMagicEnvelopeSignature withValue(String value) {
		this.value = value;
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
	
	public String toString() {
		return "keyId:" + this.keyId + ", sig:" + this.value;
	}
}
