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

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.w3c.dom.Element;

@XmlType(name="Link", namespace=XRDConstants.XRD_NAMESPACE)
@XmlAccessorType(XmlAccessType.FIELD)
public class Link {
	
	@XmlAttribute(name="rel")
	private URI rel;
	
	@XmlAttribute(name="type")
	private String type;
	
	@XmlAttribute(name="href")
	private URI href;
	
	@XmlAttribute(name="template")
	private String template;

	@XmlElement(name="Title")
	private List<Title> titles;
	
	@XmlElement(name="Property")
	private List<Property> properties;
	
	@XmlAnyElement
	private List<Element> unknownElements;
	
	@XmlAnyAttribute
	private Map<QName,Object> unknownAttributes;

	private URI processedTemplate;
	
	public void setRel(URI rel) {
		this.rel = rel;
	}

	public URI getRel() {
		return rel;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setHref(URI href) {
		this.href = href;
	}

	public URI getHref() {
		return href;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getTemplate() {
		return template;
	}

	public void setTitles(List<Title> titles) {
		this.titles = titles;
	}

	public List<Title> getTitles() {
		return titles;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setUnknownElements(List<Element> unknownElements) {
		this.unknownElements = unknownElements;
	}

	public List<Element> getUnknownElements() {
		return unknownElements;
	}

	public void setUnknownAttributes(Map<QName,Object> unknownAttributes) {
		this.unknownAttributes = unknownAttributes;
	}

	public Map<QName,Object> getUnknownAttributes() {
		if (null == this.getUnknownAttributes()) {
			this.unknownAttributes = new HashMap<QName, Object>();
		}
		return unknownAttributes;
	}

	public boolean hasTemplate() {
		return null != this.template;
	}
	
	public boolean hasHref() {
		return null != this.href;
	}
	
	public boolean hasUnknownAttributes() {
		return !(null == this.unknownAttributes || this.unknownAttributes.size() < 1);
	}

	public void setProcessedTemplate(URI processedTemplate) {
		this.processedTemplate = processedTemplate;
	}

	public URI getProcessedTemplate() {
		return processedTemplate;
	}
}
