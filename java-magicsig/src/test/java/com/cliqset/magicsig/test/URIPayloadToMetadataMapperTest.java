package com.cliqset.magicsig.test;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.cliqset.magicsig.DataParser;
import com.cliqset.magicsig.Key;
import com.cliqset.magicsig.KeyFinder;
import com.cliqset.magicsig.MagicSigException;
import com.cliqset.magicsig.URIPayloadToMetadataMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;

public class URIPayloadToMetadataMapperTest {

	@Test
	public void testMissingDataParser() {
		try {
			URIPayloadToMetadataMapper mapper = Guice.createInjector(new AbstractModule() {
	
				@Override
				protected void configure() {
					
					Multibinder<KeyFinder> keyFinderBinder = Multibinder.newSetBinder(binder(), KeyFinder.class);
					keyFinderBinder.addBinding().to(SuccessfullKeyFinder.class);
					
					MapBinder<String, DataParser> dataParserBinder = MapBinder.newMapBinder(binder(), String.class, DataParser.class);
					dataParserBinder.addBinding("test-media-type").to(TestMediaTypeDataParser.class);
				}
			}).getInstance(URIPayloadToMetadataMapper.class);
			mapper.getKeys("no-match-media-type", "acct:test@signer.com".getBytes("UTF-8"));
			Assert.fail("Expected a MagicSigException");
		} catch (MagicSigException mse) {
			Assert.assertEquals("No parser for mediaType: no-match-media-type", mse.getMessage());
		} catch (Exception e) {
			Assert.fail("Expected a MagicSigException not a " + e.getClass().getName());
		}
	}
	
	@Test
	public void testNoKeyfinders() {
		try {
			URIPayloadToMetadataMapper mapper = Guice.createInjector(new AbstractModule() {
				
				@Override
				protected void configure() {
					
					@SuppressWarnings("unused")
					Multibinder<KeyFinder> keyFinderBinder = Multibinder.newSetBinder(binder(), KeyFinder.class);
					
					MapBinder<String, DataParser> dataParserBinder = MapBinder.newMapBinder(binder(), String.class, DataParser.class);
					dataParserBinder.addBinding("test-media-type").to(TestMediaTypeDataParser.class);
				}
			}).getInstance(URIPayloadToMetadataMapper.class);
			mapper.getKeys("test-media-type", "acct:test@signer.com".getBytes("UTF-8"));
			Assert.fail("Expecting a MagicSigException");
		} catch (MagicSigException mse) {
			Assert.assertEquals("Unable to find keys for signer: acct:test@signer.com", mse.getMessage());
		} catch (Exception e) {
			Assert.fail("Expected MagicSigException not " + e.getClass().getName());
		}
	}
	
	@Test
	public void testSuccessfulMapping() {
		try {
			URIPayloadToMetadataMapper mapper = Guice.createInjector(new AbstractModule() {
	
				@Override
				protected void configure() {
					
					Multibinder<KeyFinder> keyFinderBinder = Multibinder.newSetBinder(binder(), KeyFinder.class);
					keyFinderBinder.addBinding().to(SuccessfullKeyFinder.class);
					
					MapBinder<String, DataParser> dataParserBinder = MapBinder.newMapBinder(binder(), String.class, DataParser.class);
					dataParserBinder.addBinding("test-media-type").to(TestMediaTypeDataParser.class);
				}
			}).getInstance(URIPayloadToMetadataMapper.class);
			List<Key> keys = mapper.getKeys("test-media-type", "acct:test@signer.com".getBytes("UTF-8"));
			Assert.assertEquals(1, keys.size());
		} catch (Exception e) {
			Assert.fail("Unexpected exception:" + e.getMessage());
		}
	}
	
	@Test
	public void testNoKeysFound() {
		try {
			URIPayloadToMetadataMapper mapper = Guice.createInjector(new AbstractModule() {
	
				@Override
				protected void configure() {
					
					Multibinder<KeyFinder> keyFinderBinder = Multibinder.newSetBinder(binder(), KeyFinder.class);
					keyFinderBinder.addBinding().to(ReturnsNullKeyFinder.class);
					
					MapBinder<String, DataParser> dataParserBinder = MapBinder.newMapBinder(binder(), String.class, DataParser.class);
					dataParserBinder.addBinding("test-media-type").to(TestMediaTypeDataParser.class);
				}
			}).getInstance(URIPayloadToMetadataMapper.class);
			mapper.getKeys("test-media-type", "acct:test@signer.com".getBytes("UTF-8"));
			Assert.fail("Expecting a MagicSigException");
		} catch (MagicSigException mse) {
			Assert.assertEquals("Unable to find keys for signer: acct:test@signer.com", mse.getMessage());
		} catch (Exception e) {
			Assert.fail("Expected MagicSigException not " + e.getClass().getName());
		}
	}
	
	@Test
	public void testNullSignerURI() {
		try {
			URIPayloadToMetadataMapper mapper = Guice.createInjector(new AbstractModule() {
	
				@Override
				protected void configure() {
					
					Multibinder<KeyFinder> keyFinderBinder = Multibinder.newSetBinder(binder(), KeyFinder.class);
					keyFinderBinder.addBinding().to(ExceptionKeyFinder.class);
					
					MapBinder<String, DataParser> dataParserBinder = MapBinder.newMapBinder(binder(), String.class, DataParser.class);
					dataParserBinder.addBinding("test-media-type").to(NullSignerDataParser.class);
				}
			}).getInstance(URIPayloadToMetadataMapper.class);
			mapper.getKeys("test-media-type", "acct:test@signer.com".getBytes("UTF-8"));
			Assert.fail("Expected a MagicSigException");
		} catch (MagicSigException mse) {
			Assert.assertEquals("Unable extract signer's URI from data of type test-media-type, data parser com.cliqset.magicsig.test.URIPayloadToMetadataMapperTest$NullSignerDataParser returned null signerUri.", mse.getMessage());
		} catch (Exception e) {
			Assert.fail("Expected a MagicSigException not a " + e.getClass().getName());
		}
	}

	public static class SuccessfullKeyFinder implements KeyFinder {
		
		public List<Key> findKeys(URI signerUri) throws MagicSigException {
			try {
				if (new URI("acct:test@signer.com").equals(signerUri)) {
					return Arrays.asList(new Key[] { new Key() {} });
				} else {
					return null;
				}
			} catch (Exception e) {
				throw new MagicSigException("Unexpected Exception", e);
			}
		}
	}
	
	public static class ReturnsNullKeyFinder implements KeyFinder {
		public List<Key> findKeys(URI signerUri) throws MagicSigException {
			return null;
		}
	}
	
	public static class ExceptionKeyFinder implements KeyFinder {
		public List<Key> findKeys(URI signerUri) throws MagicSigException {
			throw new MagicSigException("Should not reach KeyFinder if the signer URI is null.");
		}
	}
	
	public static class TestMediaTypeDataParser implements DataParser {
		public URI getSignerUri(byte[] data) throws MagicSigException {
			try {
				return new URI(new String(data, "UTF-8"));
			} catch (Exception e) {
				throw new MagicSigException("Unexpected Exception", e);
			}
		}
	}
	
	public static class NullSignerDataParser implements DataParser {
		public URI getSignerUri(byte[] data) throws MagicSigException {
			return null;
		}
	}
}
