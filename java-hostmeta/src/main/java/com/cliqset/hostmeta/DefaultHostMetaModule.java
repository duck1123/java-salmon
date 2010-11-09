package com.cliqset.hostmeta;

import com.cliqset.hostmeta.template.LRDDTemplateProcessor;
import com.cliqset.hostmeta.template.TemplateProcessor;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;

public class DefaultHostMetaModule extends AbstractModule {

	@Override
	protected void configure() {
		MapBinder<String, TemplateProcessor> templateBinder = MapBinder.newMapBinder(binder(), String.class, TemplateProcessor.class);
	    templateBinder.addBinding(LRDDTemplateProcessor.REL).to(LRDDTemplateProcessor.class);
	    
	    bind(XRDFetcher.class).to(JavaNetXRDFetcher.class);
	}
}
