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
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class BraceSyntaxTemplateTest {

	@Test
	public void successfullTemplate() {
		Map<String, String> input = new HashMap<String, String>();
		input.put("uri", "http://example.com/xy");
		BraceSyntaxTemplate t = new BraceSyntaxTemplate("http://example.com/lrdd?uri={uri}");
		try {
			Assert.assertEquals(new URI("http://example.com/lrdd?uri=http%3A%2F%2Fexample.com%2Fxy"), t.process(input));
		} catch (TemplateException te) {
			Assert.fail(te.getMessage());
		} catch (URISyntaxException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void templateWithoutVars() {
		Map<String, String> input = new HashMap<String, String>();
		input.put("uri", "http://example.com/xy");
		BraceSyntaxTemplate t = new BraceSyntaxTemplate("http://example.com/lrdd");
		try {
			Assert.assertEquals(new URI("http://example.com/lrdd"), t.process(input));
		} catch (TemplateException te) {
			Assert.fail(te.getMessage());
		} catch (URISyntaxException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void nullInput() {
		BraceSyntaxTemplate t = new BraceSyntaxTemplate("http://example.com/lrdd");
		try {
			Assert.assertEquals(new URI("http://example.com/lrdd"), t.process(null));
		} catch (TemplateException te) {
			Assert.fail(te.getMessage());
		} catch (URISyntaxException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void nullTemplate() {
		try {
			new BraceSyntaxTemplate(null);
			Assert.fail("Constructor should have thrown an IllegalArgumentException");
		} catch (IllegalArgumentException iae) {
			Assert.assertTrue(true);
		} catch (Exception e) {
			Assert.fail("Expected an IllegalArgumentException");
		}
	}
}
