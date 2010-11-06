package com.cliqset.salmon;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="author", namespace="http://www.w3.org/2005/Atom")
@XmlAccessorType(XmlAccessType.FIELD)
public class Author {
	
	@XmlElement(name="uri", namespace="http://www.w3.org/2005/Atom")
	public String URI;
}
