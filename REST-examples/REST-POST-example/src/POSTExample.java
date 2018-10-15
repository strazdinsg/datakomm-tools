import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class POSTExample {

    public static void main(String[] args) {
        POSTExample postExample = new POSTExample("localhost", "8080");
        postExample.post3RandomNumbers();
    }

    private String port;
    private String host;
    private String api;

    public POSTExample(String host, String port) {
        this.host = host;
        this.port = port;
        api = "http://" + host + ":" + port + "/";
    }

    public void post3RandomNumbers() {
        int a = (int) Math.round(Math.random() * 100);
        int b = (int) Math.round(Math.random() * 100);
        int c = (int) Math.round(Math.random() * 100);

        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"a\":").append(a);
        json.append(",\"b\":").append(b);
        json.append(",\"c\":").append(c);
        json.append("}");
        System.out.println(json.toString());
        // TODO: change urlParam to something correct
        sendPost("api", json.toString());
    }

    // HTTP POST request
    private void sendPost(String urlParam, String json) {
        try {
            URL object = new URL(api + urlParam);
            HttpURLConnection con = (HttpURLConnection) object.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            OutputStream os = con.getOutputStream();
            os.write(json.getBytes());
            os.flush();

            int responseCode = con.getResponseCode();
            if(responseCode == 200) {
                System.out.println("Reached");
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
