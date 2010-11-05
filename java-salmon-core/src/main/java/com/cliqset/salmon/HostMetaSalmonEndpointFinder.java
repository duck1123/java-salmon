package com.cliqset.salmon;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cliqset.hostmeta.HostMeta;
import com.cliqset.hostmeta.HostMetaException;
import com.cliqset.hostmeta.HostMetaHandler;
import com.cliqset.xrd.Link;

public class HostMetaSalmonEndpointFinder implements SalmonEndpointFinder {

	private static Logger logger = LoggerFactory.getLogger(HostMetaSalmonEndpointFinder.class);
	
	private HostMeta hostMeta = null;
	
	public HostMetaSalmonEndpointFinder() { 
		this.hostMeta = new HostMeta();
	}
	
	public HostMetaSalmonEndpointFinder(HostMeta hostMeta) { 
		this.hostMeta = hostMeta;
	}
	
	public URL find(URI resourceURI) throws SalmonException {
		try {
			SalmonEndpointHandler handler = new SalmonEndpointHandler();
			hostMeta.discoverResourceSpecific(resourceURI, handler);
			return handler.salmonEndpoint;
		} catch (HostMetaException hme) {
			throw new SalmonException(hme);
		}
	}
	
	private static class SalmonEndpointHandler extends HostMetaHandler {

		public URL salmonEndpoint = null; 
		
		public boolean onLink(Link link) {
			if (Salmon.REL_SALMON.equals(link.getRel().toString())) {
				try {
					salmonEndpoint = link.getHref().toURL();
					return true;
				} catch (MalformedURLException mue) {
					//if not a valid URL, log it and continue
					logger.warn("Salmon endpoint value: " + link.getHref() + " is not a valid URL, ignoring.");
				}
			}
			return false;
		}
	}

}
