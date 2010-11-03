package com.cliqset.hostmeta;

import java.net.URL;

import com.cliqset.xrd.XRD;

public interface XRDFetcher {
	
	XRD fetchXRD(URL url) throws HostMetaException;
}
