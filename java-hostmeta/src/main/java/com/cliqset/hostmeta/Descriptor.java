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

package com.cliqset.hostmeta;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.w3c.dom.Element;

import com.cliqset.xrd.Alias;
import com.cliqset.xrd.Expires;
import com.cliqset.xrd.Link;
import com.cliqset.xrd.Property;
import com.cliqset.xrd.Subject;
import com.cliqset.xrd.Title;

public class Descriptor {

	private String id;
	
	private Map<QName, Object> unknownAttributes;
	
	private Expires expires;

	private Subject subject;

	private List<Alias> aliases;

	private List<Property> properties;
	
	private List<Link> links;

	private Title title;
	
	private List<Element> unknownElements;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public boolean hasUnknownAttributes() {
		return !(null == this.unknownAttributes || this.unknownAttributes.size() < 1);
	}
	
	public void addUnknownAttribute(QName qname, Object obj) {
		this.getUnknownAttributes().put(qname, obj);
	}

	public Map<QName, Object> getUnknownAttributes() {
		if (null == this.unknownAttributes) {
			this.unknownAttributes = new HashMap<QName, Object>();
		}
		return unknownAttributes;
	}

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

	public boolean hasAliases() {
		return !(null == this.properties || this.properties.size() < 1);
	}
	
	public void addAliases(List<Alias> aliases) {
		this.aliases = aliases;
	}

	public List<Alias> getAliases() {
		if (null == this.aliases) {
			this.aliases = new LinkedList<Alias>();
		}
		return aliases;
	}

	public boolean hasProperties() {
		return !(null == this.properties || this.properties.size() < 1); 
	}
	
	public void addProperty(Property property) {
		this.getProperties().add(property);
	}

	public List<Property> getProperties() {
		if (null == this.properties) {
			this.properties = new LinkedList<Property>();
		}
		return properties;
	}

	public boolean hasLinks() {
		return !(null == this.links || this.links.size() < 1);
	}
	
	public void addLink(Link link) {
		this.getLinks().add(link);
	}

	public List<Link> getLinks() {
		if (null == this.links) {
			this.links = new LinkedList<Link>();
		}
		return this.links;
	}

	public void setTitle(Title title) {
		this.title = title;
	}

	public Title getTitle() {
		return title;
	}
	
	public boolean hasUnknownElements() {
		return !(null == this.unknownElements || this.unknownElements.size() < 1);
	}
	public void addUnknownElement(Element unknownElement) {
		this.getUnknownElements().add(unknownElement);
	}

	public List<Element> getUnknownElements() {
		if (null == this.unknownElements) {
			this.unknownElements = new LinkedList<Element>();
		}
		return unknownElements;
	}
}
