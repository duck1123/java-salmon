package com.cliqset.magicsig.guice;

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
import com.google.inject.multibindings.Multibinder;

public class DefaultMagicSigModule extends AbstractModule {

	@Override
	protected void configure() {
		Multibinder<MagicSigAlgorithm> algorithmBinder = Multibinder.newSetBinder(binder(), MagicSigAlgorithm.class);
	    algorithmBinder.addBinding().to(HMACSHA256MagicSigAlgorithm.class);
	    algorithmBinder.addBinding().to(RSASHA256MagicSigAlgorithm.class);
	    
	    Multibinder<MagicSigEncoding> encodingBinder = Multibinder.newSetBinder(binder(), MagicSigEncoding.class);
	    encodingBinder.addBinding().to(Base64URLMagicSigEncoding.class);
	    
	    Multibinder<KeyFinder> keyFinderBinder = Multibinder.newSetBinder(binder(), KeyFinder.class);
	    keyFinderBinder.addBinding().to(MagicPKIKeyFinder.class);
	    
	    Multibinder<DataParser> dataParserBinder = Multibinder.newSetBinder(binder(), DataParser.class);
	    dataParserBinder.addBinding().to(SimpleAtomDataParser.class);

		bind(PayloadToMetadataMapper.class).to(URIPayloadToMetadataMapper.class);
	}
}
