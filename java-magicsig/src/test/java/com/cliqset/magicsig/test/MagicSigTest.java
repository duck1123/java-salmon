package com.cliqset.magicsig.test;

import org.junit.Assert;
import org.junit.Test;

import com.cliqset.hostmeta.HostMeta;
import com.cliqset.hostmeta.JavaNetXRDFetcher;
import com.cliqset.hostmeta.XRDFetcher;
import com.cliqset.hostmeta.template.LRDDTemplateProcessor;
import com.cliqset.hostmeta.template.TemplateProcessor;
import com.cliqset.magicsig.DataParser;
import com.cliqset.magicsig.KeyFinder;
import com.cliqset.magicsig.MagicEnvelope;
import com.cliqset.magicsig.MagicEnvelopeSerializationProvider;
import com.cliqset.magicsig.MagicSig;
import com.cliqset.magicsig.MagicSigAlgorithm;
import com.cliqset.magicsig.MagicSigConstants;
import com.cliqset.magicsig.MagicSigEncoding;
import com.cliqset.magicsig.MagicSigException;
import com.cliqset.magicsig.PayloadToMetadataMapper;
import com.cliqset.magicsig.URIPayloadToMetadataMapper;
import com.cliqset.magicsig.algorithm.HMACSHA256MagicSigAlgorithm;
import com.cliqset.magicsig.algorithm.RSASHA256MagicSigAlgorithm;
import com.cliqset.magicsig.dataparser.SimpleAtomDataParser;
import com.cliqset.magicsig.encoding.Base64URLMagicSigEncoding;
import com.cliqset.magicsig.keyfinder.MagicPKIKeyFinder;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;

