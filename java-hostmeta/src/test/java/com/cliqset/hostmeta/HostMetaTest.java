package com.cliqset.hostmeta;

import java.net.URI;
import junit.framework.Assert;
import org.junit.Test;
import com.cliqset.hostmeta.template.LRDDTemplateProcessor;

public class HostMetaTest {

	@Test
	public void googleLRDD() {
		try {
			HostMeta hm = new HostMeta()
				.withTemplateProcessor("lrdd", new LRDDTemplateProcessor())
				.withXRDFetcher(new JavaNetXRDFetcher());
		
			Descriptor d = hm.discoverResourceSpecific(URI.create("acct:charlie.cauthen@gmail.com"));
			Assert.assertEquals(URI.create("acct:charlie.cauthen@gmail.com"), d.getSubject().getValue());
			Assert.assertEquals(URI.create("http://www.google.com/profiles/charlie.cauthen"), d.getAliases().get(0).getValue());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
}
