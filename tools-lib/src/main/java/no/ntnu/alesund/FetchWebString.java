package no.ntnu.alesund;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Class that can send HTTP GET or HTTP Post to a specified Web URL and return
 * the whole response (body, not headers) as one string
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
     *
     * @param debug when set to true, debug output will be printed to System.out
     * during operation. When false, debug output us disabled.
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    /**
     * Fetches a string from the web, sends HTTP GET request
     *
     * @param url - the address where to send the request to the parameters MUST
     * NOT be included in the URL, rather they should be in the parameters map!
     * @param parameters a map containing parameters. Keys are parameter names
     * @return String response, or null on error
     */
    public String httpGet(String url, Map<String, String> parameters) {
        return sendRequest(url, parameters, false);
    }

    /**
     * Send an HTTP POST to specified url with specified parameters
     *
     * @param url
     * @param parameters a map containing parameters. Keys are parameter names
     * @return
     */
    public String httpPost(String url, Map<String, String> parameters) {
        return this.sendRequest(url, parameters, true);
    }

    /**
     * Send HTTP GET or POST with given parameters
     *
     * @param url - the address where to send the request to the parameters MUST
     * NOT be included in the URL, rather they should be in the parameters map!
     * @param parameters a map containing parameters. Keys are parameter names
     * @param usePost when true, send HTTP POST, otherwise: HTTP GET
     * @return String response, or null on error
     */
    private String sendRequest(String url, Map<String, String> parameters,
            boolean usePost) {
        resultBuffer = null;
        try {
            String body = null;
            byte[] postData = null;

            // Set parameters
            if (parameters != null) {
                String paramString = getParamString(parameters);
                if (paramString != null && !"".equals(paramString)) {
                    if (usePost) {
                        // For POST: add the parameters as the body
                        body = paramString;
                    } else {
                        // For GET method just add the parameters to the URL
                        url += "?" + paramString;
                    }
                }
            }

            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setInstanceFollowRedirects(false);

            int postDataLength = 0;
            if (body != null) {
                // The parameters must be sent in the body
                postData = body.getBytes(StandardCharsets.UTF_8);
                postDataLength = postData.length;
                conn.setDoOutput(true);
            }
            
            if (usePost && postDataLength > 0) {
                // Set some HTTP parameters for POST
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                conn.setRequestProperty("charset", "utf-8");
                conn.setRequestProperty("Content-Length",
                        Integer.toString(postDataLength));
            } else {
                //If no data supplied, send GET
                conn.setRequestMethod("GET");
            }

            debugOut("Staring connection...");
            conn.connect();
            debugOut("Connected");

            // Send the body if necessary
            if (postData != null) {
                DataOutputStream wr;
                wr = new DataOutputStream(conn.getOutputStream());
                wr.write(postData);
            }

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
     *
     * @param is
     * @return
     */
    private String convertStreamToString(InputStream is) {
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
     *
     * @param msg
     */
    private void debugOut(String msg) {
        if (debug) {
            System.out.println(msg);
        }
    }

    /**
     * Take parameters as a map, convert to one string in the form
     * param1=value1&param2=value2...
     *
     * Should automatically encode weird values in the URL-encoding will skip
     * parameters if the encoding fails
     *
     * @param parameters
     * @return
     */
    public String getParamString(Map<String, String> parameters) {
        String paramString = "";
        for (Map.Entry<String, String> p : parameters.entrySet()) {
            // Make sure only valid characters are encoded
            String paramName = null;
            String paramValue = null;
            try {
                if (p.getKey() != null) {
                    paramName = URLEncoder.encode(p.getKey(), "UTF-8");
                }
                if (p.getValue() != null) {
                    paramValue = URLEncoder.encode(p.getValue(), "UTF-8");
                }
            } catch (UnsupportedEncodingException ex) {
                debugOut("Could not convert parameters: " + ex.getMessage());
            }
            if (paramName != null && !paramName.equals("")) {
                if (paramString.length() > 0) {
                    paramString += "&"; // add separator between parameters
                }
                paramString += paramName + "=" + paramValue;
            }
        }

        return paramString;
    }

}
