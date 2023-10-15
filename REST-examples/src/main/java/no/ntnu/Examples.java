package no.ntnu;

import java.util.HashMap;
import java.util.Map;

/**
 * An example showing how to send HTTP GET and read the response from the server.
 */
public class Examples {
  /**
   * Entrypoint of the application.
   *
   * @param args Command-line arguments, not used
   */
  public static void main(String[] args) {
    sendHttpGetRequests();
    sendHttpPostRequests();
  }

  /**
   * Send HTTP GET requests.
   */
  private static void sendHttpGetRequests() {
    // This should return a redirect to a secure version (using HTTPS)
    HttpRequestSender.sendGetRequest("http://hello-chuck.netlify.app/");

    // This should succeed and return a response
    HttpRequestSender.sendGetRequest("https://hello-chuck.netlify.app/");
  }

  /**
   * Send HTTP POST requests, with some data.
   */
  private static void sendHttpPostRequests() {
    Map<String, Object> data = new HashMap<>();
    data.put("name", "Fish & chips");
    data.put("age", 12);

    HttpRequestSender.sendPostRequest(
        "https://web-tek.ninja/php_backend/post_recipient.php", data, null);
    Person chuck = new Person("Chuckz", -3);
    HttpRequestSender.sendPostRequest(
        "https://web-tek.ninja/php_backend/post_recipient.php", null, chuck);
  }
}
