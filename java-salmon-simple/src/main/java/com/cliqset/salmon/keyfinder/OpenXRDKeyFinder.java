package com.cliqset.salmon.keyfinder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.parse.BasicParserPool;
import org.openxrd.DefaultBootstrap;
import org.openxrd.discovery.DiscoveryManager;
import org.openxrd.discovery.impl.BasicDiscoveryManager;
import org.openxrd.discovery.impl.HostMetaDiscoveryMethod;
import org.openxrd.discovery.impl.HtmlLinkDiscoveryMethod;
import org.openxrd.discovery.impl.HttpHeaderDiscoveryMethod;
import org.openxrd.xrd.core.Link;
import org.openxrd.xrd.core.XRD;

import com.cliqset.magicsig.Key;
import com.cliqset.magicsig.MagicKey;
import com.cliqset.magicsig.KeyFinder;
import com.cliqset.magicsig.MagicSigException;

public class OpenXRDKeyFinder implements KeyFinder {

	private static final String REL_MAGIC_KEY = "magic-public-key";
	private static final String SCHEME_DATA = "data";
	private static final String SCHEME_HTTP = "http";
	private static final String SCHEME_HTTPS = "https";
	
	private static DiscoveryManager discoveryManager;
	private static final HttpClient httpClient = new DefaultHttpClient();
	
	static {
		try {
			DefaultBootstrap.bootstrap();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
		
		discoveryManager = new BasicDiscoveryManager();
		
		HostMetaDiscoveryMethod hostMeta = new HostMetaDiscoveryMethod();
	    hostMeta.setHttpClient(httpClient);
	    hostMeta.setParserPool(new BasicParserPool());
		discoveryManager.getDiscoveryMethods().add(hostMeta);
		
		HttpHeaderDiscoveryMethod header = new HttpHeaderDiscoveryMethod();
		header.setHttpClient(httpClient);
	    header.setParserPool(new BasicParserPool());
		
		discoveryManager.getDiscoveryMethods().add(header);
		
		HtmlLinkDiscoveryMethod link = new HtmlLinkDiscoveryMethod();
		link.setHttpClient(httpClient);
	    link.setParserPool(new BasicParserPool());
		
	    discoveryManager.getDiscoveryMethods().add(link);
	}

	public List<Key> findKeys(URI signerUri) throws MagicSigException {
		XRD xrd = discoveryManager.discover(signerUri);

		List<Key> magicKeys = new LinkedList<Key>();

		for (Link link : xrd.getLinks()) {
			if (REL_MAGIC_KEY.equals(link.getRel())) {
				magicKeys.add(fetchKey(URI.create(link.getHref())));
			}
		}
		
		return magicKeys;
	}
	
	private MagicKey fetchKey(URI uri) throws MagicSigException {
		if (SCHEME_DATA.equals(uri.getScheme())) {
			String data = uri.getSchemeSpecificPart();
			
			if (data.contains(",")) {
				String[] split = data.split(",");
				byte[] dataBytes = null;
				try {
					dataBytes = split[1].getBytes("ASCII");
				} catch (UnsupportedEncodingException e) {}
				return new MagicKey(dataBytes);
				//work around for status.net 
			} else {
				String[] split = data.split(";");
				byte[] dataBytes = null;
				try {
					dataBytes = split[1].getBytes("ASCII");
				} catch (UnsupportedEncodingException e) {}
				return new MagicKey(dataBytes);
			}
		} else if (SCHEME_HTTP.equals(uri.getScheme()) || SCHEME_HTTPS.equals(uri.getScheme())) {
			HttpGet get = new HttpGet(uri);
			try {
				HttpResponse response = httpClient.execute(get);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				response.getEntity().writeTo(baos);
				return new MagicKey(baos.toByteArray());
			} catch (ClientProtocolException cpe) {
				throw new MagicSigException(cpe);
			} catch (IOException ioe) {
				throw new MagicSigException(ioe);
			}
		} else {
			throw new MagicSigException("URI Scheme " + uri.getScheme() + " is not supported when resolving magic key.");
		}
	}
}
