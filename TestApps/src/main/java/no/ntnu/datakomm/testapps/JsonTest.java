package no.ntnu.datakomm.testapps;

import no.ntnu.datakomm.data.Person;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import no.ntnu.datakomm.data.JokeResponse;

/**
 * Testing un-marshalling of JSON to objects. 
 * Note: the ObjectMapper is quite picky. It requires field names to be in
 * double quotes. And it can not consume directly the JSON output of XStream,
 * because the XStream wraps the whole object as the main variable name
 * @author girts
 */
public class JsonTest {

    public static void main(String[] args) {
        try {
            String json = "{\n"
                    + "  \"firstName\": \"Bill\",\n"
                    + "  \"lastName\": \"Gates\",\n"
                    + "  \"age\": 60,\n"
                    + "  \"phone\": \"555-555-5\"\n"
                    + "}";

            ObjectMapper mapper = new ObjectMapper();
            Person p = mapper.readValue(json, Person.class);

            System.out.println("Restored person: ");
            System.out.println(p);

            // This mapper requires field names always in double quotes. Won't
            // work directly with joke fetched from http://api.icndb.com/jokes/random
            String jokeJSON =
                    "{\n"
                    + "\"type\": \"success\",\n"
                    + "\"value\": {\n"
                    + "\"id\": 563,\n"
                    + "\"joke\": \"Chuck Norris causes the Windows Blue Screen of Death.\",\n"
                    + "\"categories\": [\n"
                    + "\"nerdy\", \"testing\"\n"
                    + "]\n"
                    + "}\n"
                    + "}";
            JokeResponse joke = mapper.readValue(jokeJSON, JokeResponse.class);
            System.out.println("Restored joke: ");
            System.out.println(joke);

        } catch (IOException ex) {
            System.out.println("Error while parsing: " + ex.getMessage());
        }
    }
}
