package com.cliqset.salmon;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name="data", namespace="http://salmon-protocol.org/ns/magic-env")
@XmlAccessorType(XmlAccessType.FIELD)
public class MagicEnvelopeData {

	@XmlValue
	private String value;
	
	@XmlAttribute(name="type", namespace="http://salmon-protocol.org/ns/magic-env")
	private String type;

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
