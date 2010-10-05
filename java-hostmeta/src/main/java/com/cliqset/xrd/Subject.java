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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.namespace.QName;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@XmlType(name="Subject", namespace=XRDConstants.XRD_NAMESPACE)
@XmlAccessorType(XmlAccessType.FIELD)
public class Subject {

	@XmlAnyAttribute()
	private Map<QName, Object> unknownAttributes;
	
	@XmlValue
	private URI value;

	public void setValue(URI value) {
		this.value = value;
	}

	public URI getValue() {
		return value;
	}

	public void setUnknownAttributes(Map<QName, Object> unknownAttributes) {
		this.unknownAttributes = unknownAttributes;
	}

	public Map<QName, Object> getUnknownAttributes() {
		if (null == this.getUnknownAttributes()) {
			this.unknownAttributes = new HashMap<QName, Object>();
		}
		return unknownAttributes;
	}
	
	public boolean hasUnknownAttributes() {
		return !(null == this.unknownAttributes || this.unknownAttributes.size() < 1);
	}
}
