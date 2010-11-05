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
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cliqset.magicsig.EnvelopeVerificationResult;
import com.cliqset.magicsig.MagicEnvelope;
import com.cliqset.magicsig.Key;
import com.cliqset.magicsig.MagicSigConstants;
import com.cliqset.magicsig.MagicSignatureException;
import com.cliqset.magicsig.MagicSigner;
import com.cliqset.magicsig.SignatureVerificationResult;
import com.cliqset.magicsig.algorithm.RSASHA256MagicSignatureAlgorithm;
import com.cliqset.magicsig.encoding.Base64URLMagicSignatureEncoding;

public class Salmon {

	public static final String REL_SALMON = "salmon";
	
	private static final Logger logger = LoggerFactory.getLogger(Salmon.class);
	
	private SalmonSender sender = new JavaNetSalmonSender();
	
	private SalmonEndpointFinder finder = new HostMetaSalmonEndpointFinder();
	
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
	
	public Salmon withSalmonSender(SalmonSender sender) {
		this.sender = sender;
		return this;
	}
	
	public byte[] verify(MagicEnvelope envelope) throws SalmonException {
		try {
			EnvelopeVerificationResult result = magicSig.verify(envelope);
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
			env.writeTo(mediaType, baos);
			send(destinationURL, MagicSigConstants.MEDIA_TYPE_MAGIC_ENV_XML, baos.toByteArray());
		} catch (MagicSignatureException mse) {
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
		try {
			//discover salmon endpoint
			URL destinationURL = new URL("");
			signAndDeliver(entry, key, destinationURL, mediaType, encoding, algorithm);
		} catch (MalformedURLException mue) {
			throw new SalmonException("Salmon endpoint for user: " + destinationUser + " is not a valid URL", mue);
		}
	}
	
	public URL findSalmonEndpoint(URI resourceURI) throws SalmonException {
		if (null == this.finder) {
			throw new SalmonException("A SalmonEndpointFinder must be configured.");
		}
		return this.finder.find(resourceURI);
	}
	
	public MagicEnvelope sign(byte[] entry, Key key) throws MagicSignatureException {
		return magicSig.sign(entry, key, DEFAULT_ALGORITHM, DEFAULT_ENCODING, DEFAULT_DATA_TYPE);
	}
	
	private void send(URL destinationURL, String contentType, byte[] data) throws SalmonException {
		if (null == this.sender) {
			throw new SalmonException("Please assign a SalmonSender.");
		}
		this.sender.send(destinationURL, contentType, data);
	}
}
