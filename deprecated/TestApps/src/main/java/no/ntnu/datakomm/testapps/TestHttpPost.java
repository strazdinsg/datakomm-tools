package no.ntnu.datakomm.testapps;

import java.util.HashMap;
import java.util.Map;
import no.ntnu.alesund.FetchWebString;

/**
 * Send a Test HTTP Post with some dummy parameters, including non-standard
 * UTF-8 characters
 * @author Girts Strazdins, 2016-10-09
 */
public class TestHttpPost {
    public static void main(String[] args) {
        String url = "http://httpbin.org/post";
        FetchWebString fetcher = new FetchWebString();
        fetcher.setDebug(true);
        Map<String, String> dummyParams = null;
        dummyParams = new HashMap<>();
        dummyParams.put("duck", "go ✌");
        dummyParams.put("cīty", "Ålesund");
        
        String response = fetcher.httpPost(url, dummyParams);
        System.out.println("Response:");
        System.out.println(response);
    }
}
