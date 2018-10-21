
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Example on how to parse JSON strings to objects or arrays, and vice versa
 */
public class JSONParseExample {

    public static void main(String[] args) {

        // Note: to do the JSON parsing you need org.json library
        // This project imports it automatically: the dependency to the library 
        // Is described in the pom.xml file. 
        objectExample();
        arrayExample();
        nestedJsonExample();
    }

    private static void objectExample() {
        System.out.println("-------------------------------");
        System.out.println("Test JSON Object parsing");
        System.out.println("-------------------------------");

        // JSON object example
        // We start with a String in JSON notation. It describes an object
        // We will try to extract the data from the JSON string
        String jsonObjectString = "{ \"year\": 2017, \"course\": \"datakomm\" }";
        System.out.println("Starting json string: " + jsonObjectString);

        // Let's try to parse it as a JSON object
        try {
            JSONObject jsonObject = new JSONObject(jsonObjectString);
            if (jsonObject.has("year")) {
                int year = jsonObject.getInt("year");
                System.out.println("The object contains field 'year' with value "
                        + year);
            }

            // We can also change some fields in the JSONObject
            jsonObject.put("year", 2018);

            // And if we want to translate a JSON object to a string, we simply
            // use toString() method
            System.out.println("The updated JSON object as a string: "
                    + jsonObject.toString());

            // Now let's try to parse the JSON string as an array.
            // This should raise an exception, because the String does not
            // contain a JSON array
            JSONArray wrongArray = new JSONArray(jsonObjectString);

        } catch (JSONException e) {
            // It is important to always wrap JSON parsing in try/catch
            // If the string is suddently not in the expected format, 
            // an exception will be generated
            System.out.println("Got exception in JSON parsing: " + e.getMessage());
        }
        System.out.println("");
    }

    private static void arrayExample() {
        System.out.println("-------------------------------");
        System.out.println("Test JSON Array parsing");
        System.out.println("-------------------------------");
        String jsonArrayString = "[12, 7, 8, 9]";
        System.out.println("Starting json: " + jsonArrayString);
        try {
            JSONArray jsonArray = new JSONArray(jsonArrayString);
            System.out.println("The array has " + jsonArray.length() + " items:");
            for (int i = 0; i < jsonArray.length(); ++i) {
                System.out.println("  " + jsonArray.getInt(i));
            }

            // We can add elements to the JSON array. And they don't have
            // all to be of same type
            jsonArray.put(5.6);
            jsonArray.put("Cherries");
            System.out.println("The array has " + jsonArray.length() + " items");
            // If we don't know what type the element is, we can use the generic
            // method .get(index) and check the type
            Object elem4 = jsonArray.get(4);
            Object elem5 = jsonArray.get(5);
            if (elem4 instanceof Double) {
                Double d = (Double) elem4;
                System.out.println("Elem4 is a floating point number: " + d);
            }
            if (elem5 instanceof Double) {
                Double d = (Double) elem5;
                System.out.println("Elem5 is a floating point number: " + d);
            } else if (elem5 instanceof String) {
                String s = (String) elem5;
                System.out.println("Elem5 is a string: " + s);
            }

            System.out.println("The whole array as a string: "
                    + jsonArray.toString());

        } catch (JSONException e) {
            System.out.println("Something went wrong in JSON array parsing: "
                    + e.getMessage());
        }
        System.out.println("");
    }

    private static void nestedJsonExample() {
        System.out.println("--------------------------------------------------");
        System.out.println("Test JSON Array with objects inside");
        System.out.println("--------------------------------------------------");

        // JSON supports hierarchy. Objects can be placed inside arrays, arrays 
        // inside objects, etc. 
        // Let's create a registry of books. It will contain an array of books
        // We parse each item as an object
        String book1 = "{ \"title\": \"Computer networks\", \"pages\": 700 }";
        String book2 = "{ \"title\": \"Databases\", \"pages\": 600 }";
        String book3 = "{ \"title\": \"BlueJ\" }";

        String jsonString = "[ " + book1 + ", " + book2 + ", " + book3 + "]";
        System.out.println("Book registry in JSON format:");
        System.out.println(jsonString);

        try {
            JSONArray bookList = new JSONArray(jsonString);
            System.out.println(bookList.length() + " books in the registry");
            for (int i = 0; i < bookList.length(); ++i) {
                JSONObject book = bookList.getJSONObject(i);
                String title = book.getString("title");
                String pages;
                if (book.has("pages")) {
                    pages = "" + book.getInt("pages");
                } else {
                    pages = "NA";
                }
                System.out.println("  " + title + ", pages: " + pages);
            }
        } catch (JSONException e) {
            System.out.println("Hmm. Something went wrong with book parsing: "
                    + e.getMessage());
        }

    }
}
