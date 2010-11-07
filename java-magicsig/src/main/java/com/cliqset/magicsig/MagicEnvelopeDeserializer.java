package com.cliqset.magicsig;

import java.io.InputStream;
import java.util.List;

public interface MagicEnvelopeDeserializer {

	List<String> getSupportedMediaTypes();
	
	MagicEnvelope deserialize(InputStream is) throws MagicSigException;
}
