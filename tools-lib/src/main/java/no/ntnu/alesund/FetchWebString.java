package no.ntnu.alesund;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class that can send HTTP GET or HTTP Post to a specified Web URL
 * and return the whole response (body, not headers) as one string 
 * 
 *
 * @author Girts Strazdins, 2016-10-09
 *
 */
public class FetchWebString {

    /**
     * The result will be stored here (buffer)
     */
    private String resultBuffer;
    
    // Set this to true if you want to see debug output to console
    private boolean debug = false; 
    
    /**
     * Control the debug output
     * @param debug when set to true, debug output will be printed to 
     * System.out during operation. When false, debug output us disabled.
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    /**
     * Fetches a string from the web, sends HTTP GET request
     *
     * @param url - the address where to send the request to
     *    the parameters should be added to the URL in the usual url form:
     *    http://example.com/path?param1=value1&param2=value2
     * @return String response, or null on error
     */
    public String httpGet(final String url) {
        resultBuffer = null;
        try {
            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            debugOut("Staring connection...");
            conn.connect();
            debugOut("Connected");
            InputStream stream = conn.getInputStream();
            resultBuffer = convertStreamToString(stream);
            stream.close();
        } catch (Exception e) {
            debugOut("Error fetching the web page: " + e.getMessage());
        }
        return resultBuffer;
    }

    /**
     * Read the whole content from an InputStream, return it as a string
     * @param is
     * @return 
     */
    String convertStreamToString(InputStream is) {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        String response = "";
        String inputLine;
        try {
            while ((inputLine = in.readLine()) != null) {
                response += inputLine;
            }
        } catch (IOException ex) {
            debugOut("Could not read the data from HTTP response: " 
                    + ex.getMessage());
        } finally {
        }
        return response;
    }

    /**
     * Print message to System.out if debug is enabled
     * @param msg 
     */
    private void debugOut(String msg) {
        if (debug) {
            System.out.println(msg);
        }
    }

}
