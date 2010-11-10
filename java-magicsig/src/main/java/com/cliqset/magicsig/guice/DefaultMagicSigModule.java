package com.cliqset.magicsig.guice;

import com.cliqset.hostmeta.HostMeta;
import com.cliqset.hostmeta.JavaNetXRDFetcher;
import com.cliqset.hostmeta.XRDFetcher;
import com.cliqset.hostmeta.template.LRDDTemplateProcessor;
import com.cliqset.hostmeta.template.TemplateProcessor;
import com.cliqset.magicsig.DataParser;
import com.cliqset.magicsig.KeyFinder;
import com.cliqset.magicsig.MagicSigAlgorithm;
import com.cliqset.magicsig.MagicSigEncoding;
import com.cliqset.magicsig.PayloadToMetadataMapper;
import com.cliqset.magicsig.URIPayloadToMetadataMapper;
import com.cliqset.magicsig.algorithm.HMACSHA256MagicSigAlgorithm;
import com.cliqset.magicsig.algorithm.RSASHA256MagicSigAlgorithm;
import com.cliqset.magicsig.dataparser.SimpleAtomDataParser;
import com.cliqset.magicsig.encoding.Base64URLMagicSigEncoding;
import com.cliqset.magicsig.keyfinder.MagicPKIKeyFinder;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;

public class DefaultMagicSigModule extends AbstractModule {

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
	    dataParserBinder.addBinding("application/atom+xml").to(SimpleAtomDataParser.class);

		bind(PayloadToMetadataMapper.class).to(URIPayloadToMetadataMapper.class);
		
		MapBinder<String, TemplateProcessor> templateBinder = MapBinder.newMapBinder(binder(), String.class, TemplateProcessor.class);
		templateBinder.addBinding(LRDDTemplateProcessor.REL).to(LRDDTemplateProcessor.class);
	}
}
