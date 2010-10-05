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

package com.cliqset.xrd;

import javax.xml.namespace.QName;
import javax.xml.bind.annotation.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.w3c.dom.Element;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@XmlRootElement(name="XRD", namespace=XRDConstants.XRD_NAMESPACE)
@XmlAccessorType(XmlAccessType.FIELD)
public class XRD {

	@XmlAttribute(name="id", namespace=XRDConstants.XML_NAMESPACE)
	private String id;
	
	@XmlAnyAttribute
	private Map<QName, Object> unknownAttributes;
	
	@XmlElement(name="Expires", namespace=XRDConstants.XRD_NAMESPACE)
	private Expires expires;
	
	@XmlElement(name="Subject", namespace=XRDConstants.XRD_NAMESPACE)
	private Subject subject;
	
	@XmlElement(name="Alias", namespace=XRDConstants.XRD_NAMESPACE)
	private List<Alias> aliases;
	
	@XmlElement(name="Property", namespace=XRDConstants.XRD_NAMESPACE)
	private List<Property> properties;
	
	@XmlElement(name="Link", namespace=XRDConstants.XRD_NAMESPACE)
	private List<Link> links;
	
	@XmlElement(name="Signature", namespace=XRDConstants.XML_SIG_NAMESPACE)
	private List<Signature> signatures;

	@XmlAnyElement
	private List<Element> unknownElements;
	
	public void setExpires(Expires expires) {
		this.expires = expires;
	}

	public Expires getExpires() {
		return expires;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setAliases(List<Alias> aliases) {
		this.aliases = aliases;
	}

	public List<Alias> getAliases() {
		return aliases;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setSignatures(List<Signature> signatures) {
		this.signatures = signatures;
	}

	public List<Signature> getSignatures() {
		return signatures;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setUnknownAttributes(Map<QName, Object> unknownAttributes) {
		this.unknownAttributes = unknownAttributes;
	}

	public Map<QName, Object> getUnknownAttributes() {
		return unknownAttributes;
	}

	public void setUnknownElements(List<Element> unknownElements) {
		this.unknownElements = unknownElements;
	}

	public List<Element> getUnknownElements() {
		return unknownElements;
	}
	
	public boolean hasId() {
		return null != this.id;
	}
	
	public boolean hasExpires() {
		return null != this.expires;
	}
	
	public boolean hasSubject() {
		return null != this.subject;
	}
	
	public boolean hasAliases() {
		return null != this.aliases;
	}
	
	public boolean hasProperties() {
		return null != this.properties;
	}
	
	public boolean hasLinks() {
		return null != this.links;
	}
	
	public static XRD fromStream(InputStream stream) throws XRDException {
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(XRD.class);
			return (XRD)context.createUnmarshaller().unmarshal(stream);
		} catch (JAXBException e) {
			throw new XRDException("Unable to deserialize stream into XRD", e);
		}
	}
}
