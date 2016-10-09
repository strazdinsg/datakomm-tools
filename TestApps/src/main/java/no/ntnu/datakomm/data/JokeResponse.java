package no.ntnu.datakomm.data;

/**
 * @author Girts Strazdins, 2016-10-09
 * The joke response in the form received from the joke server: 
 * http://api.icndb.com/jokes/random
 * It contains the type which should be "success", and "value" field which
 * contains the joke itself as the Joke structure
 */
public class JokeResponse {
    String type;
    Joke value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Joke getValue() {
        return value;
    }

    public void setValue(Joke value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return value +  "(" + type + ")";
    }
}
