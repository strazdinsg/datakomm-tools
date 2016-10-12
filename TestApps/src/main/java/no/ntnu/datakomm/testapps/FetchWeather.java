package no.ntnu.datakomm.testapps;

import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import no.ntnu.alesund.FetchWebString;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Static methods to fetch weather forecast (the closest one) from Yr.no
 * @author Girts Strazdins girts.strazdins@gmail.com 2016-10-05
 */
public class FetchWeather {

    // Constants used to build the API string
    private static final String API_HOST = "www.yr.no";
    private static final String API_PATH_TEMPLATE = "/sted/{loc}/varsel.xml";

    public static void main(String[] args) {
        String loc = "Norge/Møre_og_Romsdal/Ålesund/Ålesund";
        float temp = getTemperatureForLocation(loc);
        System.out.println("Temperature forecast for " + loc + ": " + temp + "C");

        loc = "Norge/Oslo/Oslo/Oslo";
        temp = getTemperatureForLocation(loc);
        System.out.println("Temperature forecast for " + loc + ": " + temp + "C");
    }

    /**
     * Get temperature forecast for a particular forecast, from YR.No
     * To get location string: Go to YR.no, find place you are interested in,
     * then when you see link something similar to this:
     * http://www.yr.no/place/Norway/M%C3%B8re_og_Romsdal/%C3%85lesund/Spjelkavik/hour_by_hour.html
     * Copy the part between "place/" and "/hour_by_hour.html", not including the
     * slashes
     * @param location
     * @return temperature in Celsius, or -273C on error
     */
    public static float getTemperatureForLocation(String location) {
        // Get info from YR.No in XML format
        String url = "http://" + API_HOST
                + API_PATH_TEMPLATE.replace("{loc}", location);
        FetchWebString fetcher = new FetchWebString();
        String response = fetcher.httpGet(url, null);

        // Parse the XML
        if (response != null && !response.equals("")) {
            return getFirstTemperatureForecast(response);
        } else {
            return -273;
        }
    }
    
    /**
     * Get one string containing the whole XML response from YR.NO, return
     * temperature of the first forecast item
     *
     * @param xmlResponse
     * @return temperature in Celsius
     */
    private static float getFirstTemperatureForecast(String xmlResponse) {
        // Code adapted by Girts Strazdins
        // from https://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
        // and http://stackoverflow.com/questions/340787/parsing-xml-with-xpath-in-java

        try {
            // Parse the whole XML using DOM
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xmlResponse));
            Document doc = dBuilder.parse(is);
            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            // Use XPath to find the necessary node
            // Find <tabular><time>[0]<temperature> node
            XPath xpath = XPathFactory.newInstance().newXPath();
            XPathExpression expr = xpath.compile(
                    "/weatherdata/forecast/tabular[1]/time[1]/temperature");
            Object result = expr.evaluate(doc, XPathConstants.NODESET);
            NodeList nodes = (NodeList) result;
            if (nodes.getLength() > 0) {
                Node tempertureNode = nodes.item(0);
                // Find attribute value="X"
                NamedNodeMap attrs = tempertureNode.getAttributes();
                Node tempValueAttr = attrs.getNamedItem("value");
                if (tempValueAttr != null) {
                    String v = tempValueAttr.getTextContent();
                    try {
                        float t = Float.parseFloat(v);
                        return t;
                    } catch (NumberFormatException ex) {
                        System.out.println("Invalid temperature value: " + v);
                    }
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException 
                | XPathExpressionException ex) {
            System.out.println("Error while parsing XML: " + ex.getMessage());
        }

        return -273; // something went wrong
    }
}
