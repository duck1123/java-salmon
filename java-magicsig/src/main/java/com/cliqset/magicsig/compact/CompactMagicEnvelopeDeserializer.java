package com.cliqset.magicsig.compact;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.cliqset.magicsig.MagicEnvelopeDeserializer;
import com.cliqset.magicsig.MagicEnvelope;
import com.cliqset.magicsig.MagicSigConstants;
import com.cliqset.magicsig.MagicSignatureException;
import com.cliqset.magicsig.Signature;
import com.cliqset.magicsig.encoding.Base64URLMagicSignatureEncoding;


public class CompactMagicEnvelopeDeserializer implements MagicEnvelopeDeserializer {

	private static final int KEY_ID_INDEX = 0;
	private static final int SIG_INDEX = 1;
	private static final int DATA_INDEX = 2;
	private static final int DATA_TYPE_INDEX = 3;
	private static final int ENCODING_INDEX = 4;
	private static final int ALGORITHM_INDEX = 5;
	
	private static final String DEFAULT_ENCODING = "base64url";
	private static final String DEFAULT_ALGORITHM = "RSA-SHA256";
	
	public MagicEnvelope deserialize(InputStream is) throws MagicSignatureException {
		/*
		 * 
			1. The value of the "key_id" parameter
			2. The value of the "sig" parameter
			3. The armored string for "data" produced by Section 7.1
			4. The Base64url encoding of the "data_type" parameter
			5. The Base64url encoding of the "encoding" parameter
			6. The Base64url encoding of the "alg" parameter
		 */
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[512];
			int bytesRead = 0;
			
			while (-1 != (bytesRead = is.read(buffer))) {
				baos.write(buffer, 0, bytesRead);
			}
		
			String[] compactEnvArray = baos.toString("UTF-8").split("\\.");
			if (compactEnvArray.length < 6) {
				throw new MagicSignatureException("Compact Serialization must have 6 segments, this input only has " + compactEnvArray.length);
			}
			
			if (null == compactEnvArray[DATA_TYPE_INDEX]) {
				throw new MagicSignatureException("The data-type parameter must not be omitted.");
			}
			
			Base64URLMagicSignatureEncoding encoder = new Base64URLMagicSignatureEncoding();
			
			if (null == compactEnvArray[ALGORITHM_INDEX]) { compactEnvArray[ALGORITHM_INDEX] = encoder.encodeToString(DEFAULT_ALGORITHM.getBytes("UTF-8"));}
			if (null == compactEnvArray[ENCODING_INDEX]) { compactEnvArray[ENCODING_INDEX] = encoder.encodeToString(DEFAULT_ENCODING.getBytes("UTF-8"));}
			
			return new MagicEnvelope()
						.withSignature(new Signature().withKeyId(compactEnvArray[KEY_ID_INDEX]).withValue(compactEnvArray[SIG_INDEX]))
						.withData(compactEnvArray[DATA_INDEX])
						.withDataType(new String(encoder.decode(compactEnvArray[DATA_TYPE_INDEX]), "UTF-8"))
						.withEncoding(new String(encoder.decode(compactEnvArray[ENCODING_INDEX]), "UTF-8"))
						.withAlgorithm(new String(encoder.decode(compactEnvArray[ALGORITHM_INDEX]), "UTF-8"));
			
		} catch (IOException ie) {
			throw new MagicSignatureException("Unable to deserialize magic envelope.", ie);
		}
	}

	public List<String> getSupportedMediaTypes() {
		return Collections.unmodifiableList(Arrays.asList(MagicSigConstants.MEDIA_TYPE_MAGIC_ENV_COMPACT));
	}
}
