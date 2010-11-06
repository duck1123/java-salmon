package com.cliqset.salmon.examples;

import java.net.URI;

import com.cliqset.magicsig.MagicEnvelope;
import com.cliqset.magicsig.MagicKey;
import com.cliqset.magicsig.MagicSigner;
import com.cliqset.magicsig.URIPayloadToMetadataMapper;
import com.cliqset.magicsig.algorithm.RSASHA256MagicSignatureAlgorithm;
import com.cliqset.magicsig.compact.CompactMagicEnvelopeDeserializer;
import com.cliqset.magicsig.compact.CompactMagicEnvelopeSerializer;
import com.cliqset.magicsig.encoding.Base64URLMagicSignatureEncoding;
import com.cliqset.magicsig.json.JSONMagicEnvelopeDeserializer;
import com.cliqset.magicsig.json.JSONMagicEnvelopeSerializer;
import com.cliqset.magicsig.keyfinder.MagicPKIKeyFinder;
import com.cliqset.magicsig.xml.XMLMagicEnvelopeDeserializer;
import com.cliqset.magicsig.xml.XMLMagicEnvelopeSerializer;
import com.cliqset.salmon.HostMetaSalmonEndpointFinder;
import com.cliqset.salmon.JavaNetSalmonSender;
import com.cliqset.salmon.Salmon;
import com.cliqset.salmon.SimpleAtomDataParser;

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
			Salmon salmon = new Salmon(new MagicSigner()
					.withEncoding(new Base64URLMagicSignatureEncoding())
					.withAlgorithm(new RSASHA256MagicSignatureAlgorithm())
					.withPayloadToMetadataMapper(new URIPayloadToMetadataMapper()
						.withKeyFinder(new MagicPKIKeyFinder())
						.withDataParser(new SimpleAtomDataParser())))
				.withSalmonEndpointFinder(new HostMetaSalmonEndpointFinder())
				.withSalmonSender(new JavaNetSalmonSender());
			
			MagicEnvelope.withSerializer(new CompactMagicEnvelopeSerializer());
			MagicEnvelope.withSerializer(new JSONMagicEnvelopeSerializer());
			MagicEnvelope.withSerializer(new XMLMagicEnvelopeSerializer());
			
			MagicEnvelope.withDeserializer(new CompactMagicEnvelopeDeserializer());
			MagicEnvelope.withDeserializer(new JSONMagicEnvelopeDeserializer());
			MagicEnvelope.withDeserializer(new XMLMagicEnvelopeDeserializer());
			
			MagicKey key = new MagicKey(keyString.getBytes("UTF-8"));
			salmon.signAndDeliver(entry.getBytes("UTF-8"), key, destinationUser);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
