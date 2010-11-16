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
package com.cliqset.magicsig.json;

import java.io.InputStream;
import java.io.InputStreamReader;

import com.cliqset.magicsig.MagicEnvelopeDeserializer;
import com.cliqset.magicsig.MagicEnvelope;
import com.cliqset.magicsig.MagicSigConstants;
import com.cliqset.magicsig.MagicSigException;
import com.cliqset.magicsig.Signature;
import com.google.gson.Gson;

public class JSONMagicEnvelopeDeserializer implements MagicEnvelopeDeserializer {

	public static final String MEDIA_TYPE = MagicSigConstants.MEDIA_TYPE_MAGIC_ENV_JSON;
	
	public MagicEnvelope deserialize(InputStream is) throws MagicSigException {
		if (null == is) {
			throw new IllegalArgumentException("Cannot deserialize from null input stream.");
		}
		
		JSONMagicEnvelope jsonEnv = new Gson().fromJson(new InputStreamReader(is), JSONMagicEnvelope.class);
		
		//TODO: validate required elements are present
		
		MagicEnvelope me = new MagicEnvelope()
								.withData(jsonEnv.getData())
								.withDataType(jsonEnv.getData_type())
								.withEncoding(jsonEnv.getEncoding())
								.withAlgorithm(jsonEnv.getAlg());
		for (JSONMagicEnvelopeSignature s : jsonEnv.getSigs()) {
			me.withSignature(new Signature().withKeyId(s.getKey_id()).withValue(s.getValue()));
		}
		return me;
	}
}
