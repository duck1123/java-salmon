package com.cliqset.magicsig.test;

import org.junit.Assert;
import org.junit.Test;

import com.cliqset.magicsig.MagicEnvelopeDeserializer;
import com.cliqset.magicsig.MagicEnvelopeSerializationProvider;
import com.cliqset.magicsig.MagicEnvelopeSerializer;
import com.cliqset.magicsig.MagicSigException;
import com.cliqset.magicsig.compact.CompactMagicEnvelopeDeserializer;
import com.cliqset.magicsig.compact.CompactMagicEnvelopeSerializer;
import com.cliqset.magicsig.json.JSONMagicEnvelopeDeserializer;
import com.cliqset.magicsig.json.JSONMagicEnvelopeSerializer;
import com.cliqset.magicsig.xml.XMLMagicEnvelopeDeserializer;
import com.cliqset.magicsig.xml.XMLMagicEnvelopeSerializer;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.multibindings.MapBinder;

public class MagicEnvelopeSerializationProviderTest {

	@Test
	public void testGetXMLDeserializer() {
		MagicEnvelopeSerializationProvider p = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
			    MapBinder<String, MagicEnvelopeSerializer> serializerBinder = MapBinder.newMapBinder(binder(), String.class, MagicEnvelopeSerializer.class);
			    serializerBinder.addBinding(XMLMagicEnvelopeSerializer.MEDIA_TYPE).to(XMLMagicEnvelopeSerializer.class);

			    MapBinder<String, MagicEnvelopeDeserializer> deserializerBinder = MapBinder.newMapBinder(binder(), String.class, MagicEnvelopeDeserializer.class);
			    deserializerBinder.addBinding(XMLMagicEnvelopeDeserializer.MEDIA_TYPE).to(XMLMagicEnvelopeDeserializer.class);
			}
			
		}).getInstance(MagicEnvelopeSerializationProvider.class);
		try {
			Assert.assertEquals(XMLMagicEnvelopeDeserializer.class, p.getDeserializer("application/magic-env+xml").getClass());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetXMLSerializer() {
		MagicEnvelopeSerializationProvider p = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
			    MapBinder<String, MagicEnvelopeSerializer> serializerBinder = MapBinder.newMapBinder(binder(), String.class, MagicEnvelopeSerializer.class);
			    serializerBinder.addBinding(XMLMagicEnvelopeSerializer.MEDIA_TYPE).to(XMLMagicEnvelopeSerializer.class);

			    MapBinder<String, MagicEnvelopeDeserializer> deserializerBinder = MapBinder.newMapBinder(binder(), String.class, MagicEnvelopeDeserializer.class);
			    deserializerBinder.addBinding(XMLMagicEnvelopeDeserializer.MEDIA_TYPE).to(XMLMagicEnvelopeDeserializer.class);
			}
			
		}).getInstance(MagicEnvelopeSerializationProvider.class);
		
		try {
			Assert.assertEquals(XMLMagicEnvelopeSerializer.class, p.getSerializer("application/magic-env+xml").getClass());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}		
	}
	
	@Test
	public void testGetMissingDeserializer() {
		MagicEnvelopeSerializationProvider p = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
			    MapBinder<String, MagicEnvelopeSerializer> serializerBinder = MapBinder.newMapBinder(binder(), String.class, MagicEnvelopeSerializer.class);
			    serializerBinder.addBinding(XMLMagicEnvelopeSerializer.MEDIA_TYPE).to(XMLMagicEnvelopeSerializer.class);

			    MapBinder<String, MagicEnvelopeDeserializer> deserializerBinder = MapBinder.newMapBinder(binder(), String.class, MagicEnvelopeDeserializer.class);
			    deserializerBinder.addBinding(XMLMagicEnvelopeDeserializer.MEDIA_TYPE).to(XMLMagicEnvelopeDeserializer.class);
			}
			
		}).getInstance(MagicEnvelopeSerializationProvider.class);
		
		try {
			p.getDeserializer("application/some-other-media-type");
			Assert.fail("Expecting a MagicSigException.");
		} catch (MagicSigException mse) {
			Assert.assertEquals("No known deserializer for media-type application/some-other-media-type", mse.getMessage());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetMissingSerializer() {
		MagicEnvelopeSerializationProvider p = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
			    MapBinder<String, MagicEnvelopeSerializer> serializerBinder = MapBinder.newMapBinder(binder(), String.class, MagicEnvelopeSerializer.class);
			    serializerBinder.addBinding(XMLMagicEnvelopeSerializer.MEDIA_TYPE).to(XMLMagicEnvelopeSerializer.class);

			    MapBinder<String, MagicEnvelopeDeserializer> deserializerBinder = MapBinder.newMapBinder(binder(), String.class, MagicEnvelopeDeserializer.class);
			    deserializerBinder.addBinding(XMLMagicEnvelopeDeserializer.MEDIA_TYPE).to(XMLMagicEnvelopeDeserializer.class);
			}
			
		}).getInstance(MagicEnvelopeSerializationProvider.class);
		
		try {
			p.getSerializer("application/some-other-media-type");
			Assert.fail("Expecting a MagicSigException.");
		} catch (MagicSigException mse) {
			Assert.assertEquals("No known serializer for media-type application/some-other-media-type", mse.getMessage());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetNullKeyDeserializer() {
		MagicEnvelopeSerializationProvider p = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
			    MapBinder<String, MagicEnvelopeSerializer> serializerBinder = MapBinder.newMapBinder(binder(), String.class, MagicEnvelopeSerializer.class);
			    serializerBinder.addBinding(XMLMagicEnvelopeSerializer.MEDIA_TYPE).to(XMLMagicEnvelopeSerializer.class);

			    MapBinder<String, MagicEnvelopeDeserializer> deserializerBinder = MapBinder.newMapBinder(binder(), String.class, MagicEnvelopeDeserializer.class);
			    deserializerBinder.addBinding(XMLMagicEnvelopeDeserializer.MEDIA_TYPE).to(XMLMagicEnvelopeDeserializer.class);
			}
			
		}).getInstance(MagicEnvelopeSerializationProvider.class);
		
		try {
			p.getDeserializer(null);
			Assert.fail("Expecting an IllegalArgumentException.");
		} catch (IllegalArgumentException iae) {
			Assert.assertEquals("Cannot retrieve MagicEnvelopeDeserializer with a null media-type", iae.getMessage());
		} catch (Exception e) {
			Assert.fail("Expecting an IllegalArgumentException not an " + e.getClass().getName());
		}		
	}
	
	@Test
	public void testGetNullKeySerializer() {
		MagicEnvelopeSerializationProvider p = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
			    MapBinder<String, MagicEnvelopeSerializer> serializerBinder = MapBinder.newMapBinder(binder(), String.class, MagicEnvelopeSerializer.class);
			    serializerBinder.addBinding(XMLMagicEnvelopeSerializer.MEDIA_TYPE).to(XMLMagicEnvelopeSerializer.class);

			    MapBinder<String, MagicEnvelopeDeserializer> deserializerBinder = MapBinder.newMapBinder(binder(), String.class, MagicEnvelopeDeserializer.class);
			    deserializerBinder.addBinding(XMLMagicEnvelopeDeserializer.MEDIA_TYPE).to(XMLMagicEnvelopeDeserializer.class);
			}
			
		}).getInstance(MagicEnvelopeSerializationProvider.class);
		
		try {
			p.getSerializer(null);
			Assert.fail("Expecting an IllegalArgumentException.");
		} catch (IllegalArgumentException iae) {
			Assert.assertEquals("Cannot retrieve MagicEnvelopeSerializer with a null media-type", iae.getMessage());
		} catch (Exception e) {
			Assert.fail("Expecting an IllegalArgumentException not an " + e.getClass().getName());
		}
	}
	
	@Test
	public void testGetDefault() {
		MagicEnvelopeSerializationProvider p = MagicEnvelopeSerializationProvider.getDefault();
		try {
			Assert.assertEquals(XMLMagicEnvelopeSerializer.class, p.getSerializer("application/magic-env+xml").getClass());
			Assert.assertEquals(JSONMagicEnvelopeSerializer.class, p.getSerializer("application/magic-env+json").getClass());
			Assert.assertEquals(CompactMagicEnvelopeSerializer.class, p.getSerializer("application/magic-env").getClass());
			
			Assert.assertEquals(XMLMagicEnvelopeDeserializer.class, p.getDeserializer("application/magic-env+xml").getClass());
			Assert.assertEquals(JSONMagicEnvelopeDeserializer.class, p.getDeserializer("application/magic-env+json").getClass());
			Assert.assertEquals(CompactMagicEnvelopeDeserializer.class, p.getDeserializer("application/magic-env").getClass());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}	
	}
}
