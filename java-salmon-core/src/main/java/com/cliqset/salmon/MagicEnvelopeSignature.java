package com.cliqset.salmon;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name="sig", namespace=MagicEnvelope.NS_MAGIC_ENVELOPE)
@XmlAccessorType(XmlAccessType.FIELD)
public class MagicEnvelopeSignature {

	@XmlValue
	private String value;
	
	@XmlAttribute(name="keyhash")
	private String keyhash;

	public MagicEnvelopeSignature() {}
	
	public MagicEnvelopeSignature(String keyhash, String signature) {
		this.keyhash = keyhash;
		this.value = signature;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setKeyhash(String keyhash) {
		this.keyhash = keyhash;
	}

	public String getKeyhash() {
		return keyhash;
	}
}
