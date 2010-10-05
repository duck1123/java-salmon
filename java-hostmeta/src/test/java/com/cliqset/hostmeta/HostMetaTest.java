package com.cliqset.hostmeta;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.w3c.dom.Element;

import com.cliqset.hostmeta.template.LRDDTemplateProcessor;
import javax.xml.namespace.*;
import com.cliqset.xrd.Alias;
import com.cliqset.xrd.Expires;
import com.cliqset.xrd.Link;
import com.cliqset.xrd.Property;
import com.cliqset.xrd.Subject;
import com.cliqset.xrd.Title;
import com.cliqset.xrd.XRD;

public class HostMetaTest {

	@Test
	public void googleLRDD() {
		try {
			HostMeta hm = new HostMeta()
				.withTemplateProcessor("lrdd", new LRDDTemplateProcessor());
		
			Descriptor d = hm.discoverResourceSpecific(URI.create("acct:charlie.cauthen@gmail.com"));
			Assert.assertEquals(URI.create("acct:charlie.cauthen@gmail.com"), d.getSubject().getValue());
			Assert.assertEquals(URI.create("http://www.google.com/profiles/charlie.cauthen"), d.getAliases().get(0).getValue());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
}
