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

package com.cliqset.salmon;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

import org.apache.abdera.Abdera;
import org.apache.abdera.i18n.iri.IRI;
import org.apache.abdera.i18n.iri.IRISyntaxException;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Person;
import org.apache.abdera.parser.ParseException;
import org.apache.abdera.parser.Parser;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Salmon {

	private static final String REL_MAGIC_KEY = "magic-public-key";
	private static final String SCHEME_DATA = "data";
	private static final String SCHEME_HTTP = "http";
	private static final String SCHEME_HTTPS = "https";
	
	private static final HttpClient httpClient = new DefaultHttpClient();
	
	private static final DiscoveryManager discoveryManager;
	
	private static final Abdera abdera;
	
	private static final Logger logger = LoggerFactory.getLogger(Salmon.class);
	
	static {
		
		try {
			DefaultBootstrap.bootstrap();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
		
		abdera = Abdera.getInstance();
		
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
	
	public static Entry verify(MagicEnvelope envelope) throws SalmonException {
		String encodedData = envelope.getData().getValue();
		
		byte[] data = MagicSigUtil.decode(encodedData);
		
		try {
			logger.debug("verifying:{} ({})", encodedData, new String(data, "ASCII"));
		} catch (UnsupportedEncodingException e) {}
		
		Entry entry = parseEntry(data);
		
		//get key
		URI authorURI = getAuthorURI(entry);
		List<MagicKey> authorKeys = getKeys(authorURI);
		
		if (authorKeys.size() < 1) {
			throw new SalmonException("Unable to locate any magic public keys for author URI: " + authorURI.toString());
		}
		
		return verify(envelope, authorKeys);
	}

	public static Entry verify(MagicEnvelope envelope, List<MagicKey> authorKeys) throws SalmonException {
		byte[] encodedData = new byte[0];
		
		try {
			encodedData = envelope.getData().getValue().getBytes("ASCII");
		} catch (Exception e){}
		
		byte[] data = MagicSigUtil.decode(envelope.getData().getValue());
		
		byte[] sig = MagicSigUtil.decode(envelope.getSig());
		
		try {
			logger.debug("verifying signature:{} ({})", envelope.getSig(), new String(data, "UTF-8"));
		} catch (UnsupportedEncodingException e) {}
		
		logger.debug("Verifying signature with " + authorKeys.size() + " keys");
		
		for (MagicKey key : authorKeys) {
			logger.debug("Verifying signature with:{}", key.toString());
			if (MagicSigUtil.verify(encodedData, sig, key)) {
				Entry entry = parseEntry(data);
				return entry;
			}
		}
		throw new SalmonException("Unable to verify the signature.");
	}
	
	public static Entry parseEntry(byte[] entry) throws SalmonException {
		try {
			Parser parser = abdera.getParser();
			Document<Entry> entryDoc = parser.parse(new ByteArrayInputStream(entry));
			return  entryDoc.getRoot();
		} catch (ParseException pe) {
			throw new SalmonException("Unable to parse salmon as ATOM entry", pe);
		}
	}
	
	public static URI getAuthorURI(Entry entry) throws SalmonException {
		Person author = entry.getAuthor();
		if (null == author) {
			throw new SalmonException("No author element on the entry.");
		}
		
		IRI authorIRI = null;
		try {
			authorIRI = author.getUri();
			return new URI(authorIRI.toString());
		} catch (IRISyntaxException ise) {
			throw new SalmonException("Invalid author URI", ise);
		} catch (URISyntaxException use) {
			throw new SalmonException("Invalid author URI", use);
		}
	}
	
	public static List<MagicKey> getKeys(URI authorUri) throws SalmonException {
		XRD xrd = discoveryManager.discover(authorUri);

		List<MagicKey> magicKeys = new LinkedList<MagicKey>();

		for (Link link : xrd.getLinks()) {
			if (REL_MAGIC_KEY.equals(link.getRel())) {
				magicKeys.add(fetchKey(URI.create(link.getHref())));
			}
		}
		
		return magicKeys;
	}
	
	public static MagicKey fetchKey(URI uri) throws SalmonException {
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
				throw new SalmonException(cpe);
			} catch (IOException ioe) {
				throw new SalmonException(ioe);
			}
		} else {
			throw new SalmonException("URI Scheme " + uri.getScheme() + " is not supported when resolving magic key.");
		}
	}
	
	public static MagicEnvelope sign(byte[] entry, MagicKey key) throws Exception {
		MagicEnvelope env = new MagicEnvelope();
		
		env.setAlg(MagicSigUtil.ALGORITHM);
		MagicEnvelopeData data = new MagicEnvelopeData();
		data.setValue(MagicSigUtil.encodeToString(entry));
		data.setType("application/atom+xml");
		env.setData(data);
		env.setEncoding(MagicSigUtil.ENCODING);
		env.setSig(MagicSigUtil.encodeToString(MagicSigUtil.sign(MagicSigUtil.encode(entry), key)));
		
		return env;
	}
}
