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

import org.w3c.dom.Element;

import com.cliqset.xrd.Alias;
import com.cliqset.xrd.Expires;
import com.cliqset.xrd.Link;
import com.cliqset.xrd.Property;
import com.cliqset.xrd.Subject;
import com.cliqset.xrd.Title;
import com.cliqset.xrd.XRD;

public class HostMetaHandler {

	public boolean onAlias(Alias alias) { return false; }

	public boolean onExpires(Expires expires) { return false; }

	public boolean onLink(Link link) { return false; }

	public boolean onProperty(Property property) { return false; }

	public boolean onSubject(Subject subject) { return false; }

	public boolean onTitle(Title title) { return false; }

	public boolean onXRD(XRD xrd) { return false; }
	
	public boolean onUnknownElement(Element element) { return false; }
	
	public boolean onLinkVisit(Link link) { return false; }
	
	public boolean onLinkVisited(Link link) { return false; }
}
