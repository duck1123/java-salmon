package com.cliqset.hostmeta;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import org.junit.Test;
import com.cliqset.hostmeta.template.LRDDTemplateProcessor;
import com.cliqset.hostmeta.template.TemplateProcessor;

public class HostMetaTest {

	@Test
	public void googleLRDD() {
		try {
			Map<String, TemplateProcessor> templateProcessors = new HashMap<String, TemplateProcessor>();
			templateProcessors.put("lrdd", new LRDDTemplateProcessor());
			
			HostMeta hm = new HostMeta(templateProcessors, new JavaNetXRDFetcher());
		
			Descriptor d = hm.discoverResourceSpecific(URI.create("acct:charlie.cauthen@gmail.com"));
			Assert.assertEquals(URI.create("acct:charlie.cauthen@gmail.com"), d.getSubject().getValue());
			Assert.assertEquals(URI.create("http://www.google.com/profiles/charlie.cauthen"), d.getAliases().get(0).getValue());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
}
