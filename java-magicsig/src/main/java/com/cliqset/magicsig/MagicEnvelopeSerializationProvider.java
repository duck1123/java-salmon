package com.cliqset.magicsig;

import java.util.Map;

import com.cliqset.magicsig.guice.DefaultMagicEnvelopeSerializationModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class MagicEnvelopeSerializationProvider {

	private Map<String, MagicEnvelopeDeserializer> deserializers;
	private Map<String, MagicEnvelopeSerializer> serializers;
	
	@Inject
	public MagicEnvelopeSerializationProvider (
			Map<String, MagicEnvelopeSerializer> serializers, 
			Map<String, MagicEnvelopeDeserializer> deserializers) {
		
		this.serializers = serializers;
		this.deserializers = deserializers;
	}
	
	public MagicEnvelopeDeserializer getDeserializer(String mediaType) throws MagicSigException {
		MagicEnvelopeDeserializer d = this.deserializers.get(mediaType);
		if (null == d) {
			throw new MagicSigException("No known deserializer for media-type " + mediaType);
		}
		return d;
	}
	
	public MagicEnvelopeSerializer getSerializer(String mediaType) throws MagicSigException {
		MagicEnvelopeSerializer s = this.serializers.get(mediaType);
		if (null == s) {
			throw new MagicSigException("No known serializer for media-type " + mediaType);
		}
		return s;
	}
	
	public static MagicEnvelopeSerializationProvider getDefault() {
		Injector i = Guice.createInjector(new DefaultMagicEnvelopeSerializationModule());
		return i.getInstance(MagicEnvelopeSerializationProvider.class);
	}
}
