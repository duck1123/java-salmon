package com.cliqset.magicsig.guice;

import com.cliqset.magicsig.MagicEnvelopeDeserializer;
import com.cliqset.magicsig.MagicEnvelopeSerializer;
import com.cliqset.magicsig.compact.CompactMagicEnvelopeDeserializer;
import com.cliqset.magicsig.compact.CompactMagicEnvelopeSerializer;
import com.cliqset.magicsig.json.JSONMagicEnvelopeDeserializer;
import com.cliqset.magicsig.json.JSONMagicEnvelopeSerializer;
import com.cliqset.magicsig.xml.XMLMagicEnvelopeDeserializer;
import com.cliqset.magicsig.xml.XMLMagicEnvelopeSerializer;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;

public class DefaultMagicEnvelopeSerializationModule extends AbstractModule {

	@Override
	protected void configure() {
	    MapBinder<String, MagicEnvelopeSerializer> serializerBinder = MapBinder.newMapBinder(binder(), String.class, MagicEnvelopeSerializer.class);
	    serializerBinder.addBinding(XMLMagicEnvelopeSerializer.MEDIA_TYPE).to(XMLMagicEnvelopeSerializer.class);
	    serializerBinder.addBinding(JSONMagicEnvelopeSerializer.MEDIA_TYPE).to(JSONMagicEnvelopeSerializer.class);
	    serializerBinder.addBinding(CompactMagicEnvelopeSerializer.MEDIA_TYPE).to(CompactMagicEnvelopeSerializer.class);

	    MapBinder<String, MagicEnvelopeDeserializer> deserializerBinder = MapBinder.newMapBinder(binder(), String.class, MagicEnvelopeDeserializer.class);
	    deserializerBinder.addBinding(XMLMagicEnvelopeDeserializer.MEDIA_TYPE).to(XMLMagicEnvelopeDeserializer.class);
	    deserializerBinder.addBinding(JSONMagicEnvelopeDeserializer.MEDIA_TYPE).to(JSONMagicEnvelopeDeserializer.class);
	    deserializerBinder.addBinding(CompactMagicEnvelopeDeserializer.MEDIA_TYPE).to(CompactMagicEnvelopeDeserializer.class);
	}
}
