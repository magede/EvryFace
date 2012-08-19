package se.magede.evryface.intranet;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

public class HtmlToXmlParser {

	public static String parse(String html, InputStream xslStream) throws Exception {
		
		String cleanedHtml = cleanAndStripHtml(html);
		
		StreamSource xslSource = new StreamSource(xslStream);
		StreamSource xmlSource = new StreamSource(new ByteArrayInputStream(cleanedHtml.getBytes("UTF-8")));
		StringWriter outWriter = new StringWriter();  
		TransformerFactory factory = TransformerFactory.newInstance();			
		Transformer transformer = factory.newTransformer(xslSource);
		transformer.transform(xmlSource, new StreamResult( outWriter ));

		return outWriter.toString();
	}
	
	private static String cleanAndStripHtml(String htmlText) {
		
		Document doc = Jsoup.parse(htmlText);
		
		Whitelist whiteList = Whitelist.none().addTags("tr","td", "table", "img");
		whiteList.addAttributes("table", "id");
		whiteList.addAttributes("img", "src");
		
		Cleaner cleaner = new Cleaner(whiteList);
		Document filteredDoc = cleaner.clean(doc);
		
		filteredDoc.select(":containsOwn(\u00a0)").remove();
		filteredDoc.select("tr").not(":has(td)").remove();
		
		Elements interestingParts = filteredDoc.select("table#tblInfo");
		
		String selectedHtmlAsString = "<table>" + interestingParts.html() + "</table>";
		selectedHtmlAsString = selectedHtmlAsString.replaceAll("&Atilde;&para;", "ö");
		selectedHtmlAsString = selectedHtmlAsString.replaceAll("&ouml;", "ö");
		selectedHtmlAsString = selectedHtmlAsString.replaceAll("&auml;", "ä");
		selectedHtmlAsString = selectedHtmlAsString.replaceAll("&nbsp;", "");
		
		return selectedHtmlAsString;
	}	
	
	
}
