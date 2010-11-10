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

import junit.framework.Assert;
import org.junit.Test;

public class HostMetaTest {

	@Test
	public void googleLRDD() {
		try {
			HostMeta hm = HostMeta.getDefault();
		
			Descriptor d = hm.discoverResourceSpecific(URI.create("acct:charlie.cauthen@gmail.com"));
			Assert.assertEquals(URI.create("acct:charlie.cauthen@gmail.com"), d.getSubject().getValue());
			Assert.assertEquals(URI.create("http://www.google.com/profiles/charlie.cauthen"), d.getAliases().get(0).getValue());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
}
