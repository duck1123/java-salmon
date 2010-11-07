package com.cliqset.salmon.examples;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.cliqset.magicsig.DataParser;
import com.cliqset.magicsig.KeyFinder;
import com.cliqset.magicsig.MagicEnvelope;
import com.cliqset.magicsig.MagicKey;
import com.cliqset.magicsig.MagicSig;
import com.cliqset.magicsig.MagicSigAlgorithm;
import com.cliqset.magicsig.MagicSigEncoding;
import com.cliqset.magicsig.URIPayloadToMetadataMapper;
import com.cliqset.magicsig.algorithm.RSASHA256MagicSigAlgorithm;
import com.cliqset.magicsig.compact.CompactMagicEnvelopeDeserializer;
import com.cliqset.magicsig.compact.CompactMagicEnvelopeSerializer;
import com.cliqset.magicsig.encoding.Base64URLMagicSigEncoding;
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
			Map<String, MagicSigAlgorithm> algorithms = new HashMap<String, MagicSigAlgorithm>();
			algorithms.put("RSA-SHA256", new RSASHA256MagicSigAlgorithm());
			
			Map<String, MagicSigEncoding> encodings = new HashMap<String, MagicSigEncoding>();
			encodings.put("base64url", new Base64URLMagicSigEncoding());
			
			Set<DataParser> dataParsers = new HashSet<DataParser>();
			
			Set<KeyFinder> keyFinders = new HashSet<KeyFinder>();
			
			MagicSig magicSig = new MagicSig(algorithms, encodings, new URIPayloadToMetadataMapper(dataParsers, keyFinders));
			
			Salmon salmon = new Salmon(magicSig);
			
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
