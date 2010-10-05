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

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BraceSyntaxTemplate {
	
	private static final String variablePattern = "\\{(.+)\\}";
	
	private String template;
	
	public BraceSyntaxTemplate(String template) {
		if (null == template) { throw new IllegalArgumentException("template must not be null."); }
		this.template = template;
	}
	
	public URI process(Map<String, String> input) throws TemplateException {
		StringBuffer output = new StringBuffer();
		
		Pattern pattern = Pattern.compile(variablePattern);
		
		Matcher matcher = pattern.matcher(template);

        while (matcher.find()) {
            String variableName = matcher.group(1);
            String value;

            if (input.containsKey(variableName)) {
            	try {
            		value = URLEncoder.encode(input.get(variableName), "UTF-8");
            	} catch (UnsupportedEncodingException uee) {
            		throw new TemplateException("No UTF-8 on this system.", uee);
            	}
            } else {
                value = "";
            }

            matcher.appendReplacement(output, value);
        }

        matcher.appendTail(output);
        try {
        	return new URI(output.toString());
        } catch (URISyntaxException use) {
        	throw new TemplateException("Result of template processing is not a URI", use);
        }
	}
}
	