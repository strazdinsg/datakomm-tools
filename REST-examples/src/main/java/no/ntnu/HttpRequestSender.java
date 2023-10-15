package no.ntnu;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * A utility class for sending HTTP requests.
 */
public class HttpRequestSender {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * Not allowed to create instances of this utility class.
   */
  private HttpRequestSender() {
  }

  public static void sendGetRequest(String url) {
    sendHttpRequest(false, url, null, null);
  }

  /**
   * Check whether the given HTTP status code means "a redirect to another site is requested".
   *
   * @param statusCode The HTTP status code to check
   * @return True, if this code means "Redirect"
   */
  private static boolean isRedirect(int statusCode) {
    return statusCode / 100 == 3;
  }

  /**
   * Send and HTTP POST request. Note: use either formData or bodyData, not both!
   *
   * @param url      The URL to send the request to
   * @param formData The POST data to send in the form-data format
   * @param bodyData The data to send as a custom JSON in the request body.
   * @throws IllegalArgumentException When both formData and bodyData are specified
   */
  public static void sendPostRequest(String url, Map<String, Object> formData, Object bodyData)
      throws IllegalArgumentException {
    if (formData != null && bodyData != null) {
      throw new IllegalArgumentException("Can't have both form data and body data for a POST!");
    }
    String dataType = formData != null ? "form" : "body";
    System.out.println("Sending GET request with " + dataType + " data to " + url);
    sendHttpRequest(true, url, formData, bodyData);
  }

  /**
   * Send an HTTP request. Warning: only one of form or body data can be specified,
   * and only for POST requests!
   *
   * @param isPost   When true, send an HTTP POST, otherwise send an HTTP GET
   * @param url      The URL to send the request to
   * @param formData Form data to post.
   * @param bodyData Custom object to post as a JSON in the body.
   * @throws IllegalArgumentException When the provided data can't be sent
   */
  private static void sendHttpRequest(boolean isPost, String url,
                                      Map<String, Object> formData, Object bodyData)
      throws IllegalArgumentException {
    if ((formData != null || bodyData != null) && !isPost) {
      throw new IllegalArgumentException("Can't send data in the body for a GET request");
    }

    System.out.println("----------------------------------------");
    System.out.println("Sending HTTP request to " + url);
    System.out.println("----------------------------------------\n");

    HttpRequest.Builder requestBuilder;
    if (isPost) {
      requestBuilder = preparePostRequest(formData, bodyData);
    } else {
      requestBuilder = HttpRequest.newBuilder().GET();
    }
    requestBuilder.uri(URI.create(url));

    try {
      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request = requestBuilder.build();
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      if (response.statusCode() == HttpURLConnection.HTTP_OK) {
        System.out.println("Server's response: " + response.body());
      } else {
        if (isRedirect(response.statusCode())) {
          System.out.println("The server requests a redirect: " + response.body());
        } else {
          System.err.println("Request failed with status code " + response.statusCode());
        }
      }
    } catch (IOException e) {
      System.err.println("HTTP request failed: " + e.getMessage());
    } catch (InterruptedException e) {
      System.err.println("HTTP request timed out");
      Thread.currentThread().interrupt();
    }

    System.out.println();
  }

  private static HttpRequest.Builder preparePostRequest(Map<String, Object> formData,
                                                        Object bodyData) {
    HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
    String bodyString;
    String contentType;
    if (formData != null) {
      contentType = "application/x-www-form-urlencoded";
      bodyString = formDataToString(formData);
    } else {
      contentType = "application/json";
      bodyString = objectToJson(bodyData);
    }
    requestBuilder.header("Content-Type", contentType);
    requestBuilder.POST(HttpRequest.BodyPublishers.ofString(bodyString));
    return requestBuilder;
  }

  /**
   * Parse the map, convert it to a string which can be sent as form-data in an HTTP request body.
   * The result will be in the format key1=value1&...&keyN=valueN, where all the keys and values
   * will be URL-encoded, so that they are safe to send
   *
   * @param data The data to send
   * @return The data as URL-encoded form-data
   */
  private static String formDataToString(Map<String, Object> data) {
    StringBuilder formData = new StringBuilder();
    for (Map.Entry<String, Object> entry : data.entrySet()) {
      if (!formData.isEmpty()) {
        formData.append("&");
      }
      String safeKey = URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8);
      String safeValue = URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8);
      formData.append(safeKey)
          .append("=")
          .append(safeValue);
    }
    return formData.toString();
  }

  /**
   * Convert any object o a JSON string.
   *
   * @param obj The object to convert
   * @return JSON string representation of the object; or an empty string on error
   */
  private static String objectToJson(Object obj) {
    String json = "";
    try {
      json = objectMapper.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      System.err.println("Could not convert the object to JSON: " + e.getMessage());
    }
    return json;
  }
}
