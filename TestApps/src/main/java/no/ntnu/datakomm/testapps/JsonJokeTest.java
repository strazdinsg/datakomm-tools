package no.ntnu.datakomm.testapps;

import no.ntnu.alesund.FetchWebString;
import no.ntnu.alesund.JsonMarshalling;
import no.ntnu.datakomm.data.JokeResponse;

/**
 * Fetch a hoke from http://api.icndb.com/jokes/random , parse it to 
 * JokeResponse object
 *
 * @author Girts Strazdins, 2016-10-09
 */
public class JsonJokeTest {

    public static void main(String[] args) {
        String url = "http://api.icndb.com/jokes/random";
        FetchWebString fetcher = new FetchWebString();
//        fetcher.setDebug(true);
        String jokeJSON = fetcher.httpGet(url, null);
        // Hack the response - add main object alias
        jokeJSON = "{\"joke\": " + jokeJSON + "}";
        System.out.println("Json response: ");
        System.out.println(jokeJSON);
        
        
        JsonMarshalling marshalling = new JsonMarshalling();
//        marshalling.setDebug(true);
        JokeResponse joke = (JokeResponse) marshalling.unmarshall(
                jokeJSON, JokeResponse.class);
        System.out.println("Restored joke: ");
        System.out.println(joke != null ? joke.getValue() : "no joke");

    }
}
