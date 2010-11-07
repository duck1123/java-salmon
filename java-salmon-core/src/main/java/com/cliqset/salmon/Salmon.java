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

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cliqset.magicsig.DataParser;
import com.cliqset.magicsig.EnvelopeVerificationResult;
import com.cliqset.magicsig.KeyFinder;
import com.cliqset.magicsig.MagicEnvelope;
import com.cliqset.magicsig.Key;
import com.cliqset.magicsig.MagicSig;
import com.cliqset.magicsig.MagicSigAlgorithm;
import com.cliqset.magicsig.MagicSigConstants;
import com.cliqset.magicsig.MagicSigEncoding;
import com.cliqset.magicsig.MagicSigException;
import com.cliqset.magicsig.MagicSig;
import com.cliqset.magicsig.json.JSONMagicEnvelopeDeserializer;
import com.cliqset.magicsig.json.JSONMagicEnvelopeSerializer;
import com.cliqset.magicsig.keyfinder.MagicPKIKeyFinder;
import com.cliqset.magicsig.xml.XMLMagicEnvelopeDeserializer;
import com.cliqset.magicsig.xml.XMLMagicEnvelopeSerializer;
import com.cliqset.magicsig.SignatureVerificationResult;
import com.cliqset.magicsig.URIPayloadToMetadataMapper;
import com.cliqset.magicsig.algorithm.RSASHA256MagicSigAlgorithm;
import com.cliqset.magicsig.compact.CompactMagicEnvelopeDeserializer;
import com.cliqset.magicsig.compact.CompactMagicEnvelopeSerializer;
import com.cliqset.magicsig.encoding.Base64URLMagicSigEncoding;

public class Salmon {

	public static final String REL_SALMON = "salmon";
	
	private static final Logger logger = LoggerFactory.getLogger(Salmon.class);
	
	private SalmonSender sender = new JavaNetSalmonSender();
	
	private SalmonEndpointFinder endpointFinder = new HostMetaSalmonEndpointFinder();
	
	private static final String DEFAULT_DATA_TYPE = "application/atom+xml";
	
	private static final String DEFAULT_ALGORITHM = "RSA-SHA256";
	
	private static final String DEFAULT_ENCODING = "base64url";
	
	private MagicSig magicSig = null;
	
	public Salmon() {
		Map<String, MagicSigAlgorithm> algorithms = new HashMap<String, MagicSigAlgorithm>();
		algorithms.put("RSA-SHA256", new RSASHA256MagicSigAlgorithm());
		
		Map<String, MagicSigEncoding> encodings = new HashMap<String, MagicSigEncoding>();
		encodings.put("base64url", new Base64URLMagicSigEncoding());
		
		Set<DataParser> dataParsers = new HashSet<DataParser>();
		dataParsers.add(new SimpleAtomDataParser());
		
		Set<KeyFinder> keyFinders = new HashSet<KeyFinder>();
		keyFinders.add(new MagicPKIKeyFinder());
		
		MagicSig magicSig = new MagicSig(algorithms, encodings, new URIPayloadToMetadataMapper(dataParsers, keyFinders));
		
		MagicEnvelope.withSerializer(new CompactMagicEnvelopeSerializer());
		MagicEnvelope.withSerializer(new JSONMagicEnvelopeSerializer());
		MagicEnvelope.withSerializer(new XMLMagicEnvelopeSerializer());
		
		MagicEnvelope.withDeserializer(new CompactMagicEnvelopeDeserializer());
		MagicEnvelope.withDeserializer(new JSONMagicEnvelopeDeserializer());
		MagicEnvelope.withDeserializer(new XMLMagicEnvelopeDeserializer());
	}
	
	public Salmon(MagicSig magicSig) {
		this.magicSig = magicSig;
	}
	
	/*
	public Salmon withSalmonEndpointFinder(SalmonEndpointFinder endpointFinder) {
		this.endpointFinder = endpointFinder;
		return this;
	}
	
	public Salmon withSalmonSender(SalmonSender sender) {
		this.sender = sender;
		return this;
	}
	*/
	public byte[] verify(MagicEnvelope envelope) throws SalmonException {
		try {
			EnvelopeVerificationResult result = magicSig.verify(envelope);
			for (SignatureVerificationResult sigResult : result.getSignatureVerificationResults()) {
				if (sigResult.isVerified()) {
					//salmon has one author, so if one of his keys verify, we are good
					return result.getData();
				}
			}
		} catch (MagicSigException mse) {
			throw new SalmonException(mse);
		}
		throw new SalmonException("Unable to verify the signature.");
	}
	
	public void signAndDeliver(byte[] entry, Key key, URL destinationURL) throws SalmonException {
		signAndDeliver(entry, key, destinationURL, DEFAULT_DATA_TYPE);
	}

	public void signAndDeliver(byte[] entry, Key key, URL destinationURL, String mediaType) throws SalmonException {
		signAndDeliver(entry, key, destinationURL, mediaType, DEFAULT_ENCODING, DEFAULT_ALGORITHM);
	}
	
	public void signAndDeliver(byte[] entry, Key key, URL destinationURL, String mediaType, String encoding, String algorithm) throws SalmonException {
		try {
			MagicEnvelope env = sign(entry, key);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			env.writeTo(MagicSigConstants.MEDIA_TYPE_MAGIC_ENV_XML, baos);
			send(destinationURL, MagicSigConstants.MEDIA_TYPE_MAGIC_ENV_XML, baos.toByteArray());
		} catch (MagicSigException mse) {
			throw new SalmonException(mse);
		}
	}
	
	public void signAndDeliver(byte[] entry, Key key, URI destinationUser) throws SalmonException {
		signAndDeliver(entry, key, destinationUser, DEFAULT_DATA_TYPE, DEFAULT_ENCODING, DEFAULT_ALGORITHM);
	}
	
	public void signAndDeliver(byte[] entry, Key key, URI destinationUser, String mediaType) throws SalmonException {
		signAndDeliver(entry, key, destinationUser, mediaType, DEFAULT_ENCODING, DEFAULT_ALGORITHM);
	}
	
	public void signAndDeliver(byte[] entry, Key key, URI destinationUser, String mediaType, String encoding, String algorithm) throws SalmonException {
		URL destinationURL = findSalmonEndpoint(destinationUser);
		signAndDeliver(entry, key, destinationURL, mediaType, encoding, algorithm);
	}
	
	private URL findSalmonEndpoint(URI resourceURI) throws SalmonException {
		if (null == this.endpointFinder) {
			throw new SalmonException("A SalmonEndpointFinder must be configured.");
		}
		return this.endpointFinder.find(resourceURI);
	}
	
	public MagicEnvelope sign(byte[] entry, Key key) throws MagicSigException {
		return magicSig.sign(entry, key, DEFAULT_ALGORITHM, DEFAULT_ENCODING, DEFAULT_DATA_TYPE);
	}
	
	private void send(URL destinationURL, String contentType, byte[] data) throws SalmonException {
		if (null == this.sender) {
			throw new SalmonException("Please assign a SalmonSender.");
		}
		this.sender.send(destinationURL, contentType, data);
	}
}