public class MagicSigTest {

 	
	@Test
	public void testEmptyDataParsers() {
		MagicSig s = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(HostMeta.class);
				bind(XRDFetcher.class).to(JavaNetXRDFetcher.class);
				
				MapBinder<String, MagicSigAlgorithm> algorithmBinder = MapBinder.newMapBinder(binder(), String.class, MagicSigAlgorithm.class);
			    algorithmBinder.addBinding(HMACSHA256MagicSigAlgorithm.ALGORITHM_IDENTIFIER).to(HMACSHA256MagicSigAlgorithm.class);
			    algorithmBinder.addBinding(RSASHA256MagicSigAlgorithm.ALGORITHM_IDENTIFIER).to(RSASHA256MagicSigAlgorithm.class);
			    
			    MapBinder<String, MagicSigEncoding> encodingBinder = MapBinder.newMapBinder(binder(), String.class, MagicSigEncoding.class);
			    encodingBinder.addBinding(Base64URLMagicSigEncoding.ENCODING_IDENTIFIER).to(Base64URLMagicSigEncoding.class);
			    
			    Multibinder<KeyFinder> keyFinderBinder = Multibinder.newSetBinder(binder(), KeyFinder.class);
			    keyFinderBinder.addBinding().to(MagicPKIKeyFinder.class);
			    
			    @SuppressWarnings("unused")
				MapBinder<String, DataParser> dataParserBinder = MapBinder.newMapBinder(binder(), String.class, DataParser.class);
//			    dataParserBinder.addBinding("application/atom+xml").to(SimpleAtomDataParser.class);

				bind(PayloadToMetadataMapper.class).to(URIPayloadToMetadataMapper.class);
				
				MapBinder<String, TemplateProcessor> templateBinder = MapBinder.newMapBinder(binder(), String.class, TemplateProcessor.class);
				templateBinder.addBinding(LRDDTemplateProcessor.REL).to(LRDDTemplateProcessor.class);
			}
		}).getInstance(MagicSig.class);
			
		try {
			MagicEnvelope env = MagicEnvelopeSerializationProvider.getDefault().getDeserializer(MagicSigConstants.MEDIA_TYPE_MAGIC_ENV_XML).deserialize(MagicSigTest.class.getResourceAsStream("/MagicEnvelopeOne.xml"));
			s.verify(env);
			Assert.fail("Should get a MagicSigException.");
		} catch (MagicSigException mse) {
			Assert.assertEquals("No parser for mediaType: application/atom+xml", mse.getMessage());
		} catch (Exception e) {
			Assert.fail("Should get a MagicSigException");
		}
	}
	
	@Test
	public void testNoMatchingDataParser() {
		MagicSig s = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(HostMeta.class);
				bind(XRDFetcher.class).to(JavaNetXRDFetcher.class);
				
				MapBinder<String, MagicSigAlgorithm> algorithmBinder = MapBinder.newMapBinder(binder(), String.class, MagicSigAlgorithm.class);
			    algorithmBinder.addBinding(HMACSHA256MagicSigAlgorithm.ALGORITHM_IDENTIFIER).to(HMACSHA256MagicSigAlgorithm.class);
			    algorithmBinder.addBinding(RSASHA256MagicSigAlgorithm.ALGORITHM_IDENTIFIER).to(RSASHA256MagicSigAlgorithm.class);
			    
			    MapBinder<String, MagicSigEncoding> encodingBinder = MapBinder.newMapBinder(binder(), String.class, MagicSigEncoding.class);
			    encodingBinder.addBinding(Base64URLMagicSigEncoding.ENCODING_IDENTIFIER).to(Base64URLMagicSigEncoding.class);
			    
			    Multibinder<KeyFinder> keyFinderBinder = Multibinder.newSetBinder(binder(), KeyFinder.class);
			    keyFinderBinder.addBinding().to(MagicPKIKeyFinder.class);
			    
			    MapBinder<String, DataParser> dataParserBinder = MapBinder.newMapBinder(binder(), String.class, DataParser.class);
			    dataParserBinder.addBinding("application/some_other_type").to(SimpleAtomDataParser.class);

				bind(PayloadToMetadataMapper.class).to(URIPayloadToMetadataMapper.class);
				
				MapBinder<String, TemplateProcessor> templateBinder = MapBinder.newMapBinder(binder(), String.class, TemplateProcessor.class);
				templateBinder.addBinding(LRDDTemplateProcessor.REL).to(LRDDTemplateProcessor.class);
				
			}
		}).getInstance(MagicSig.class);
		
		try {
			MagicEnvelope env = MagicEnvelopeSerializationProvider.getDefault().getDeserializer(MagicSigConstants.MEDIA_TYPE_MAGIC_ENV_XML).deserialize(MagicSigTest.class.getResourceAsStream("/MagicEnvelopeOne.xml"));
			s.verify(env);
			Assert.fail("Should get a MagicSigException.");
		} catch (MagicSigException mse) {
			Assert.assertEquals("No parser for mediaType: application/atom+xml", mse.getMessage());
		} catch (Exception e) {
			Assert.fail("Should get a MagicSigException");
		}
	}
	
	@Test
	public void testEmptyKeyFinder() {
		MagicSig s = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(HostMeta.class);
				bind(XRDFetcher.class).to(JavaNetXRDFetcher.class);
				
				MapBinder<String, MagicSigAlgorithm> algorithmBinder = MapBinder.newMapBinder(binder(), String.class, MagicSigAlgorithm.class);
			    algorithmBinder.addBinding(HMACSHA256MagicSigAlgorithm.ALGORITHM_IDENTIFIER).to(HMACSHA256MagicSigAlgorithm.class);
			    algorithmBinder.addBinding(RSASHA256MagicSigAlgorithm.ALGORITHM_IDENTIFIER).to(RSASHA256MagicSigAlgorithm.class);
			    
			    MapBinder<String, MagicSigEncoding> encodingBinder = MapBinder.newMapBinder(binder(), String.class, MagicSigEncoding.class);
			    encodingBinder.addBinding(Base64URLMagicSigEncoding.ENCODING_IDENTIFIER).to(Base64URLMagicSigEncoding.class);
			    
			    @SuppressWarnings("unused")
				Multibinder<KeyFinder> keyFinderBinder = Multibinder.newSetBinder(binder(), KeyFinder.class);
//			    keyFinderBinder.addBinding().to(MagicPKIKeyFinder.class);
			    
			    MapBinder<String, DataParser> dataParserBinder = MapBinder.newMapBinder(binder(), String.class, DataParser.class);
			    dataParserBinder.addBinding("application/atom+xml").to(SimpleAtomDataParser.class);

				bind(PayloadToMetadataMapper.class).to(URIPayloadToMetadataMapper.class);
				
				MapBinder<String, TemplateProcessor> templateBinder = MapBinder.newMapBinder(binder(), String.class, TemplateProcessor.class);
				templateBinder.addBinding(LRDDTemplateProcessor.REL).to(LRDDTemplateProcessor.class);
				
			}
		}).getInstance(MagicSig.class);
		
		try {
			MagicEnvelope env = MagicEnvelopeSerializationProvider.getDefault().getDeserializer(MagicSigConstants.MEDIA_TYPE_MAGIC_ENV_XML).deserialize(MagicSigTest.class.getResourceAsStream("/MagicEnvelopeOne.xml"));
			s.verify(env);
			Assert.fail("Should get a MagicSigException.");
		} catch (MagicSigException se) {
			Assert.assertEquals("Unable to find keys for signer: acct:test@example.com", se.getMessage());
		} catch (Exception e) {
			Assert.fail("Should get a MagicSigException");
		}
	}

	public void testNoMatchingAlgorithm() {
		MagicSig s = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(HostMeta.class);
				bind(XRDFetcher.class).to(JavaNetXRDFetcher.class);
				
				MapBinder<String, MagicSigAlgorithm> algorithmBinder = MapBinder.newMapBinder(binder(), String.class, MagicSigAlgorithm.class);
			    algorithmBinder.addBinding("NOT_MATCHING_ALG_IDENTIFIER").to(HMACSHA256MagicSigAlgorithm.class);
			    
			    MapBinder<String, MagicSigEncoding> encodingBinder = MapBinder.newMapBinder(binder(), String.class, MagicSigEncoding.class);
			    encodingBinder.addBinding(Base64URLMagicSigEncoding.ENCODING_IDENTIFIER).to(Base64URLMagicSigEncoding.class);
			    
			    Multibinder<KeyFinder> keyFinderBinder = Multibinder.newSetBinder(binder(), KeyFinder.class);
			    keyFinderBinder.addBinding().to(MagicPKIKeyFinder.class);
			    
				MapBinder<String, DataParser> dataParserBinder = MapBinder.newMapBinder(binder(), String.class, DataParser.class);
			    dataParserBinder.addBinding("application/atom+xml").to(SimpleAtomDataParser.class);

				bind(PayloadToMetadataMapper.class).to(URIPayloadToMetadataMapper.class);
				
				MapBinder<String, TemplateProcessor> templateBinder = MapBinder.newMapBinder(binder(), String.class, TemplateProcessor.class);
				templateBinder.addBinding(LRDDTemplateProcessor.REL).to(LRDDTemplateProcessor.class);
			}
		}).getInstance(MagicSig.class);
			
		try {
			MagicEnvelope env = MagicEnvelopeSerializationProvider.getDefault().getDeserializer(MagicSigConstants.MEDIA_TYPE_MAGIC_ENV_XML).deserialize(MagicSigTest.class.getResourceAsStream("/MagicEnvelopeOne.xml"));
			s.verify(env);
			Assert.fail("Should get a MagicSigException.");
		} catch (MagicSigException mse) {
			Assert.assertEquals("Unrecognized algorithm:RSA-SHA256", mse.getMessage());
		} catch (Exception e) {
			Assert.fail("Should get a MagicSigException");
		}
	}
	
	public void testNoMatchingEncoding() {
		MagicSig s = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(HostMeta.class);
				bind(XRDFetcher.class).to(JavaNetXRDFetcher.class);
				
				MapBinder<String, MagicSigAlgorithm> algorithmBinder = MapBinder.newMapBinder(binder(), String.class, MagicSigAlgorithm.class);
			    algorithmBinder.addBinding(HMACSHA256MagicSigAlgorithm.ALGORITHM_IDENTIFIER).to(HMACSHA256MagicSigAlgorithm.class);
			    algorithmBinder.addBinding(RSASHA256MagicSigAlgorithm.ALGORITHM_IDENTIFIER).to(RSASHA256MagicSigAlgorithm.class);
			    
			    MapBinder<String, MagicSigEncoding> encodingBinder = MapBinder.newMapBinder(binder(), String.class, MagicSigEncoding.class);
			    encodingBinder.addBinding("NOT_MATCHING_ENCODING_IDENTIFIER").to(Base64URLMagicSigEncoding.class);
			    
			    Multibinder<KeyFinder> keyFinderBinder = Multibinder.newSetBinder(binder(), KeyFinder.class);
			    keyFinderBinder.addBinding().to(MagicPKIKeyFinder.class);
			    
				MapBinder<String, DataParser> dataParserBinder = MapBinder.newMapBinder(binder(), String.class, DataParser.class);
			    dataParserBinder.addBinding("application/atom+xml").to(SimpleAtomDataParser.class);

				bind(PayloadToMetadataMapper.class).to(URIPayloadToMetadataMapper.class);
				
				MapBinder<String, TemplateProcessor> templateBinder = MapBinder.newMapBinder(binder(), String.class, TemplateProcessor.class);
				templateBinder.addBinding(LRDDTemplateProcessor.REL).to(LRDDTemplateProcessor.class);
			}
		}).getInstance(MagicSig.class);
			
		try {
			MagicEnvelope env = MagicEnvelopeSerializationProvider.getDefault().getDeserializer(MagicSigConstants.MEDIA_TYPE_MAGIC_ENV_XML).deserialize(MagicSigTest.class.getResourceAsStream("/MagicEnvelopeOne.xml"));
			s.verify(env);
			Assert.fail("Should get a MagicSigException.");
		} catch (MagicSigException mse) {
			Assert.assertEquals("Unrecognized encoding:base64url", mse.getMessage());
		} catch (Exception e) {
			Assert.fail("Should get a MagicSigException");
		}
	}
}
