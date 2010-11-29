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

import java.net.URI;
import java.net.URL;

import junit.framework.Assert;
import org.junit.Test;

import com.cliqset.hostmeta.template.LRDDTemplateProcessor;
import com.cliqset.hostmeta.template.TemplateProcessor;
import com.cliqset.xrd.XRD;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.multibindings.MapBinder;

public class HostMetaTest {

	@Test
	public void testDescriptor() {
		try {
			Injector i = Guice.createInjector(new AbstractModule() {
				@Override
				protected void configure() {
					MapBinder<String, TemplateProcessor> templateBinder = MapBinder.newMapBinder(binder(), String.class, TemplateProcessor.class);
				    templateBinder.addBinding(LRDDTemplateProcessor.REL).to(LRDDTemplateProcessor.class);
				    
					bind(XRDFetcher.class).to(TestResourceXRDFetcher.class);
				}
			});
			
			HostMeta hm = i.getInstance(HostMeta.class);
		
			Descriptor d = hm.discoverResourceSpecific(URI.create("acct:charlie@fakedomain.com"));
			Assert.assertEquals(URI.create("acct:charlie@fakedomain.com"), d.getSubject().getValue());
			Assert.assertEquals(URI.create("http://www.fakedomain.com/profiles/charlie"), d.getAliases().get(0).getValue());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
	public static class TestResourceXRDFetcher implements XRDFetcher {

		public XRD fetchXRD(URL url) throws HostMetaException {
			try {
				if (url.equals(new URL("https://fakedomain.com/.well-known/host-meta"))) {
					return XRD.fromStream(HostMetaTest.class.getResourceAsStream("/hostmeta.xml"));
				} else if (url.equals(new URL("http://fakedomain.com/.well-known/host-meta"))) {
					return XRD.fromStream(HostMetaTest.class.getResourceAsStream("/hostmeta.xml"));
				} else if (url.equals(new URL("https://fakedomain.com/lrddone?q=acct%3Acharlie%40fakedomain.com"))) {
					return XRD.fromStream(HostMetaTest.class.getResourceAsStream("/lrddone.xml"));
				} else if (url.equals(new URL("http://fakedomain.com/lrddone?q=acct%3Acharlie%40fakedomain.com"))) {
					return XRD.fromStream(HostMetaTest.class.getResourceAsStream("/lrddone.xml"));	
				} else {
					throw new HostMetaException("unrecognized URL " + url);
				}
			} catch (Exception e) {
				throw new HostMetaException(e);
			}
		}
	}
}
