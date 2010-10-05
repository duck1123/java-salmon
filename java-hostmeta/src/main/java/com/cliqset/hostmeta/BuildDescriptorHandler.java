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

import java.util.Map.Entry;

import javax.xml.namespace.QName;

import org.w3c.dom.Element;

import com.cliqset.xrd.Alias;
import com.cliqset.xrd.Expires;
import com.cliqset.xrd.Link;
import com.cliqset.xrd.Property;
import com.cliqset.xrd.Subject;
import com.cliqset.xrd.Title;
import com.cliqset.xrd.XRD;

public class BuildDescriptorHandler extends HostMetaHandler {

	private Descriptor descriptor = new Descriptor();

	@Override
	public boolean onAlias(Alias alias) {
		this.descriptor.getAliases().add(alias);
		return false;
	}

	@Override
	public boolean onExpires(Expires expires) {
		this.descriptor.setExpires(expires);
		return false;
	}

	@Override
	public boolean onLink(Link link) {
		this.descriptor.getLinks().add(link);
		return false;
	}

	@Override
	public boolean onProperty(Property property) {
		this.descriptor.getProperties().add(property);
		return false;
	}

	@Override
	public boolean onSubject(Subject subject) {
		this.descriptor.setSubject(subject);
		return false;
	}

	@Override
	public boolean onTitle(Title title) {
		this.descriptor.setTitle(title);
		return false;
	}

	@Override
	public boolean onUnknownElement(Element element) {
		this.descriptor.getUnknownElements().add(element);
		return false;
	}

	@Override
	public boolean onXRD(XRD xrd) {
		for (Entry<QName, Object> a: xrd.getUnknownAttributes().entrySet()) {
			this.descriptor.addUnknownAttribute(a.getKey(), a.getValue());
		}
		return false;
	}
	
	@Override
	public boolean onLinkVisited(Link link) {
		return false;
	}

	@Override
	public boolean onLinkVisit(Link link) {
		return false;
	}
	
	public Descriptor getDescriptor() {
		return this.descriptor;
	}
}
