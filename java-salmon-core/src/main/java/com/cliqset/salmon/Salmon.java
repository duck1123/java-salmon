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

import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cliqset.magicsig.EnvelopeVerificationResult;
import com.cliqset.magicsig.MagicEnvelope;
import com.cliqset.magicsig.Key;
import com.cliqset.magicsig.MagicSignatureException;
import com.cliqset.magicsig.MagicSigner;
import com.cliqset.magicsig.SignatureVerificationResult;
import com.cliqset.magicsig.algorithm.RSASHA256MagicSignatureAlgorithm;
import com.cliqset.magicsig.encoding.Base64URLMagicSignatureEncoding;

public class Salmon {

	public static final String REL_SALMON = "salmon"; 
	
	private static final Logger logger = LoggerFactory.getLogger(Salmon.class);
	
	private List<DataParser> dataParsers = new LinkedList<DataParser>();
	
	private List<KeyFinder> keyFinders = new LinkedList<KeyFinder>();
	
	private static final String DEFAULT_DATA_TYPE = "application/atom+xml";
	
	private static final String DEFAULT_ALGORITHM = "RSA-SHA256";
	
	private static final String DEFAULT_ENCODING = "base64url";
	
	private MagicSigner magicSig = null;
	
	public Salmon() {
		this.magicSig = new MagicSigner()
			.withEncoding(new Base64URLMagicSignatureEncoding())
			.withAlgorithm(new RSASHA256MagicSignatureAlgorithm());
	}
	
	public Salmon(MagicSigner magicSig) {
		this.magicSig = magicSig;
	}
	
	public Salmon withDataParser(DataParser parser) {
		if (null == parser) {
			throw new IllegalArgumentException("parser must not be null.");
		}
		dataParsers.add(parser);
		return this;
	}
	
	public Salmon withKeyFinder(KeyFinder keyFinder) {
		if (null == keyFinder) {
			throw new IllegalArgumentException("keyfinder must not be null.");
		}
		keyFinders.add(keyFinder);
		return this;
	}
	
	public byte[] verify(MagicEnvelope envelope) throws SalmonException {
		try {
			//get key
			URI authorURI = extractSignerUri(envelope.getDataType(), this.magicSig.decodeData(envelope));
			
			List<Key> authorKeys = findKeys(authorURI);
			
			if (authorKeys.size() < 1) {
				throw new SalmonException("Unable to locate any magic public keys for author URI: " + authorURI.toString());
			}
			
			return verify(envelope, authorKeys);
		} catch (MagicSignatureException mse) {
			throw new SalmonException(mse);
		}
	}
	
	public byte[] verify(MagicEnvelope envelope, List<? extends Key> authorKeys) throws SalmonException {
		try {
			EnvelopeVerificationResult result = magicSig.verify(envelope, authorKeys);
			for (SignatureVerificationResult sigResult : result.getSignatureVerificationResults()) {
				if (sigResult.isVerified()) {
					//salmon has one author, so if one of his keys verify, we are good
					return result.getData();
				}
			}
		} catch (MagicSignatureException mse) {
			throw new SalmonException(mse);
		}

		throw new SalmonException("Unable to verify the signature.");
	}
	
	// TODO: make this deliver the salmon too, the lib should incorporate the discovery and posting to the endpoint
	// support different strategies like we do for the KeyFinder and  DataParser
	public MagicEnvelope sign(byte[] entry, Key key) throws Exception {
		return magicSig.sign(entry, key, DEFAULT_ALGORITHM, DEFAULT_ENCODING, DEFAULT_DATA_TYPE);
	}
	
	private URI extractSignerUri(String mimeType, byte[] data) throws SalmonException {
		for (DataParser parser : this.dataParsers) {
			if (parser.parsesMimeType(mimeType)) {
				try {
					return parser.getSignerUri(data);
				} catch (Exception e) { 
					//ignore and try the next one
				}
			}
		}
		throw new SalmonException("Unable to extract signer URI from data.");
	}
	
	private List<Key> findKeys(URI authorUri) throws SalmonException {
		for (KeyFinder finder : this.keyFinders) {
			try {
				List<Key> keys = finder.findKeys(authorUri); 
				if (null != keys && keys.size() > 0) { return keys; }
			} catch (Exception e) { 
				//ignore and try the next one
			}
		}
		throw new SalmonException("Unable to find keys for signer: " + authorUri.toString());
	}
}
