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

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Salmon {

	private static final Logger logger = LoggerFactory.getLogger(Salmon.class);
	
	private List<DataParser> dataParsers = new LinkedList<DataParser>();
	
	private List<KeyFinder> keyFinders = new LinkedList<KeyFinder>();
	
	private static final String DATA_TYPE = "application/atom+xml";
	
	public Salmon() {}
	
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
		String encodedData = envelope.getData().getValue();
		
		byte[] data = MagicSigUtil.decode(encodedData);
		
		try {
			logger.debug("verifying:{} ({})", encodedData, new String(data, "ASCII"));
		} catch (UnsupportedEncodingException e) {}
		
		//get key
		URI authorURI = extractSignerUri(envelope.getData().getType(), data);
		
		List<MagicKey> authorKeys = findKeys(authorURI);
		
		if (authorKeys.size() < 1) {
			throw new SalmonException("Unable to locate any magic public keys for author URI: " + authorURI.toString());
		}
		
		return verify(envelope, authorKeys);
	}
	
	public byte[] verify(MagicEnvelope envelope, List<MagicKey> authorKeys) throws SalmonException {
		
		byte[] data = MagicSigUtil.decode(envelope.getData().getValue());
		
		byte[] sig = MagicSigUtil.decode(envelope.getSig().getValue());
		
		byte[] dataForSig;
		
		try {
			dataForSig = buildDataForSig(data, envelope.getData().getType(), envelope.getEncoding(), envelope.getAlg());
		} catch (Exception e) {
			throw new SalmonException("Unable to build data for signature verification.", e);
		}
		
		String signatureKeyhash = envelope.getSig().getKeyhash();
		
		try {
			logger.debug("verifying signature:{} ({})", envelope.getSig(), new String(data, "UTF-8"));
		} catch (UnsupportedEncodingException e) {}
		
		logger.debug("Verifying signature with " + authorKeys.size() + " keys");
		
		for (MagicKey key : authorKeys) {
			String keyhash = key.getKeyhash(); 
			if (null == signatureKeyhash || keyhash.equals(signatureKeyhash)) {
				logger.debug("Verifying signature with:{}", key.toString());
				if (MagicSigUtil.verify(dataForSig, sig, key)) {
					return data;
				} else {
					logger.info("Key {} matched keyhash {}, but didn't verify.", key.toString(), keyhash);
				}
			} else {
				logger.debug("Key {} with keyhash of {} does not match signature keyhash of {}", new String[] {key.toString(), keyhash, signatureKeyhash});
			}
		}
		throw new SalmonException("Unable to verify the signature.");
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
	
	private List<MagicKey> findKeys(URI authorUri) throws SalmonException {
		for (KeyFinder finder : this.keyFinders) {
			try {
				List<MagicKey> keys = finder.findKeys(authorUri); 
				if (null != keys && keys.size() > 0) { return keys; }
			} catch (Exception e) { 
				//ignore and try the next one
			}
		}
		throw new SalmonException("Unable to find keys for signer: " + authorUri.toString());
	}
	
	public static MagicEnvelope sign(byte[] entry, MagicKey key) throws Exception {
		return sign(entry, key, MagicSigUtil.ALGORITHM, MagicSigUtil.ENCODING, DATA_TYPE);
	}
	
	private static MagicEnvelope sign(byte[] data, MagicKey key, String algorithm, String encoding, String dataType) throws Exception {
		MagicEnvelope env = new MagicEnvelope();
		
		env.setAlg(algorithm);
		MagicEnvelopeData envelopeData = new MagicEnvelopeData();
		byte[] signatureData = buildDataForSig(data, dataType, encoding, algorithm);
		envelopeData.setValue(MagicSigUtil.encodeToString(data));
		envelopeData.setType(dataType);
		env.setData(envelopeData);
		env.setEncoding(encoding);
		env.setSig(new MagicEnvelopeSignature(key.getKeyhash(), MagicSigUtil.encodeToString(MagicSigUtil.sign(signatureData, key))));
		
		return env;
	}
	
	private static byte[] buildDataForSig(byte[] data, String dataType, String encoding, String algorithm) throws Exception {
		StringBuilder sb = new StringBuilder(MagicSigUtil.encodeToString(data));
		sb.append(".");
		sb.append(MagicSigUtil.encodeToString(dataType.getBytes("ASCII")));
		sb.append(".");
		sb.append(MagicSigUtil.encodeToString(encoding.getBytes("ASCII")));
		sb.append(".");
		sb.append(MagicSigUtil.encodeToString(algorithm.getBytes("ASCII")));
		
		return sb.toString().getBytes("ASCII");
	}
}
