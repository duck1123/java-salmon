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

import org.junit.Assert;
import org.junit.Test;

public class LRDDTemplateProcessorTest {

	@Test
	public void successfulProcessOne() {
		try {
			LRDDTemplateProcessor p = new LRDDTemplateProcessor();
			Assert.assertEquals(new URI("http://example.com/lrdd?uri=http%3A%2F%2Fexample.com%2Fxy"), p.process("http://example.com/lrdd?uri={uri}", new URI("http://example.com/xy")));
		} catch (TemplateException e) {
			Assert.fail();
		} catch (URISyntaxException e) {
			Assert.fail();
		}
	}
	
	@Test
	public void nullTemplate() {
		try {
			LRDDTemplateProcessor p = new LRDDTemplateProcessor();
			Assert.assertEquals(new URI("http://example.com/lrdd?uri=http%3A%2F%2Fexample.com%2Fxy"), p.process(null, new URI("http://example.com/xy")));
			Assert.fail("Should have received an IllegalArgumentException");
		} catch (IllegalArgumentException iae) {
			Assert.assertTrue(true);
		} catch (TemplateException e) {
			Assert.fail();
		} catch (URISyntaxException e) {
			Assert.fail();
		}
	}
	
	@Test
	public void nullContextResourceURI() {
		try {
			LRDDTemplateProcessor p = new LRDDTemplateProcessor();
			Assert.assertEquals(new URI("http://example.com/lrdd?uri=http%3A%2F%2Fexample.com%2Fxy"), p.process("http://example.com/lrdd?uri={uri}", null));
			Assert.fail("Should have received an IllegalArgumentException");
		} catch (IllegalArgumentException iae) {
			Assert.assertTrue(true);
		} catch (TemplateException e) {
			Assert.fail();
		} catch (URISyntaxException e) {
			Assert.fail();
		}
	}
	
	@Test
	public void hostSpecificTemplate() {
		try {
			LRDDTemplateProcessor p = new LRDDTemplateProcessor();
			Assert.assertEquals(new URI("http://example.com/author"), p.process("http://example.com/author", new URI("http://example.com/xy")));
		} catch (TemplateException e) {
			Assert.fail();
		} catch (URISyntaxException e) {
			Assert.fail();
		}
	}
}
