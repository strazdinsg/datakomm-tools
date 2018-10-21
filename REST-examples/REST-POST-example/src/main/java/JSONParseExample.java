import org.json.JSONArray;
import org.json.JSONObject;

public class JSONParseExample {
    public static void main(String[] args) {
        JSONParseExample JSONParseExample = new JSONParseExample();

        // JSON object example
        String jsonObjectString = "{\"year\":2018,\"course\":\"datakomm\"}";
        System.out.println("Starting json: " + jsonObjectString);
        JSONObject jsonObject = JSONParseExample.fromStringToJSONObject(jsonObjectString);
        System.out.println("The json object: " + jsonObject);
        String jsonObjectStringAfter = JSONParseExample.fromJSONObjectToString(jsonObject);
        System.out.println("Json after parsing: " + jsonObjectStringAfter);

        // JSON array example
        String jsonArrayString = "[{\"year\":2018,\"course\":\"datakomm\"},{\"year\":2018,\"course\":\"datakomm\"}]";
        System.out.println("Starting json: " + jsonArrayString);
        JSONArray jsonArray = JSONParseExample.fromStringToJSONArray(jsonArrayString);
        System.out.println("The json object: " + jsonArray);
        String jsonArrayStringAfter = JSONParseExample.fromJSONArrayToString(jsonArray);
        System.out.println("Json after parsing: " + jsonArrayStringAfter);
    }

    public JSONParseExample() {}

    private JSONObject fromStringToJSONObject(String json) {
        return new JSONObject(json);
    }

    private String fromJSONObjectToString(JSONObject json) {
        return json.toString();
    }

    private JSONArray fromStringToJSONArray(String json) {
        return new JSONArray(json);
    }

    private String fromJSONArrayToString(JSONArray json) {
        return json.toString();
    }
}
