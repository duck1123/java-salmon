package com.cliqset.salmon;

import java.net.URI;
import java.net.URL;

public interface SalmonEndpointFinder {

	URL find(URI resourceURI) throws SalmonException;
}
