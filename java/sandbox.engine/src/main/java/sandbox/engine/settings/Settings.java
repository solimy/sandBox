package sandbox.engine.settings;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Settings extends DefaultHandler {
	String current;
	Map<String, String> values = new HashMap<>();

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		current = qName;
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		current = null;
	}

	@Override
	public void characters(char[] caracteres, int debut, int longueur) {
		String donnees = new String(caracteres, debut, longueur);
		if (current != null)
			values.put(current, donnees);
		current = null;
	}
	
	public String get(String setting) {
		return values.get(setting);
	}
}