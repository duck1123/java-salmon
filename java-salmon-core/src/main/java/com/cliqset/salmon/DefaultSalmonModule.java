package com.cliqset.salmon;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.cliqset.hostmeta.HostMeta;
import com.cliqset.hostmeta.JavaNetXRDFetcher;
import com.cliqset.hostmeta.XRDFetcher;
import com.cliqset.magicsig.DataParser;
import com.cliqset.magicsig.KeyFinder;
import com.cliqset.magicsig.MagicSig;
import com.cliqset.magicsig.MagicSigEncoding;
import com.cliqset.magicsig.PayloadToMetadataMapper;
import com.cliqset.magicsig.URIPayloadToMetadataMapper;
import com.cliqset.magicsig.MagicSigAlgorithm;
import com.cliqset.magicsig.algorithm.HMACSHA256MagicSigAlgorithm;
import com.cliqset.magicsig.algorithm.RSASHA256MagicSigAlgorithm;
import com.cliqset.magicsig.dataparser.SimpleAtomDataParser;
import com.cliqset.magicsig.encoding.Base64URLMagicSigEncoding;
import com.cliqset.magicsig.keyfinder.MagicPKIKeyFinder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;

public class DefaultSalmonModule extends AbstractModule {

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
	}
	
	@Provides
	ExecutorService provideExecutorService() {
		return new ThreadPoolExecutor(10, 10, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(30), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
	}
}
