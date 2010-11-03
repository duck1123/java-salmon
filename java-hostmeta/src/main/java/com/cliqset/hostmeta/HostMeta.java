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

package com.cliqset.hostmeta;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cliqset.hostmeta.template.TemplateException;
import com.cliqset.hostmeta.template.TemplateProcessor;
import com.cliqset.xrd.Alias;
import com.cliqset.xrd.Link;
import com.cliqset.xrd.Property;
import com.cliqset.xrd.XRD;

public class HostMeta {

	private static Logger logger = LoggerFactory.getLogger(HostMeta.class);

	private static final String HOST_META_PATH = "/.well-known/host-meta";
	
	private Map<String, TemplateProcessor> templates = new HashMap<String, TemplateProcessor>();
	
	private XRDFetcher xrdFetcher = null;
	
	public HostMeta withXRDFetcher(XRDFetcher fetcher) {
		this.xrdFetcher = fetcher;
		return this;
	}
	
	public HostMeta withTemplateProcessor(String rel, TemplateProcessor processor) {
		this.templates.put(rel, processor);
		return this;
	}
	
	public Descriptor discoverHostWide(URI uri) throws HostMetaException {
		BuildDescriptorHandler handler = new BuildDescriptorHandler();
		discoverHostWide(uri, handler);
		return handler.getDescriptor();
	}
	
	public void discoverHostWide(URI uri, HostMetaHandler handler) throws HostMetaException {
		XRD xrd = getHostMeta(uri);
		processHostWide(xrd, handler);
	}
	
	public Descriptor discoverResourceSpecific(URI uri) throws HostMetaException {
		BuildDescriptorHandler handler = new BuildDescriptorHandler();
		discoverResourceSpecific(uri, handler);
		return handler.getDescriptor();
	}
	
	public void discoverResourceSpecific(URI uri, HostMetaHandler handler) throws HostMetaException {
		XRD xrd = getHostMeta(uri);
		processResourceSpecific(uri, xrd, handler);
	}

	private XRD getHostMeta(URI uri) throws HostMetaException {
		String uriHost = null;

        //TODO: allow for extensible way to extract host from uri based on scheme
		
        if ("acct".equals(uri.getScheme())) {
            uriHost = extractHostFromAcct(uri);
        } else {
            uriHost = uri.getHost();
        }

        if (null == uriHost) {
        	logger.warn("Unable to determine host of {}, with scheme of {}", uri, uri.getScheme());
        	throw new HostMetaException("Unable to determine host of uri:" + uri);
        }

        for (String scheme : new String[] {"https", "http"}) {
	        URL hostmetaXrdLocation = null;
	        try {
	            hostmetaXrdLocation = new URL(scheme, uriHost, HOST_META_PATH);
	            return fetchXRD(hostmetaXrdLocation);
	        } catch (HostMetaException hme) {
	        	//log it, squash it, try the next scheme
	        	logger.debug("No valid host-meta found at {}", hostmetaXrdLocation.toString());
	        } catch (MalformedURLException e) {
	        	logger.warn("host-meta url is malformed.  scheme: {}, uriHost: {}, HOST_META_PATH : {}", new String[] {scheme, uriHost, HOST_META_PATH});
	            throw new HostMetaException(e);
	        }
        }
        throw new HostMetaException("No valid host-meta found.");
	}
	
	private XRD fetchXRD(URL url) throws HostMetaException {
		if (null == this.xrdFetcher) {
			throw new HostMetaException("An XRDFetcher must be configured.");
		}
		return this.xrdFetcher.fetchXRD(url);
	}
	
	private void processHostWide(XRD xrd, HostMetaHandler handler) {
		//Subject
		if (xrd.hasSubject()) {
			if (handler.onSubject(xrd.getSubject())) { return; }
		}
		//Aliases
		if (xrd.hasAliases()) {
			for (Alias a : xrd.getAliases()) {
				if (handler.onAlias(a)) { return; }
			}
		}
		//Expires
		if (xrd.hasExpires()) {
			if (handler.onExpires(xrd.getExpires())) { return; }
		}
		//Properties
		if (xrd.hasProperties()) {
			for (Property p : xrd.getProperties()) {
				if (handler.onProperty(p)) { return; }
			}
		}
		//Links
		if (xrd.hasLinks()) {
			for (Link l : xrd.getLinks()) {
				if (l.hasHref()) {
					if (handler.onLink(l)) { return; }
				}
			}
		}
	}
	
	private void processResourceSpecific(URI contextResourceURI, XRD xrd, HostMetaHandler handler) {
		//Links
		if (xrd.hasLinks()) {
			for (Link l : xrd.getLinks()) {
				try {
					if (l.hasTemplate()) {
						//process the template
						URI u = this.templates.get(l.getRel().toString()).process(l.getTemplate(), contextResourceURI);
						
						if (HostMetaConstants.REL_LRDD.equals(l.getRel().toString())) {
							if (handler.onLinkVisit(l)) { return; }
							XRD x = fetchXRD(u.toURL());
							if (handler.onLinkVisited(l)) { return; }
							//Subject
							if (x.hasSubject()) {
								if (handler.onSubject(x.getSubject())) { return; }
							}
							//Aliases
							if (x.hasAliases()) {
								for (Alias a : x.getAliases()) {
									if (handler.onAlias(a)) { return; }
								}
							}
							//Expires
							if (x.hasExpires()) {
								if (handler.onExpires(x.getExpires())) { return; }
							}
							//Properties
							if (x.hasProperties()) {
								for (Property p : x.getProperties()) {
									if (handler.onProperty(p)) { return; }
								}
							}
							//for each link that is not an lrdd
							for (Link lk : x.getLinks()) {
								if (!HostMetaConstants.REL_LRDD.equals(lk.getRel().toString())) {
									if (lk.hasTemplate()) {
										URI processedTemplate = this.templates.get(lk.getRel().toString()).process(lk.getTemplate(), contextResourceURI);
										lk.setProcessedTemplate(processedTemplate);
									}
									
									if (handler.onLink(lk)) { return; }
								}
							}
						} else {
							l.setProcessedTemplate(u);
							if (handler.onLink(l)) { return; }
						}
					}
				} catch (TemplateException te) {
					//TODO:  log it and skip link
				} catch (MalformedURLException e) {
					//TODO:  log it and skip link
				} catch (HostMetaException e) {
					//TODO:  log it and skip link
				}
			}
		}
	}

	private String extractHostFromAcct(URI uri) throws HostMetaException {
        String[] splitUri = uri.getSchemeSpecificPart().split("@");
        if (2 != splitUri.length) {
            throw new HostMetaException("Invalid acct URI.");
        }
        return splitUri[1];
    }
}
