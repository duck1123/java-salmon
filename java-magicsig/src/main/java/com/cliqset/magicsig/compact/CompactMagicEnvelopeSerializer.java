package com.cliqset.magicsig.compact;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cliqset.magicsig.MagicEnvelope;
import com.cliqset.magicsig.MagicEnvelopeSerializer;
import com.cliqset.magicsig.MagicSigConstants;
import com.cliqset.magicsig.MagicSigException;
import com.cliqset.magicsig.encoding.Base64URLMagicSigEncoding;

public class CompactMagicEnvelopeSerializer implements MagicEnvelopeSerializer {

	public static final String MEDIA_TYPE = MagicSigConstants.MEDIA_TYPE_MAGIC_ENV_COMPACT;
	
	private static Logger logger = LoggerFactory.getLogger(CompactMagicEnvelopeSerializer.class);

	public void serialize(MagicEnvelope env, OutputStream os) throws MagicSigException {
		/*
			1. The value of the "key_id" parameter
			2. The value of the "sig" parameter
			3. The armored string for "data" produced by Section 7.1
			4. The Base64url encoding of the "data_type" parameter
			5. The Base64url encoding of the "encoding" parameter
			6. The Base64url encoding of the "alg" parameter
		 */
		Base64URLMagicSigEncoding encoder = new Base64URLMagicSigEncoding();
		StringBuilder sb = new StringBuilder();
		
		try {
			if (null != env.getSignatures() && null != env.getSignatures().get(0)) { sb.append(env.getSignatures().get(0).getKeyId()); }
			sb.append(".");
			if (null != env.getSignatures() && null != env.getSignatures().get(0)) { sb.append(env.getSignatures().get(0).getValue()); }
			sb.append(".");
			sb.append(env.getData());
			sb.append(".");
			sb.append(encoder.encodeToString(env.getDataType().getBytes("UTF-8")));
			sb.append(".");
			sb.append(encoder.encodeToString(env.getEncoding().getBytes("UTF-8")));
			sb.append(".");
			sb.append(encoder.encodeToString(env.getAlgorithm().getBytes("UTF-8")));
		} catch (UnsupportedEncodingException uee) {
			//shouldn't happen right?
			logger.error("No UTF-8 on this system.");
		}
		try {
			os.write(sb.toString().getBytes());
		} catch (IOException ioe) {
			throw new MagicSigException("Unable to serialize magic envelope to output stream.", ioe);
		}
	}
}
