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

package com.cliqset.hostmeta.template;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class LRDDTemplateProcessor implements TemplateProcessor {
	
	public URI process(String template, URI contextResourceURI) throws TemplateException {
		if (null == template) { throw new IllegalArgumentException("template must not be null."); }
		if (null == contextResourceURI) { throw new IllegalArgumentException("contextResourceURI must not be null."); }
		
		Map<String, String> input = new HashMap<String, String>();
		input.put("uri", contextResourceURI.toString());
		
		BraceSyntaxTemplate t = new BraceSyntaxTemplate(template);
		
		return t.process(input);
	}
}
