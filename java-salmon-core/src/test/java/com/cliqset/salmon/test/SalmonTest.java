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

package com.cliqset.salmon.test;

import java.net.URI;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.Assert;

import com.cliqset.hostmeta.JavaNetXRDFetcher;
import com.cliqset.hostmeta.XRDFetcher;
import com.cliqset.hostmeta.template.LRDDTemplateProcessor;
import com.cliqset.hostmeta.template.TemplateProcessor;
import com.cliqset.magicsig.DataParser;
import com.cliqset.magicsig.Key;
import com.cliqset.magicsig.KeyFinder;
import com.cliqset.magicsig.PayloadToMetadataMapper;
import com.cliqset.magicsig.MagicEnvelope;
import com.cliqset.magicsig.MagicKey;
import com.cliqset.magicsig.MagicSigConstants;
import com.cliqset.magicsig.MagicSigException;
import com.cliqset.magicsig.MagicSig;
import com.cliqset.magicsig.algorithm.HMACSHA256MagicSigAlgorithm;
import com.cliqset.magicsig.algorithm.RSASHA256MagicSigAlgorithm;
import com.cliqset.magicsig.encoding.Base64URLMagicSigEncoding;
import com.cliqset.magicsig.xml.XMLMagicEnvelopeDeserializer;
import com.cliqset.salmon.HostMetaSalmonEndpointFinder;
import com.cliqset.salmon.JavaNetSalmonSender;
import com.cliqset.salmon.Salmon;
import com.cliqset.salmon.SalmonEndpointFinder;
import com.cliqset.salmon.SalmonSender;
import com.cliqset.magicsig.MagicEnvelopeSerializationProvider;
import com.cliqset.magicsig.MagicSigAlgorithm;
import com.cliqset.magicsig.MagicSigEncoding;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.multibindings.MapBinder;

