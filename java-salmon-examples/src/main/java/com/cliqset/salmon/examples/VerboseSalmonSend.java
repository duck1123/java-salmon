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
package com.cliqset.salmon.examples;

import java.net.URI;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.cliqset.hostmeta.HostMeta;
import com.cliqset.hostmeta.JavaNetXRDFetcher;
import com.cliqset.hostmeta.XRDFetcher;
import com.cliqset.hostmeta.template.LRDDTemplateProcessor;
import com.cliqset.hostmeta.template.TemplateProcessor;
import com.cliqset.magicsig.DataParser;
import com.cliqset.magicsig.KeyFinder;
import com.cliqset.magicsig.MagicKey;
import com.cliqset.magicsig.MagicSig;
import com.cliqset.magicsig.MagicSigAlgorithm;
import com.cliqset.magicsig.MagicSigEncoding;
import com.cliqset.magicsig.PayloadToMetadataMapper;
import com.cliqset.magicsig.URIPayloadToMetadataMapper;
import com.cliqset.magicsig.algorithm.HMACSHA256MagicSigAlgorithm;
import com.cliqset.magicsig.algorithm.RSASHA256MagicSigAlgorithm;
import com.cliqset.magicsig.dataparser.SimpleAtomDataParser;
import com.cliqset.magicsig.encoding.Base64URLMagicSigEncoding;
import com.cliqset.magicsig.keyfinder.MagicPKIKeyFinder;
import com.cliqset.salmon.HostMetaSalmonEndpointFinder;
import com.cliqset.salmon.JavaNetSalmonSender;
import com.cliqset.salmon.Salmon;
import com.cliqset.salmon.SalmonEndpointFinder;
import com.cliqset.salmon.SalmonSender;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;

public class VerboseSalmonSend {
	public static final String keyString = "RSA.oidnySWI_e4OND41VHNtYSRzbg5SaZ0YwnQ0J1ihKFEHY49-61JFybnszkSaJJD7vBfxyVZ1lTJjxdtBJzSNGEZlzKbkFvcMdtln8g2ec6oI2G0jCsjKQtsH57uHbPY3IAkBAW3Ar14kGmOKwqoGUq1yhz93rXUomLnDYwz8E88=.AQAB.hgOzTxbqhZN9wce4I7fSKnsJu2eyzP69O9j2UZ56cuulA6_Q4YP5kaNMB53DF32L0ASqHBCM1WXz984hptlT0e4U3asXxqegTqrGPNAXw5A6r2E-9MeS84LDFUnUz420YPxMxknzMJBeAz21PuKyrv_QZf6zmRQ0m5eQ0QNJoYE=";
	public static final String entry = 
	"<entry xmlns=\"http://www.w3.org/2005/Atom\" xml:lang=\"en\">" +
		"<id>tag:somesite.com,2010-11-05:/username/entry/7k23hk0hjiAlIFjkggmCTjPObFoj5XIbpWrs5iCokMY</id>" +
		"<published>2010-11-05T19:38:27.703Z</published>" +
		"<updated>2010-11-05T19:38:27.703Z</updated>" +
		"<summary type=\"html\">hey, so here is the summary</summary>" + 
		"<title type=\"text\">hey, so here is the title</title>" +
		"<author>" +
			"<name>Charlie</name>" +
			"<uri>acct:charlie@domian.com</uri>" +
		"</author>" +
	"</entry>";
	
	public static final URI destinationUser = URI.create("acct:charlie@reticulateme.appspot.com");
	
	public static void main(String[] args) {
		try {
			Injector injector = Guice.createInjector(new CustomSalmonModule());
			Salmon salmon = injector.getInstance(Salmon.class);
			MagicKey key = new MagicKey(keyString.getBytes("UTF-8"));
			salmon.signAndDeliver(entry.getBytes("UTF-8"), key, destinationUser);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static class CustomSalmonModule extends AbstractModule {

		@Override
		protected void configure() {
			bind(MagicSig.class);
			bind(SalmonSender.class).to(JavaNetSalmonSender.class);
			
			bind(SalmonEndpointFinder.class).to(HostMetaSalmonEndpointFinder.class);
			
			bind(XRDFetcher.class).to(JavaNetXRDFetcher.class);
			
			bind(HostMeta.class);
			
			//MagicSig dependencies
			MapBinder<String, MagicSigAlgorithm> algorithmBinder = MapBinder.newMapBinder(binder(), String.class, MagicSigAlgorithm.class);
		    algorithmBinder.addBinding(HMACSHA256MagicSigAlgorithm.ALGORITHM_IDENTIFIER).to(HMACSHA256MagicSigAlgorithm.class);
		    algorithmBinder.addBinding(RSASHA256MagicSigAlgorithm.ALGORITHM_IDENTIFIER).to(RSASHA256MagicSigAlgorithm.class);
		    
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
		
		@Provides
		ExecutorService provideExecutorService() {
			return new ThreadPoolExecutor(10, 10, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(30), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
		}
	}
}
