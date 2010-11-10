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