public class SalmonTest extends BaseTestCase {
	/*
	@Test
	public void testSignBasic() {
		try {
			Salmon s = new Salmon()
				.withDataParser(new BasicAtomDataParser())
				.withKeyFinder(new BasicKeyFinder());
			MagicEnvelope env = s.sign(getBytes("/BasicAtom.txt"), new MagicKey(getBytes("/BasicRSAKey.txt")));
			Assert.assertEquals(env.getAlg(), "RSA-SHA256");
			Assert.assertEquals(env.getData().getValue(), "PD94bWwgdmVyc2lvbj0nMS4wJyBlbmNvZGluZz0nVVRGLTgnPz4KPGVudHJ5IHhtbG5zPSdodHRwOi8vd3d3LnczLm9yZy8yMDA1L0F0b20nPgoJPGlkPnRhZzpleGFtcGxlLmNvbSwyMDA5OmNtdC0wLjQ0Nzc1NzE4PC9pZD4KCTxhdXRob3I-CgkJPG5hbWU-dGVzdEBleGFtcGxlLmNvbTwvbmFtZT4KCQk8dXJpPmFjY3Q6dGVzdEBleGFtcGxlLmNvbTwvdXJpPgoJPC9hdXRob3I-Cgk8Y29udGVudD5TYWxtb24gc3dpbSB1cHN0cmVhbSE8L2NvbnRlbnQ-Cgk8dGl0bGU-U2FsbW9uIHN3aW0gdXBzdHJlYW0hPC90aXRsZT4KCTx1cGRhdGVkPjIwMDktMTItMThUMjA6MDQ6MDNaPC91cGRhdGVkPgo8L2VudHJ5Pg==");
			Assert.assertEquals(env.getData().getType(), "application/atom+xml");
			Assert.assertEquals(env.getEncoding(), "base64url");
			//Assert.assertEquals(env.getSigs().get(0).getKeyId(), "XV4QrdLUj8SyFr-hhobmeKlOTSTg6Rd4sbQXHnx4ejg=");
			Assert.assertEquals(env.getSigs().get(0).getValue(), "ARx-SOqs9geUJKhqgGOZ-KUE7Qe_v7w-bPrI4lPwXW95SFuvaQwtB-lhfiXltYS4PvrAEl7wXDDmd1nCR4YMag==");
			
		} catch (Exception e){
			Assert.fail(e.getMessage());
		}
	}
	*/
	@Test
	public void testVerifyBasic() {
		try {
			Injector i = Guice.createInjector(new AbstractModule() {

				@Override
				protected void configure() {
					bind(MagicSig.class);
					bind(SalmonSender.class).to(JavaNetSalmonSender.class);
					bind(SalmonEndpointFinder.class).to(HostMetaSalmonEndpointFinder.class);
					bind(XRDFetcher.class).to(JavaNetXRDFetcher.class);
					MapBinder<String, TemplateProcessor> templateBinder = MapBinder.newMapBinder(binder(), String.class, TemplateProcessor.class);
					templateBinder.addBinding(LRDDTemplateProcessor.REL).to(LRDDTemplateProcessor.class);
					
					//MagicSig dependencies
					MapBinder<String, MagicSigAlgorithm> algorithmBinder = MapBinder.newMapBinder(binder(), String.class, MagicSigAlgorithm.class);
				    algorithmBinder.addBinding(HMACSHA256MagicSigAlgorithm.ALGORITHM_IDENTIFIER).to(HMACSHA256MagicSigAlgorithm.class);
				    algorithmBinder.addBinding(RSASHA256MagicSigAlgorithm.ALGORITHM_IDENTIFIER).to(RSASHA256MagicSigAlgorithm.class);
				    
				    MapBinder<String, MagicSigEncoding> encodingBinder = MapBinder.newMapBinder(binder(), String.class, MagicSigEncoding.class);
				    encodingBinder.addBinding(Base64URLMagicSigEncoding.ENCODING_IDENTIFIER).to(Base64URLMagicSigEncoding.class);

					bind(PayloadToMetadataMapper.class).to(BasicPayloadToMetadataMapper.class);	
				}
				
				@SuppressWarnings("unused")
				@Provides
				ExecutorService provideExecutorService() {
					return new ThreadPoolExecutor(10, 10, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(30), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
				}
				
			});
			
			Salmon salmon = i.getInstance(Salmon.class);
			MagicEnvelope.withDeserializer(new XMLMagicEnvelopeDeserializer());
			MagicEnvelope env = MagicEnvelopeSerializationProvider.getDefault().getDeserializer(MagicSigConstants.MEDIA_TYPE_MAGIC_ENV_XML).deserialize(SalmonTest.class.getResourceAsStream("/BasicEnvelope.txt"));
			
			byte[] dataBytes = salmon.verify(env);
			Assert.assertArrayEquals(dataBytes, getBytes("/BasicAtom.txt"));
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
	public static class BasicAtomDataParser implements DataParser {

		public URI getSignerUri(byte[] data) throws MagicSigException {
			return URI.create("acct:test@example.com");
		}

		public boolean parsesMimeType(String mimeType) {
			return "application/atom+xml".equals(mimeType);
		}
	}
	
	public static class BasicKeyFinder implements KeyFinder {

		public List<Key> findKeys(URI signerUri) throws MagicSigException {
			if (URI.create("acct:test@example.com").equals(signerUri)) {
				return Arrays.asList(new Key[] { new MagicKey(getBytes("/BasicRSAKey.txt"))});
			}
			throw new MagicSigException("Can't find keys for " + signerUri);
		}	
	}
	
	public static class BasicPayloadToMetadataMapper implements PayloadToMetadataMapper {

		public List<Key> getKeys(String mediaType, byte[] data) throws MagicSigException {
			List<Key> keys = new LinkedList<Key>();
			try {
				keys.add(new MagicKey(getBytes("/BasicRSAKey.txt")));
			} catch (Exception e) {
				throw new MagicSigException("Couldn't read the keys!");
			}
			return keys;
		}
	}
}
