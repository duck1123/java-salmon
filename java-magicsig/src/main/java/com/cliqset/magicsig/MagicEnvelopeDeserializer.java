package com.cliqset.magicsig;

import java.io.InputStream;

public interface MagicEnvelopeDeserializer {

	MagicEnvelope deserialize(InputStream is) throws MagicSigException;
}
